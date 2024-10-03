package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Consorcio;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.ConsorcioServiceBean;
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
 * Controlador de vista de listConsorcio
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListConsorcio {
    
    private @EJB ConsorcioServiceBean serviceBean;
    private Collection<Consorcio> consorcioList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarConsorcios();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los consorcioes activos a consorcioList
     * @throws ErrorServiceException 
     */
    public void listarConsorcios() throws ErrorServiceException {
        try {
            consorcioList.clear();
            consorcioList.addAll(serviceBean.listarConsorcioActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:consorcioTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de consorcioList
     * @return Collection
     */
    public Collection<Consorcio> getConsorcioList() {
        return this.consorcioList;
    }
    
    /**
     * Setter de consorcioList
     * @param consorcioList Collection
     */
    public void setListConsorcio(Collection<Consorcio> consorcioList) {
        this.consorcioList = consorcioList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editConsorcio";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param consorcio Consorcio
     * @return String
     */
    public String consultar(Consorcio consorcio) {
        try{
            guardarSession("CONSULTAR", consorcio);
            return "editConsorcio";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param consorcio Consorcio
     * @return String
     */
    public String modificar(Consorcio consorcio) {
        try{
            guardarSession("MODIFICAR", consorcio);
            return "editConsorcio";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param consorcio 
     */
    public void baja(Consorcio consorcio) {
        try{
            serviceBean.eliminarConsorcio(consorcio.getId());
            listarConsorcios();
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
     * @param consorcio Consorcio
     */
    private void guardarSession(String casoDeUso, Consorcio consorcio){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo CONSORCIO
        session.setAttribute("CONSORCIO", consorcio);  
    }
    
}

