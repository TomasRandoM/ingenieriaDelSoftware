package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Nacionalidad;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.NacionalidadServiceBean;
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
 * Controlador de vista de listNacionalidad
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListNacionalidad {
    
    private @EJB NacionalidadServiceBean serviceBean;
    private Collection<Nacionalidad> nacionalidadList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarNacionalidades();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los nacionalidades activos a nacionalidadList
     * @throws ErrorServiceException 
     */
    public void listarNacionalidades() throws ErrorServiceException {
        try {
            nacionalidadList.clear();
            nacionalidadList.addAll(serviceBean.listarNacionalidad());
            
            RequestContext.getCurrentInstance().update("formPpal:nacionalidadTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de nacionalidadList
     * @return Collection
     */
    public Collection<Nacionalidad> getNacionalidadList() {
        return this.nacionalidadList;
    }
    
    /**
     * Setter de nacionalidadList
     * @param nacionalidadList Collection
     */
    public void setListNacionalidad(Collection<Nacionalidad> nacionalidadList) {
        this.nacionalidadList = nacionalidadList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editNacionalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param nacionalidad Nacionalidad
     * @return String
     */
    public String consultar(Nacionalidad nacionalidad) {
        try{
            guardarSession("CONSULTAR", nacionalidad);
            return "editNacionalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param nacionalidad Nacionalidad
     * @return String
     */
    public String modificar(Nacionalidad nacionalidad) {
        try{
            guardarSession("MODIFICAR", nacionalidad);
            return "editNacionalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param nacionalidad 
     */
    public void baja(Nacionalidad nacionalidad) {
        try{
            serviceBean.eliminarNacionalidad(nacionalidad.getId());
            listarNacionalidades();
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
     * @param nacionalidad Nacionalidad
     */
    private void guardarSession(String casoDeUso, Nacionalidad nacionalidad){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo NACIONALIDAD
        session.setAttribute("NACIONALIDAD", nacionalidad);  
    }
    
}

