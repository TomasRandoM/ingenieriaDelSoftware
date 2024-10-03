package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Pais;
import com.ntm.consorcio.logic.entity.PaisServiceBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * Controlador de editPais
 * @version 1.0.0
 * @author Tomas Rando
 */

@ManagedBean
@ViewScoped
public class ControllerEditPais implements Serializable {

    private @EJB PaisServiceBean paisServiceBean;
    
    private Pais pais;
    private String nombre;
    
    private String casoDeUso;
    private boolean desactivado;

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el país de la sesión
            pais = (Pais) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PAIS");
            //Setea el campo desactivado en falso
            desactivado = false;
            
            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = pais.getNombre();
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
                paisServiceBean.crearPais(nombre);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                paisServiceBean.modificarPais(pais.getId(), nombre);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listPais";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listPais";
    }

    public PaisServiceBean getPaisServiceBean() {
        return paisServiceBean;
    }

    public void setPaisServiceBean(PaisServiceBean paisServiceBean) {
        this.paisServiceBean = paisServiceBean;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
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