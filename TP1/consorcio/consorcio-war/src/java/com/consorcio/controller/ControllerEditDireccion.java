
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Direccion;
import com.ntm.consorcio.domain.entity.Localidad;
import com.ntm.consorcio.logic.entity.DireccionServiceBean;
import com.ntm.consorcio.logic.entity.LocalidadServiceBean;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Controlador de editDireccion
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditDireccion {
    private @EJB DireccionServiceBean direccionServiceBean;
    private @EJB LocalidadServiceBean localidadService;
    
    private Direccion direccion;
    private String calle;
    private String numeracion;
    private String idLocalidad;
    
    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> localidades = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el direccion de la sesión
            direccion = (Direccion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("DIRECCION");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargar();

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                calle = direccion.getCalle();
                numeracion = direccion.getNumeracion();
                idLocalidad = direccion.getLocalidad().getId();
                cargarPorDepartamento(direccion.getLocalidad().getDepartamento().getId());
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
            //Si el caso de uso es alta, crea el direccion
            if (casoDeUso.equals("ALTA")) {
                direccionServiceBean.crearDireccion(calle, numeracion, idLocalidad);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                direccionServiceBean.modificarDireccion(direccion.getId(), calle, numeracion, idLocalidad);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listDireccion";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listDireccion";
    }

    /**
     * Carga los localidades para el menú desplegable
     */
    public void cargar() {
        try {  
            localidades = new ArrayList<>();
            localidades.add(new SelectItem(null, "Seleccione..."));
            for (Localidad localidad: localidadService.listarLocalidadActivo()) {
                localidades.add(new SelectItem(localidad.getId(), localidad.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Carga los localidades para el menú desplegable por departamento
     */
    public void cargarPorDepartamento(String id) {
        try {  
            localidades = new ArrayList<>();
            localidades.add(new SelectItem(null, "Seleccione..."));
            for (Localidad localidad: localidadService.listarLocalidadActivoPorDepartamento(id)) {
                localidades.add(new SelectItem(localidad.getId(), localidad.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public DireccionServiceBean getDireccionServiceBean() {
        return direccionServiceBean;
    }

    public void setDireccionServiceBean(DireccionServiceBean direccionServiceBean) {
        this.direccionServiceBean = direccionServiceBean;
    }

    public LocalidadServiceBean getLocalidadService() {
        return localidadService;
    }

    public void setLocalidadService(LocalidadServiceBean localidadService) {
        this.localidadService = localidadService;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public String getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(String idLocalidad) {
        this.idLocalidad = idLocalidad;
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

    public Collection<SelectItem> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(Collection<SelectItem> localidades) {
        this.localidades = localidades;
    }
    
}
