package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Nacionalidad;
import com.ntm.consorcio.logic.entity.NacionalidadServiceBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * Controlador de editNacionalidad
 * @version 1.0.0
 * @author Tomas Rando
 */

@ManagedBean
@ViewScoped
public class ControllerEditNacionalidad implements Serializable {

    private @EJB NacionalidadServiceBean nacionalidadServiceBean;
    
    private Nacionalidad nacionalidad;
    private String nombre;
    
    private String casoDeUso;
    private boolean desactivado;

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el país de la sesión
            nacionalidad = (Nacionalidad) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("NACIONALIDAD");
            //Setea el campo desactivado en falso
            desactivado = false;
            
            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = nacionalidad.getNombre();
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
                nacionalidadServiceBean.crearNacionalidad(nombre);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                nacionalidadServiceBean.modificarNacionalidad(nacionalidad.getId(), nombre);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listNacionalidad";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listNacionalidad";
    }

    public NacionalidadServiceBean getNacionalidadServiceBean() {
        return nacionalidadServiceBean;
    }

    public void setNacionalidadServiceBean(NacionalidadServiceBean nacionalidadServiceBean) {
        this.nacionalidadServiceBean = nacionalidadServiceBean;
    }

    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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