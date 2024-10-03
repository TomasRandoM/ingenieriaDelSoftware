
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Menu;
import com.ntm.consorcio.domain.entity.Submenu;
import com.ntm.consorcio.domain.entity.Usuario;
import com.ntm.consorcio.logic.entity.MenuServiceBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;

/**
 * Controlador de sesión
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@RequestScoped
public class ControllerSession implements Serializable {

    //Clase de servicio de Menu
    private @EJB MenuServiceBean serviceBean;
    
    private Usuario usuario;
    private Collection<Menu> menuList = new ArrayList();
    
    private MenuModel model;
    private FacesContext context;
    private HashMap sessionMap = new HashMap();
    
    @PostConstruct
    public void init() {
        mostrarMenu();
    }
    
    /**
     * Configura los menús y submenús
     */
    public void mostrarMenu() {
        try {
            
            //Se crea el modelo que sirve para manejar menús dinámicos (desplegables, etc)
            model = new DefaultMenuModel();
            
            if (menuList == null || menuList.isEmpty()) {
                //Si el menuList está vacio, se llena con los menus activos
                menuList = serviceBean.listarMenuActivo();
            }
            
            //Agrega todos los menús que se mostrarán
            ArrayList<Menu> menusAMostrar = new ArrayList<>();
            menusAMostrar.addAll(menuList);
            
            //Ordena los menús teniendo en cuenta el atributo orden que poseen
            Collections.sort(menusAMostrar, new Comparator<Menu>() {
                @Override
                public int compare(Menu o1, Menu o2) {
                    return new Integer(o1.getOrden()).compareTo(o2.getOrden());
                }
            });
            
            for (Menu men : menusAMostrar) {
                ArrayList<Submenu> subMenuList = new ArrayList<>();
                //Agrega a la lista los subMenues del menú actual
                subMenuList.addAll(men.getSubmenu());
                
                //Ordena los menús teniendo en cuenta el atributo orden que poseen
                Collections.sort(subMenuList, new Comparator<Submenu>() {
                    @Override
                    public int compare(Submenu o1, Submenu o2) {
                        return new Integer(o1.getOrden()).compareTo(o2.getOrden());
                    }
                });
                
                if (subMenuList == null) {
                    //Crea un elemento de menu con el nombre del menu
                    MenuElement menuElem = new DefaultMenuItem(men.getNombre());
                    //Lo agrega al modelo
                    model.addElement(menuElem);
                } else {
                    //Si el menú tiene submenús se crea un objeto DefaultSubMenu con el nombre del menú
                    DefaultSubMenu subMenu = new DefaultSubMenu(men.getNombre());
                    for (Submenu sbMenu: subMenuList) {
                        //Se crea un elemento de menú para cada submenú
                        DefaultMenuItem item = new DefaultMenuItem(sbMenu.getNombre());
                        //Se configura la URL y el ícono
                        item.setUrl(sbMenu.getUrl());
                        item.setIcon("pi pi-bars");
                        //Se agrega el elemento de submenú al submenú
                        subMenu.addElement(item);
                    }
                    //Se agrega el submenú completo al modelo de menú
                    model.addElement(subMenu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Metodo para cerrar la sesión
     * @return String que será utilizado por faces-config.xml
     */
    public String logout() {
        clearSession();
        return "login"; 
    }
    
    /**
     * Metodo para limpiar la sesión
     */
    public void clearSession() {
        //Recupera la sesión (si existe) actual del usuario. Si no existe, devuelve null
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        //Si existe sesión, la invalida.
        if (session != null) {
            session.invalidate();
        }
    }
    
    /**
     * Obtener el usuario
     * @return Usuario
     */
    public Usuario getUsuario() {
        // Recuperamos el usuario de sesión. Si no está, devolvemos un usuario vacío
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        
        //Si el atributo usuarioLogin es null, significa que no contiene ningún usuario. Por ello, se crea un usuario vacío
        if (session.getAttribute("usuarioLogin") == null) {
            usuario = new Usuario();
        } else {
            //Si no es null, recupera el atributo, que contiene al Usuario y lo asigna a usuario.
            usuario = (Usuario) session.getAttribute("usuarioLogin");
        }
        return usuario;
    }
    
    /**
     * Setter de menuList
     * @param menuList Collection
     */
    public void setMenuList(Collection<Menu> menuList) {
        this.menuList = menuList;
    }
    
    /**
     * Getter de model
     * @return MenuModel
     */
    public MenuModel getModel() {
        return model;
    }
    
    /**
     * Setter de model
     * @param model MenuModel
     */
    public void setModel(MenuModel model) {
        this.model = model;
    }
}

