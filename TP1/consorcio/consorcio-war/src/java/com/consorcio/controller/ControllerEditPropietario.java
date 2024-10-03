
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Direccion;
import com.ntm.consorcio.domain.entity.Propietario;
import com.ntm.consorcio.logic.entity.DireccionServiceBean;
import com.ntm.consorcio.logic.entity.PropietarioServiceBean;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Controlador para editPropietario
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditPropietario {
    
    private @EJB PropietarioServiceBean propietarioServiceBean;
    private @EJB DireccionServiceBean direccionService;
    
    private Propietario propietario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correoElectronico;
    private boolean esHabitante;
    private String idDireccion;

    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> direcciones = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el propietario de la sesión
            propietario = (Propietario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PROPIETARIO");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargar();

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = propietario.getNombre();
                apellido = propietario.getApellido();
                telefono = propietario.getTelefono();
                correoElectronico = propietario.getCorreoElectronico();
                esHabitante = propietario.getHabitaConsorcio();
                idDireccion = propietario.getDireccion().getId();
                //cargar();
                //cargarPorLocalidad(propietario.getDireccion().getLocalidad().getId());
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
            //Si el caso de uso es alta, crea el propietario
            if (casoDeUso.equals("ALTA")) {
                propietarioServiceBean.crearPropietario(nombre, apellido, telefono, correoElectronico, esHabitante, idDireccion);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                propietarioServiceBean.modificarPropietario(propietario.getId(), nombre, apellido, telefono, correoElectronico, esHabitante, idDireccion);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listPropietario";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listPropietario";
    }

    /**
     * Carga los direcciones para el menú desplegable
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
     * Carga los direcciones para el menú desplegable por departamento
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

    public PropietarioServiceBean getPropietarioServiceBean() {
        return propietarioServiceBean;
    }

    public void setPropietarioServiceBean(PropietarioServiceBean propietarioServiceBean) {
        this.propietarioServiceBean = propietarioServiceBean;
    }

    public DireccionServiceBean getDireccionService() {
        return direccionService;
    }

    public void setDireccionService(DireccionServiceBean direccionService) {
        this.direccionService = direccionService;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public boolean isEsHabitante() {
        return esHabitante;
    }

    public void setEsHabitante(boolean esHabitante) {
        this.esHabitante = esHabitante;
    }

    public String getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
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

    public Collection<SelectItem> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Collection<SelectItem> direcciones) {
        this.direcciones = direcciones;
    }
    
    
}
