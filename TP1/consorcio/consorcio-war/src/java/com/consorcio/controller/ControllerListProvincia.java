package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Provincia;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.ProvinciaServiceBean;
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
 * Controlador para listProvincia
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListProvincia {
    
    private @EJB ProvinciaServiceBean serviceBean;
    private Collection<Provincia> provinciaList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarProvincias();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los provinciaes activos a provinciaList
     * @throws ErrorServiceException 
     */
    public void listarProvincias() throws ErrorServiceException {
        try {
            provinciaList.clear();
            provinciaList.addAll(serviceBean.listarProvinciaActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:provinciaTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de provinciaList
     * @return Collection
     */
    public Collection<Provincia> getProvinciaList() {
        return this.provinciaList;
    }
    
    /**
     * Setter de provinciaList
     * @param provinciaList Collection
     */
    public void setListProvincia(Collection<Provincia> provinciaList) {
        this.provinciaList = provinciaList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editProvincia";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param provincia Provincia
     * @return String
     */
    public String consultar(Provincia provincia) {
        try{
            guardarSession("CONSULTAR", provincia);
            return "editProvincia";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param provincia Provincia
     * @return String
     */
    public String modificar(Provincia provincia) {
        try{
            guardarSession("MODIFICAR", provincia);
            return "editProvincia";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param provincia 
     */
    public void baja(Provincia provincia) {
        try{
            serviceBean.eliminarProvincia(provincia.getId());
            listarProvincias();
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
     * @param provincia Provincia
     */
    private void guardarSession(String casoDeUso, Provincia provincia){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo PROVINCIA
        session.setAttribute("PROVINCIA", provincia);  
    }
    
}
