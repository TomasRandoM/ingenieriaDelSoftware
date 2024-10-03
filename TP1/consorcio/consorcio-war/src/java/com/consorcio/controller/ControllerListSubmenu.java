package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Submenu;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.SubmenuServiceBean;
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
 * Controlador para listSubMenu
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListSubmenu implements Serializable {
    
    private @EJB SubmenuServiceBean serviceBean;
    private Collection<Submenu> submenuList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarSubMenus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los submenues activos a submenuList
     * @throws ErrorServiceException 
     */
    public void listarSubMenus() throws ErrorServiceException {
        try {
            submenuList.clear();
            submenuList.addAll(serviceBean.listarSubMenuActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:submenuTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de submenuList
     * @return Collection
     */
    public Collection<Submenu> getSubmenuList() {
        return this.submenuList;
    }
    
    /**
     * Setter de submenuList
     * @param submenuList Collection
     */
    public void setListSubmenu(Collection<Submenu> submenuList) {
        this.submenuList = submenuList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editSubmenu";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param submenu SubMenu
     * @return String
     */
    public String consultar(Submenu submenu) {
        try{
            guardarSession("CONSULTAR", submenu);
            return "editSubmenu";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param submenu SubMenu
     * @return String
     */
    public String modificar(Submenu submenu) {
        try{
            guardarSession("MODIFICAR", submenu);
            return "editSubmenu";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param submenu 
     */
    public void baja(Submenu submenu) {
        try{
            serviceBean.eliminarSubMenu(submenu.getId());
            listarSubMenus();
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
     * @param submenu SubMenu
     */
    private void guardarSession(String casoDeUso, Submenu submenu){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo SUBMENU
        session.setAttribute("SUBMENU", submenu);  
    }
    
}
