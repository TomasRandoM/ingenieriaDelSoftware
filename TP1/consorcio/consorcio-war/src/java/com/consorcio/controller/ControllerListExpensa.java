package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Expensa;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.ExpensaServiceBean;

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
 * Controladr de ListExpensa
 * @version 1.0.0
 * @author Mauro Sorbello
 */
@ManagedBean
@ViewScoped
public class ControllerListExpensa {

    private @EJB ExpensaServiceBean serviceBean;
    private Collection<Expensa> expensaList = new ArrayList();
    
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
            expensaList.clear();
            expensaList.addAll(serviceBean.listarExpensaActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:expensaTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de la lista
     * @return Collection
     */
    public Collection<Expensa> getExpensaList() {
        return this.expensaList;
    }
    
    /**
     * Setter de lista
     * @param list Collection
     */
    public void setListExpensa(Collection<Expensa> list) {
        this.expensaList = list;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editExpensa";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param expensa Expensa
     * @return String
     */
    public String consultar(Expensa expensa) {
        try{
            guardarSession("CONSULTAR", expensa);
            return "editExpensa";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param expensa Expensa
     * @return String
     */
    public String modificar(Expensa expensa) {
        try{
            guardarSession("MODIFICAR", expensa);
            return "editExpensa";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param expensa Expensa
     */
    public void baja(Expensa expensa) {
        try{
            serviceBean.eliminarExpensa(expensa.getId());
            listar();
            Messages.show("Baja realizada exitosamente", TypeMessages.MENSAJE);
            RequestContext.getCurrentInstance().update("formPpal:msj");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Guarda el caso de uso y el expensa en la sesión
     * @param casoDeUso String
     * @param expensa Expensa
     */
    private void guardarSession(String casoDeUso, Expensa expensa){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el expense en el atributo EXPENSA
        session.setAttribute("EXPENSA", expensa);  
    }
    
}