
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Inquilino;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.InquilinoServiceBean;
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
 * Controlador para listInquilino
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListInquilino implements Serializable {
    
    private @EJB InquilinoServiceBean serviceBean;
    private Collection<Inquilino> inquilinoList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarInquilino();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los inquilinoes activos a inquilinoList
     * @throws ErrorServiceException 
     */
    public void listarInquilino() throws ErrorServiceException {
        try {
            inquilinoList.clear();
            inquilinoList.addAll(serviceBean.listarInquilinoActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:inquilinoTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de inquilinoList
     * @return Collection
     */
    public Collection<Inquilino> getInquilinoList() {
        return this.inquilinoList;
    }
    
    /**
     * Setter de inquilinoList
     * @param inquilinoList Collection
     */
    public void setListInquilino(Collection<Inquilino> inquilinoList) {
        this.inquilinoList = inquilinoList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editInquilino";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param inquilino Inquilino
     * @return String
     */
    public String consultar(Inquilino inquilino) {
        try{
            guardarSession("CONSULTAR", inquilino);
            return "editInquilino";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param inquilino Inquilino
     * @return String
     */
    public String modificar(Inquilino inquilino) {
        try{
            guardarSession("MODIFICAR", inquilino);
            return "editInquilino";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param inquilino 
     */
    public void baja(Inquilino inquilino) {
        try{
            serviceBean.eliminarInquilino(inquilino.getId());
            listarInquilino();
            Messages.show("Baja realizada exitosamente", TypeMessages.MENSAJE);
            RequestContext.getCurrentInstance().update("formPpal:msj");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Guarda el caso de uso y el país en la sesión
     * @param casoDeUso String
     * @param inquilino Inquilino
     */
    private void guardarSession(String casoDeUso, Inquilino inquilino){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo INQUILINO
        session.setAttribute("INQUILINO", inquilino);  
    }
    
}


