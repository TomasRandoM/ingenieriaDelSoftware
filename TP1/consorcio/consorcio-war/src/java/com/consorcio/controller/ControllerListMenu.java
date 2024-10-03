package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Menu;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.MenuServiceBean;
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
 * Controlador para listMenu
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListMenu implements Serializable {
    
    private @EJB MenuServiceBean serviceBean;
    private Collection<Menu> menuList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarMenus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los menues activos a menuList
     * @throws ErrorServiceException 
     */
    public void listarMenus() throws ErrorServiceException {
        try {
            menuList.clear();
            menuList.addAll(serviceBean.listarMenuActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:menuTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de menuList
     * @return Collection
     */
    public Collection<Menu> getMenuList() {
        return this.menuList;
    }
    
    /**
     * Setter de menuList
     * @param menuList Collection
     */
    public void setListMenu(Collection<Menu> menuList) {
        this.menuList = menuList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editMenu";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param menu Menu
     * @return String
     */
    public String consultar(Menu menu) {
        try{
            guardarSession("CONSULTAR", menu);
            return "editMenu";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param menu Menu
     * @return String
     */
    public String modificar(Menu menu) {
        try{
            guardarSession("MODIFICAR", menu);
            return "editMenu";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param menu 
     */
    public void baja(Menu menu) {
        try{
            serviceBean.eliminarMenu(menu.getId());
            listarMenus();
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
     * @param menu Menu
     */
    private void guardarSession(String casoDeUso, Menu menu){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo MENU
        session.setAttribute("MENU", menu);  
    }
    
}