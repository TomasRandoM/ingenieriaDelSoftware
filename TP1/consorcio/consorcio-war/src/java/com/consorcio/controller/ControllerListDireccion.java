package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Direccion;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.DireccionServiceBean;
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
 * Controlador para listDireccion
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListDireccion implements Serializable {
    
    private @EJB DireccionServiceBean serviceBean;
    private Collection<Direccion> direccionList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarDireccion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los direcciones activos a direccionList
     * @throws ErrorServiceException 
     */
    public void listarDireccion() throws ErrorServiceException {
        try {
            direccionList.clear();
            direccionList.addAll(serviceBean.listarDireccionActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:direccionTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de direccionList
     * @return Collection
     */
    public Collection<Direccion> getDireccionList() {
        return this.direccionList;
    }
    
    /**
     * Setter de direccionList
     * @param direccionList Collection
     */
    public void setListDireccion(Collection<Direccion> direccionList) {
        this.direccionList = direccionList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editDireccion";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param direccion Direccion
     * @return String
     */
    public String consultar(Direccion direccion) {
        try{
            guardarSession("CONSULTAR", direccion);
            return "editDireccion";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param direccion Direccion
     * @return String
     */
    public String modificar(Direccion direccion) {
        try{
            guardarSession("MODIFICAR", direccion);
            return "editDireccion";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param direccion 
     */
    public void baja(Direccion direccion) {
        try{
            serviceBean.eliminarDireccion(direccion.getId());
            listarDireccion();
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
     * @param direccion Direccion
     */
    private void guardarSession(String casoDeUso, Direccion direccion){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo DIRECCION
        session.setAttribute("DIRECCION", direccion);  
    }
    
}

