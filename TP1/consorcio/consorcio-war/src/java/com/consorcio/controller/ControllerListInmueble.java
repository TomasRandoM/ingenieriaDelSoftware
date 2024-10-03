package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Inmueble;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.InmuebleServiceBean;

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
 * Controladr de ListInmueble
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListInmueble {

    private @EJB InmuebleServiceBean serviceBean;
    private Collection<Inmueble> inmuebleList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los objetos activos a la lista
     * @throws ErrorServiceException 
     */
    public void listar() throws ErrorServiceException {
        try {
            inmuebleList.clear();
            inmuebleList.addAll(serviceBean.listarInmueblesActivos());
            
            RequestContext.getCurrentInstance().update("formPpal:inmuebleTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de la lista
     * @return Collection
     */
    public Collection<Inmueble> getInmuebleList() {
        return this.inmuebleList;
    }
    
    /**
     * Setter de lista
     * @param list Collection
     */
    public void setListInmueble(Collection<Inmueble> list) {
        this.inmuebleList = list;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editInmueble";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param inmueble Inmueble
     * @return String
     */
    public String consultar(Inmueble inmueble) {
        try{
            guardarSession("CONSULTAR", inmueble);
            return "editInmueble";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param inmueble Inmueble
     * @return String
     */
    public String modificar(Inmueble inmueble) {
        try{
            guardarSession("MODIFICAR", inmueble);
            return "editInmueble";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param inmueble Inmueble
     */
    public void baja(Inmueble inmueble) {
        try{
            serviceBean.eliminarInmueble(inmueble.getId());
            listar();
            Messages.show("Baja realizada exitosamente", TypeMessages.MENSAJE);
            RequestContext.getCurrentInstance().update("formPpal:msj");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Guarda el caso de uso y el inmueble en la sesión
     * @param casoDeUso String
     * @param inmueble Inmueble
     */
    private void guardarSession(String casoDeUso, Inmueble inmueble){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el inmueble en el atributo INMUEBLE
        session.setAttribute("INMUEBLE", inmueble);  
    }
    
}
