package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Expensa;
import com.ntm.consorcio.logic.entity.ExpensaServiceBean;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * Controlador de editExpensa
 * @version 1.0.0
 * @author Mauro Sorbello
 */
@ManagedBean
@ViewScoped
public class ControllerEditExpensa {
    private @EJB ExpensaServiceBean expensaServiceBean;
    
    private Expensa expensa;
    private Date fechaDesde;
    private Date fechaHasta;
    private double monto;
    private String casoDeUso;
    private boolean desactivado;
    
    
    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el país de la sesión
            expensa = (Expensa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("EXPENSA");
            //Setea el campo desactivado en falso
            desactivado = false;
            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                fechaDesde = expensa.getFechaDesde();
                fechaHasta = expensa.getFechaHasta() == null ? null : expensa.getFechaHasta();
                monto = expensa.getImporte();
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
    
    public String aceptar() {
        try{
            //Si el caso de uso es alta, crea el país
            if (casoDeUso.equals("ALTA")) {
                expensaServiceBean.crearExpensa(fechaDesde, monto);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                expensaServiceBean.modificarExpensa(expensa.getId(), fechaDesde, fechaHasta, monto);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listExpensa";
    }
    
        /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listExpensa";
    }
    public void setDesactivado(boolean desactivado) {
        this.desactivado = desactivado;
    }
    private Date parseFecha(String fechaString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(fechaString); // Convertir String a Date
        } catch (ParseException e) {
            e.printStackTrace(); // Manejo de excepción
            return null;
        }
    }
    
    private String formatFecha(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fecha);
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
    
    public Expensa getExpensa() {
        return expensa;
    }

    public void setExpensa(Expensa expensa) {
        this.expensa = expensa;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
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
   

}
