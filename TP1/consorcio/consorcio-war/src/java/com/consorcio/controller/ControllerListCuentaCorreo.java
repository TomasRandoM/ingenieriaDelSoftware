package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.CuentaCorreo;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.CuentaCorreoServiceBean;
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
 * Controlador de vista de listCuentaCorreo
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListCuentaCorreo {
    
    private @EJB CuentaCorreoServiceBean serviceBean;
    private Collection<CuentaCorreo> cuentaCorreoList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarCuentaCorreos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los cuentaCorreoes activos a cuentaCorreoList
     * @throws ErrorServiceException 
     */
    public void listarCuentaCorreos() throws ErrorServiceException {
        try {
            cuentaCorreoList.clear();
            cuentaCorreoList.add(serviceBean.listarCuentaCorreoActiva());
            
            RequestContext.getCurrentInstance().update("formPpal:cuentaCorreoTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de cuentaCorreoList
     * @return Collection
     */
    public Collection<CuentaCorreo> getCuentaCorreoList() {
        return this.cuentaCorreoList;
    }
    
    /**
     * Setter de cuentaCorreoList
     * @param cuentaCorreoList Collection
     */
    public void setListCuentaCorreo(Collection<CuentaCorreo> cuentaCorreoList) {
        this.cuentaCorreoList = cuentaCorreoList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editCuentaCorreo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param cuentaCorreo CuentaCorreo
     * @return String
     */
    public String consultar(CuentaCorreo cuentaCorreo) {
        try{
            guardarSession("CONSULTAR", cuentaCorreo);
            return "editCuentaCorreo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param cuentaCorreo CuentaCorreo
     * @return String
     */
    public String modificar(CuentaCorreo cuentaCorreo) {
        try{
            guardarSession("MODIFICAR", cuentaCorreo);
            return "editCuentaCorreo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param cuentaCorreo 
     */
    public void baja(CuentaCorreo cuentaCorreo) {
        try{
            serviceBean.eliminarCuentaCorreo(cuentaCorreo.getId());
            listarCuentaCorreos();
            Messages.show("Baja realizada exitosamente", TypeMessages.MENSAJE);
            RequestContext.getCurrentInstance().update("formPpal:msj");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Guarda el caso de uso y la cuentaCorreo en la sesión
     * @param casoDeUso String
     * @param cuentaCorreo CuentaCorreo
     */
    private void guardarSession(String casoDeUso, CuentaCorreo cuentaCorreo){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo CUENTA_CORREO
        session.setAttribute("CUENTA_CORREO", cuentaCorreo);  
    }
    
}

