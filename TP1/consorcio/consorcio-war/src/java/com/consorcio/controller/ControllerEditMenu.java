
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Menu;
import com.ntm.consorcio.logic.entity.MenuServiceBean;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Controlador para editMenu
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditMenu {
    
    private @EJB MenuServiceBean menuServiceBean;
    
    private Menu menu;
    private String nombre;
    private String icon;
    private int orden;

    private String casoDeUso;
    private boolean desactivado;

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el menu de la sesión
            menu = (Menu) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("MENU");
            //Setea el campo desactivado en falso
            desactivado = false;
            orden = 1;

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = menu.getNombre();
                icon = menu.getIcon();
                orden = menu.getOrden();

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
            //Si el caso de uso es alta, crea el menu
            if (casoDeUso.equals("ALTA")) {
                menuServiceBean.crearMenu(nombre, icon, orden);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                menuServiceBean.modificarMenu(menu.getId(), nombre, icon, orden);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listMenu";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listMenu";
    }

    public MenuServiceBean getMenuServiceBean() {
        return menuServiceBean;
    }

    public void setMenuServiceBean(MenuServiceBean menuServiceBean) {
        this.menuServiceBean = menuServiceBean;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
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

