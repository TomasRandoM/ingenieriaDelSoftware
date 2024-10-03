
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Propietario;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.PropietarioServiceBean;
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
 * Controlador para listPropietario
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListPropietario implements Serializable {
    
    private @EJB PropietarioServiceBean serviceBean;
    private Collection<Propietario> propietarioList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarPropietario();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los propietarioes activos a propietarioList
     * @throws ErrorServiceException 
     */
    public void listarPropietario() throws ErrorServiceException {
        try {
            propietarioList.clear();
            propietarioList.addAll(serviceBean.listarPropietarioActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:propietarioTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de propietarioList
     * @return Collection
     */
    public Collection<Propietario> getPropietarioList() {
        return this.propietarioList;
    }
    
    /**
     * Setter de propietarioList
     * @param propietarioList Collection
     */
    public void setListPropietario(Collection<Propietario> propietarioList) {
        this.propietarioList = propietarioList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editPropietario";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param propietario Propietario
     * @return String
     */
    public String consultar(Propietario propietario) {
        try{
            guardarSession("CONSULTAR", propietario);
            return "editPropietario";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param propietario Propietario
     * @return String
     */
    public String modificar(Propietario propietario) {
        try{
            guardarSession("MODIFICAR", propietario);
            return "editPropietario";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param propietario 
     */
    public void baja(Propietario propietario) {
        try{
            serviceBean.eliminarPropietario(propietario.getId());
            listarPropietario();
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
     * @param propietario Propietario
     */
    private void guardarSession(String casoDeUso, Propietario propietario){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo PROPIETARIO
        session.setAttribute("PROPIETARIO", propietario);  
    }
    
}

