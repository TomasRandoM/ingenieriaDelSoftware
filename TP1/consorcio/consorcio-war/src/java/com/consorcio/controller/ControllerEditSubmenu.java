package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Menu;
import com.ntm.consorcio.domain.entity.Submenu;
import com.ntm.consorcio.logic.entity.MenuServiceBean;
import com.ntm.consorcio.logic.entity.SubmenuServiceBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Controlador de editSubmenu
 * @version 1.0.0
 * @author Tomas Rando
 */

@ManagedBean
@ViewScoped
public class ControllerEditSubmenu implements Serializable {

    private @EJB SubmenuServiceBean submenuServiceBean;
    private @EJB MenuServiceBean menuService;
    
    private Submenu submenu;
    private String nombre;
    private int orden;
    private String url;
    private String idMenu;

    private Collection<SelectItem> menus = new ArrayList();
    private String casoDeUso;
    private boolean desactivado;

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el país de la sesión
            submenu = (Submenu) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SUBMENU");
            //Setea el campo desactivado en falso
            desactivado = false;
            orden = 1;
            cargar();
            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = submenu.getNombre();
                orden = submenu.getOrden();
                url = submenu.getUrl();
                //Si es consultar desactiva el campo
                if (casoDeUso.equals("CONSULTAR")) {
                    desactivado = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Messages.show(ex.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Función para ejecutar luego de presionar aceptar
     * @return String
     */
    public String aceptar() {
        try{
            //Si el caso de uso es alta, crea el país
            if (casoDeUso.equals("ALTA")) {
                submenuServiceBean.crearSubMenu(idMenu, nombre, url, orden);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                submenuServiceBean.modificarSubMenu(idMenu, submenu.getId(), nombre, url, orden);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listSubmenu";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listSubmenu";
    }

    /**
     * Carga los menús para el menú desplegable
     */
    public void cargar() {
        try {  
            menus = new ArrayList<>();
            menus.add(new SelectItem(null, "Seleccione..."));
            for (Menu menu: menuService.listarMenuActivo()) {
                menus.add(new SelectItem(menu.getId(), menu.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public SubmenuServiceBean getSubmenuServiceBean() {
        return submenuServiceBean;
    }

    public void setSubmenuServiceBean(SubmenuServiceBean submenuServiceBean) {
        this.submenuServiceBean = submenuServiceBean;
    }

    public MenuServiceBean getMenuService() {
        return menuService;
    }

    public void setMenuService(MenuServiceBean menuService) {
        this.menuService = menuService;
    }

    public Submenu getSubmenu() {
        return submenu;
    }

    public void setSubmenu(Submenu submenu) {
        this.submenu = submenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public Collection<SelectItem> getMenus() {
        return menus;
    }

    public void setMenus(Collection<SelectItem> menus) {
        this.menus = menus;
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public boolean isDesactivado() {
        return desactivado;
    }

    public void setDesactivado(boolean desactivado) {
        this.desactivado = desactivado;
    }
    
    
}