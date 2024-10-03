package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Consorcio;
import com.ntm.consorcio.domain.entity.Direccion;
import com.ntm.consorcio.logic.entity.ConsorcioServiceBean;
import com.ntm.consorcio.logic.entity.DireccionServiceBean;
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
 * Controlador de editConsorcio
 * @version 1.0.0
 * @author Tomas Rando
 */

@ManagedBean
@ViewScoped
public class ControllerEditConsorcio implements Serializable {

    private @EJB ConsorcioServiceBean consorcioServiceBean;
    private @EJB DireccionServiceBean direccionService;
    
    private Consorcio consorcio;
    private String nombre;
    private String idDireccion;

    private Collection<SelectItem> direcciones = new ArrayList();
    private String casoDeUso;
    private boolean desactivado;

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el país de la sesión
            consorcio = (Consorcio) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONSORCIO");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargar();
            
            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = consorcio.getNombre();
                idDireccion = consorcio.getDireccion().getId();
                //cargarPorLocalidad(consorcio.getDireccion().getLocalidad().getId());
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
                consorcioServiceBean.crearConsorcio(nombre, idDireccion);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                consorcioServiceBean.modificarConsorcio(consorcio.getId(), nombre, idDireccion);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listConsorcio";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listConsorcio";
    }

    /**
     * Función para cargar las direcciones
     */
    public void cargar() {
        try {  
            direcciones = new ArrayList<>();
            direcciones.add(new SelectItem(null, "Seleccione..."));
            for (Direccion direccion: direccionService.listarDireccionActivo()) {
                direcciones.add(new SelectItem(direccion.getId(), direccion.getCalleNumeracion()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Carga los direcciones para el menú desplegable por localidad
     */
    public void cargarPorLocalidad(String id) {
        try {  
            direcciones = new ArrayList<>();
            direcciones.add(new SelectItem(null, "Seleccione..."));
            for (Direccion direccion: direccionService.listarDireccionActivoPorLocalidad(id)) {
                direcciones.add(new SelectItem(direccion.getId(), direccion.getCalleNumeracion()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public ConsorcioServiceBean getConsorcioServiceBean() {
        return consorcioServiceBean;
    }

    public void setConsorcioServiceBean(ConsorcioServiceBean consorcioServiceBean) {
        this.consorcioServiceBean = consorcioServiceBean;
    }

    public DireccionServiceBean getDireccionService() {
        return direccionService;
    }

    public void setDireccionService(DireccionServiceBean direccionService) {
        this.direccionService = direccionService;
    }

    public Consorcio getConsorcio() {
        return consorcio;
    }

    public void setConsorcio(Consorcio consorcio) {
        this.consorcio = consorcio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
    }

    public Collection<SelectItem> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Collection<SelectItem> direcciones) {
        this.direcciones = direcciones;
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
