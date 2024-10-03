
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.DetalleRecibo;
import com.ntm.consorcio.domain.entity.ExpensaInmueble;
import com.ntm.consorcio.domain.entity.Recibo;
import com.ntm.consorcio.logic.entity.ExpensaInmuebleServiceBean;
import com.ntm.consorcio.logic.entity.ReciboServiceBean;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 * Controlador para editDetalleRecibo
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditDetalleRecibo {
    
    private @EJB ReciboServiceBean reciboServiceBean;
    private @EJB ExpensaInmuebleServiceBean expensaInmuebleService;
    
    private DetalleRecibo detalleRecibo;
    private int cantidad;
    private double subtotal;
    private String idExpensaInmueble;
    private Recibo recibo;

    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> expensasInmuebles = new ArrayList();


    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el detalleRecibo de la sesión
            detalleRecibo = (DetalleRecibo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("DETALLE_RECIBO");
            recibo = (Recibo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("RECIBO");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargarExpensasInmuebles();

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                cantidad = detalleRecibo.getCantidad();
                subtotal = detalleRecibo.getSubtotal();
                idExpensaInmueble = detalleRecibo.getExpensaInmueble().getId();

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
            //Si el caso de uso es alta, crea el detalleRecibo
            if (casoDeUso.equals("ALTA")) {
                reciboServiceBean.crearDetalleRecibo(recibo.getId(), cantidad, idExpensaInmueble);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                reciboServiceBean.modificarDetalleRecibo(recibo.getId(), detalleRecibo.getId(), cantidad, idExpensaInmueble);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            recibo = reciboServiceBean.buscarRecibo(recibo.getId());
            guardarSession(recibo);
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listDetalleRecibo";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listDetalleRecibo";
    }

    /**
     * Carga las expensas inmuebles para el menú desplegable
     */
    public void cargarExpensasInmuebles() {
        try {  
            expensasInmuebles = new ArrayList<>();
            expensasInmuebles.add(new SelectItem(null, "Seleccione..."));
            for (ExpensaInmueble expensaInmueble: expensaInmuebleService.listarExpensaInmuebleActivo()) {
                expensasInmuebles.add(new SelectItem(expensaInmueble.getId(), expensaInmueble.getData()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
            
    }
    
    /**
     * Guarda el Recibo en la sesión
     * @param casoDeUso String
     * @param detalleRecibo DetalleRecibo
     */
    private void guardarSession(Recibo rec){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el Recibo en el atributo RECIBO
        session.setAttribute("RECIBO", rec);  
    }
    
    public ReciboServiceBean getReciboServiceBean() {
        return reciboServiceBean;
    }

    public void setReciboServiceBean(ReciboServiceBean reciboServiceBean) {
        this.reciboServiceBean = reciboServiceBean;
    }

    public ExpensaInmuebleServiceBean getExpensaInmuebleService() {
        return expensaInmuebleService;
    }

    public void setExpensaInmuebleService(ExpensaInmuebleServiceBean expensaInmuebleService) {
        this.expensaInmuebleService = expensaInmuebleService;
    }

    public DetalleRecibo getDetalleRecibo() {
        return detalleRecibo;
    }

    public void setDetalleRecibo(DetalleRecibo detalleRecibo) {
        this.detalleRecibo = detalleRecibo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getIdExpensaInmueble() {
        return idExpensaInmueble;
    }

    public void setIdExpensaInmueble(String idExpensaInmueble) {
        this.idExpensaInmueble = idExpensaInmueble;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
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

    public Collection<SelectItem> getExpensasInmuebles() {
        return expensasInmuebles;
    }

    public void setExpensasInmuebles(Collection<SelectItem> expensasInmuebles) {
        this.expensasInmuebles = expensasInmuebles;
    }
    
}
