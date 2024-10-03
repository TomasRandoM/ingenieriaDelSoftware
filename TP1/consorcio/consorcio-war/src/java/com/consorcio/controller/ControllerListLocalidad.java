package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Localidad;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.LocalidadServiceBean;
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
 * Controlador para listLocalidad
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListLocalidad implements Serializable {
    
    private @EJB LocalidadServiceBean serviceBean;
    private Collection<Localidad> localidadList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarLocalidad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los localidades activos a localidadList
     * @throws ErrorServiceException 
     */
    public void listarLocalidad() throws ErrorServiceException {
        try {
            localidadList.clear();
            localidadList.addAll(serviceBean.listarLocalidadActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:localidadTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de localidadList
     * @return Collection
     */
    public Collection<Localidad> getLocalidadList() {
        return this.localidadList;
    }
    
    /**
     * Setter de localidadList
     * @param localidadList Collection
     */
    public void setListLocalidad(Collection<Localidad> localidadList) {
        this.localidadList = localidadList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editLocalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param localidad Localidad
     * @return String
     */
    public String consultar(Localidad localidad) {
        try{
            guardarSession("CONSULTAR", localidad);
            return "editLocalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param localidad Localidad
     * @return String
     */
    public String modificar(Localidad localidad) {
        try{
            guardarSession("MODIFICAR", localidad);
            return "editLocalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param localidad 
     */
    public void baja(Localidad localidad) {
        try{
            serviceBean.eliminarLocalidad(localidad.getId());
            listarLocalidad();
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
     * @param localidad Localidad
     */
    private void guardarSession(String casoDeUso, Localidad localidad){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo LOCALIDAD
        session.setAttribute("LOCALIDAD", localidad);  
    }
    
}
