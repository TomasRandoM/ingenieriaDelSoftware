package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.EstadoInmueble;
import com.ntm.consorcio.domain.entity.Inmueble;
import com.ntm.consorcio.domain.entity.Inquilino;
import com.ntm.consorcio.domain.entity.Propietario;
import com.ntm.consorcio.logic.entity.InmuebleServiceBean;
import com.ntm.consorcio.logic.entity.InquilinoServiceBean;
import com.ntm.consorcio.logic.entity.PropietarioServiceBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Controlador de editInmueble
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditInmueble implements Serializable {

    private @EJB InmuebleServiceBean inmuebleServiceBean;
    private @EJB InquilinoServiceBean inquilinoService;
    private @EJB PropietarioServiceBean propietarioService;
    
    private Inmueble inmueble;
    private String piso;
    private String dpto;
    private EstadoInmueble estadoInmueble;
    private String idPropietario;
    private String idInquilino;
    
    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> propietarios = new ArrayList();
    private Collection<SelectItem> inquilinos = new ArrayList();
    private Collection<SelectItem> estados = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el país de la sesión
            inmueble = (Inmueble) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("INMUEBLE");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargarEstados();
            cargarInquilinos();
            cargarPropietarios();
            
            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                piso = inmueble.getPiso();
                dpto = inmueble.getDpto();
                estadoInmueble = inmueble.getEstadoInmueble();
                idPropietario = inmueble.getPropietario() == null ? null : inmueble.getPropietario().getId();
                idInquilino = inmueble.getInquilino() == null ? null : inmueble.getInquilino().getId();
                
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
                inmuebleServiceBean.crearInmueble(piso, dpto, estadoInmueble, idPropietario, idInquilino);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                inmuebleServiceBean.modificarInmueble(inmueble.getId(), piso, dpto, estadoInmueble, idPropietario, idInquilino);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listInmueble";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listInmueble";
    }

    /**
     * Carga los propietarios para el menú desplegable
     */
    public void cargarPropietarios(){
      try{  
        
            propietarios = new ArrayList<>();
            propietarios.add(new SelectItem(null, "Seleccione..."));
            for (Propietario propietario: propietarioService.listarPropietarioActivo()) {
              propietarios.add(new SelectItem(propietario.getId(), propietario.getNombreApellido()));
            }
                
      }catch(Exception e){
        Messages.show(e.getMessage(), TypeMessages.ERROR);
      }
    }
    
    /**
     * Carga los inquilinos para el menú desplegable
     */
    public void cargarInquilinos(){
      try {  
            inquilinos = new ArrayList<>();
            inquilinos.add(new SelectItem(null, "Seleccione..."));
            for (Inquilino inquilino: inquilinoService.listarInquilinoActivo()) {
              inquilinos.add(new SelectItem(inquilino.getId(), inquilino.getNombreApellido()));
            }            
      } catch(Exception e) {
        Messages.show(e.getMessage(), TypeMessages.ERROR);
      }
    }
    
    /**
     * Carga los estados para el menú desplegable
     */
    public void cargarEstados(){
      try {
            estados.add(new SelectItem(null, "Seleccione..."));
            estados.add(new SelectItem(EstadoInmueble.OCUPADO, "OCUPADO"));
            estados.add(new SelectItem(EstadoInmueble.DESOCUPADO, "DESOCUPADO"));      
      } catch(Exception e) {
        Messages.show(e.getMessage(), TypeMessages.ERROR);
      }
    }
    
    public InmuebleServiceBean getInmuebleServiceBean() {
        return inmuebleServiceBean;
    }

    public void setInmuebleServiceBean(InmuebleServiceBean inmuebleServiceBean) {
        this.inmuebleServiceBean = inmuebleServiceBean;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getDpto() {
        return dpto;
    }

    public void setDpto(String dpto) {
        this.dpto = dpto;
    }

    public EstadoInmueble getEstadoInmueble() {
        return estadoInmueble;
    }

    public void setEstadoInmueble(EstadoInmueble estadoInmueble) {
        this.estadoInmueble = estadoInmueble;
    }

    public String getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(String idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(String idInquilino) {
        this.idInquilino = idInquilino;
    }

    public Collection<SelectItem> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(Collection<SelectItem> propietarios) {
        this.propietarios = propietarios;
    }

    public Collection<SelectItem> getInquilinos() {
        return inquilinos;
    }

    public void setInquilinos(Collection<SelectItem> inquilinos) {
        this.inquilinos = inquilinos;
    }

    public Collection<SelectItem> getEstados() {
        return estados;
    }

    public void setEstados(Collection<SelectItem> estados) {
        this.estados = estados;
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