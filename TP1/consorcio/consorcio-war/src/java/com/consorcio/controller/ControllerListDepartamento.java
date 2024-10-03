package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Departamento;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.DepartamentoServiceBean;
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
 * Controlador para listDepartamento
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListDepartamento implements Serializable {
    
    private @EJB DepartamentoServiceBean serviceBean;
    private Collection<Departamento> departamentoList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarDepartamentos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los departamentoes activos a departamentoList
     * @throws ErrorServiceException 
     */
    public void listarDepartamentos() throws ErrorServiceException {
        try {
            departamentoList.clear();
            departamentoList.addAll(serviceBean.listarDepartamentoActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:departamentoTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de departamentoList
     * @return Collection
     */
    public Collection<Departamento> getDepartamentoList() {
        return this.departamentoList;
    }
    
    /**
     * Setter de departamentoList
     * @param departamentoList Collection
     */
    public void setListDepartamento(Collection<Departamento> departamentoList) {
        this.departamentoList = departamentoList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editDepartamento";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param departamento Departamento
     * @return String
     */
    public String consultar(Departamento departamento) {
        try{
            guardarSession("CONSULTAR", departamento);
            return "editDepartamento";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param departamento Departamento
     * @return String
     */
    public String modificar(Departamento departamento) {
        try{
            guardarSession("MODIFICAR", departamento);
            return "editDepartamento";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param departamento 
     */
    public void baja(Departamento departamento) {
        try{
            serviceBean.eliminarDepartamento(departamento.getId());
            listarDepartamentos();
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
     * @param departamento Departamento
     */
    private void guardarSession(String casoDeUso, Departamento departamento){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo DEPARTAMENTO
        session.setAttribute("DEPARTAMENTO", departamento);  
    }
    
}