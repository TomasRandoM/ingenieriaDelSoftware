
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.FormaDePago;
import com.ntm.consorcio.domain.entity.Recibo;
import com.ntm.consorcio.logic.entity.ReciboServiceBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Controlador para editRecibo
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditRecibo {
    
    private @EJB ReciboServiceBean reciboServiceBean;
    
    private Recibo recibo;
    private Date fechaPago;
    private double total;
    private FormaDePago formaDePago;

    private Date minDate;
    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> formasDePago = new ArrayList();


    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el recibo de la sesión
            recibo = (Recibo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("RECIBO");
            //Setea el campo desactivado en falso
            desactivado = false;
            Calendar calendar = Calendar.getInstance();
            calendar.set(1900, Calendar.JANUARY, 1);
            minDate = calendar.getTime();
            cargarFormasDePago();

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                fechaPago = recibo.getFechaPago();
                total = recibo.getTotal();
                formaDePago = recibo.getFormaDePago();

                //Si es consultar desactiva el campo
                if (casoDeUso.equals("CONSULTAR")) {
                    desactivado = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Messages.show(ex.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Función para ejecutar luego de presionar aceptar
     * @return String
     */
    public String aceptar() {
        try{
            //Si el caso de uso es alta, crea el recibo
            if (casoDeUso.equals("ALTA")) {
                reciboServiceBean.crearRecibo(fechaPago, formaDePago);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                reciboServiceBean.modificarRecibo(recibo.getId(), fechaPago, formaDePago);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listRecibo";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listRecibo";
    }

    /**
     * Carga las formas de pago para el menú desplegable
     */
    public void cargarFormasDePago() {
        try {  
            formasDePago = new ArrayList<>();
            formasDePago.add(new SelectItem(null, "Seleccione..."));

            formasDePago.add(new SelectItem(FormaDePago.EFECTIVO, "EFECTIVO"));
            formasDePago.add(new SelectItem(FormaDePago.TRANSFERENCIA, "TRANSFERENCIA"));
            formasDePago.add(new SelectItem(FormaDePago.BILLETERA_VIRTUAL, "BILLETERA VIRTUAL"));
            formasDePago.add(new SelectItem(FormaDePago.TARJETA_DE_CREDITO, "TARJETA DE CREDITO"));
            formasDePago.add(new SelectItem(FormaDePago.TARJETA_DE_DEBITO, "TARJETA DE DEBITO"));

        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public ReciboServiceBean getReciboServiceBean() {
        return reciboServiceBean;
    }

    public void setReciboServiceBean(ReciboServiceBean reciboServiceBean) {
        this.reciboServiceBean = reciboServiceBean;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public FormaDePago getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(FormaDePago formaDePago) {
        this.formaDePago = formaDePago;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public boolean isDesactivado() {
        return desactivado;
    }

    public void setDesactivado(boolean desactivado) {
        this.desactivado = desactivado;
    }

    public Collection<SelectItem> getFormasDePago() {
        return formasDePago;
    }

    public void setFormasDePago(Collection<SelectItem> formasDePago) {
        this.formasDePago = formasDePago;
    }
    
}

