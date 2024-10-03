package com.consorcio.controller;

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
 * Controlador para listRecibo
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListRecibo implements Serializable {
    
    private @EJB ReciboServiceBean serviceBean;
    private Collection<Recibo> reciboList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarRecibo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los reciboes activos a reciboList
     * @throws ErrorServiceException 
     */
    public void listarRecibo() throws ErrorServiceException {
        try {
            reciboList.clear();
            reciboList.addAll(serviceBean.listarReciboActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:reciboTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de reciboList
     * @return Collection
     */
    public Collection<Recibo> getReciboList() {
        return this.reciboList;
    }
    
    /**
     * Setter de reciboList
     * @param reciboList Collection
     */
    public void setListRecibo(Collection<Recibo> reciboList) {
        this.reciboList = reciboList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editRecibo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param recibo Recibo
     * @return String
     */
    public String consultar(Recibo recibo) {
        try{
            guardarSession("CONSULTAR", recibo);
            return "editRecibo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param recibo Recibo
     * @return String
     */
    public String modificar(Recibo recibo) {
        try{
            guardarSession("MODIFICAR", recibo);
            return "editRecibo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param recibo 
     */
    public void baja(Recibo recibo) {
        try{
            serviceBean.eliminarRecibo(recibo.getId());
            listarRecibo();
            Messages.show("Baja realizada exitosamente", TypeMessages.MENSAJE);
            RequestContext.getCurrentInstance().update("formPpal:msj");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Gestiona la vista de los detalle de los recibos
     * @param rec Recibo
     * @return String
     */
    public String verDetalle(Recibo rec) {
        try{
            guardarSession("CONSULTAR", rec);
            return "listDetalleRecibo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Guarda el caso de uso y el país en la sesión
     * @param casoDeUso String
     * @param recibo Recibo
     */
    private void guardarSession(String casoDeUso, Recibo recibo){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo RECIBO
        session.setAttribute("RECIBO", recibo);  
    }
    
    /**
     * Muestra los departamentos del recibo
     * @param rec Recibo
     */
    public String getInmuebleInfo(Recibo rec) {
        String info = null;
        try {
            info = serviceBean.getInfoDpto(rec.getDetalleRecibo());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
        return info;
    }
    
    /**
     * Genera recibo y lo envia por mail
     * @param rec Recibo
     */
    public void enviarMail(Recibo rec) {
        try {
            serviceBean.generarYEnviarRecibo(rec.getId());
            Messages.show("Recibo enviado correctamente", TypeMessages.MENSAJE);
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
}

