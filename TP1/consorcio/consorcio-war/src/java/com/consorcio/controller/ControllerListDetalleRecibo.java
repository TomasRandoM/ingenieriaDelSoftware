package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.DetalleRecibo;
import com.ntm.consorcio.domain.entity.Recibo;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.ReciboServiceBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 * Controlador para listDetalleRecibo
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListDetalleRecibo implements Serializable {
    
    private @EJB ReciboServiceBean serviceBean;
    private Collection<DetalleRecibo> detalleReciboList = new ArrayList();
    Recibo recibo;
    
    @PostConstruct
    public void init() {
        recibo = (Recibo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("RECIBO");
        try {
            listarDetalleRecibo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los detalleReciboes activos a detalleReciboList
     * @throws ErrorServiceException 
     */
    public void listarDetalleRecibo() throws ErrorServiceException {
        try {
            detalleReciboList.clear();
            Collection<DetalleRecibo> collection;
            collection = serviceBean.listarDetalleReciboActivo(recibo);
            if (collection != null) {
                detalleReciboList.addAll(collection);
            }
            RequestContext.getCurrentInstance().update("formPpal:detalleReciboTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de detalleReciboList
     * @return Collection
     */
    public Collection<DetalleRecibo> getDetalleReciboList() {
        return this.detalleReciboList;
    }
    
    /**
     * Setter de detalleReciboList
     * @param detalleReciboList Collection
     */
    public void setListDetalleRecibo(Collection<DetalleRecibo> detalleReciboList) {
        this.detalleReciboList = detalleReciboList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null, recibo);
            return "editDetalleRecibo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param detalleRecibo DetalleRecibo
     * @return String
     */
    public String consultar(DetalleRecibo detalleRecibo) {
        try{
            guardarSession("CONSULTAR", detalleRecibo, recibo);
            return "editDetalleRecibo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param detalleRecibo DetalleRecibo
     * @return String
     */
    public String modificar(DetalleRecibo detalleRecibo) {
        try{
            guardarSession("MODIFICAR", detalleRecibo, recibo);
            return "editDetalleRecibo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param detalleRecibo 
     */
    public void baja(DetalleRecibo detalleRecibo) {
        try{
            serviceBean.eliminarDetalleRecibo(recibo.getId(), detalleRecibo.getId());
            recibo = serviceBean.buscarRecibo(recibo.getId());
            listarDetalleRecibo();
            Messages.show("Baja realizada exitosamente", TypeMessages.MENSAJE);
            RequestContext.getCurrentInstance().update("formPpal:msj");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Guarda el caso de uso y el detalleRecibo en la sesión
     * @param casoDeUso String
     * @param detalleRecibo DetalleRecibo
     */
    private void guardarSession(String casoDeUso, DetalleRecibo detalleRecibo, Recibo rec){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el detalleRecibo en el atributo DETALLE_RECIBO
        session.setAttribute("DETALLE_RECIBO", detalleRecibo);  
        session.setAttribute("RECIBO", rec);
    }
    
    /**
     * Vuelve al listado de recibos
     * @return String
     */
    public String volver() {
        return "listRecibo";
    }
    
}

