
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Departamento;
import com.ntm.consorcio.domain.entity.Localidad;
import com.ntm.consorcio.logic.entity.DepartamentoServiceBean;
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
 * Controlador de editLocalidad
 * @author Tomas Rando
 * @version 1.0.0
 */
@ManagedBean
@ViewScoped
public class ControllerEditLocalidad {

    private @EJB LocalidadServiceBean localidadServiceBean;
    private @EJB DepartamentoServiceBean departamentoService;
    private String casoDeUso;
    private Localidad localidad;
    private String nombre;
    private String idDepartamento;
    private String codigoPostal;
    
    
    private boolean desactivado;
    private Collection<SelectItem> departamentos = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el localidad de la sesión
            localidad = (Localidad) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOCALIDAD");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargar();

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = localidad.getNombre();
                idDepartamento = localidad.getDepartamento().getId();
                codigoPostal = localidad.getCodigoPostal();
                cargarPorProvincia(localidad.getDepartamento().getProvincia().getId());
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
            //Si el caso de uso es alta, crea el localidad
            if (casoDeUso.equals("ALTA")) {
                localidadServiceBean.crearLocalidad(nombre, codigoPostal ,idDepartamento);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                localidadServiceBean.modificarLocalidad(localidad.getId(), nombre, codigoPostal, idDepartamento);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listLocalidad";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listLocalidad";
    }

    /**
     * Carga los departamentos para el menú desplegable
     */
    public void cargar() {
        try {  
            departamentos = new ArrayList<>();
            departamentos.add(new SelectItem(null, "Seleccione..."));
            for (Departamento departamento: departamentoService.listarDepartamentoActivo()) {
                departamentos.add(new SelectItem(departamento.getId(), departamento.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Carga los departamentos para el menú desplegable por país
     */
    public void cargarPorProvincia(String id) {
        try {  
            departamentos = new ArrayList<>();
            departamentos.add(new SelectItem(null, "Seleccione..."));
            for (Departamento departamento: departamentoService.listarDepartamentoActivoPorProvincia(id)) {
                departamentos.add(new SelectItem(departamento.getId(), departamento.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public LocalidadServiceBean getLocalidadServiceBean() {
        return localidadServiceBean;
    }

    public void setLocalidadServiceBean(LocalidadServiceBean localidadServiceBean) {
        this.localidadServiceBean = localidadServiceBean;
    }

    public DepartamentoServiceBean getDepartamentoService() {
        return departamentoService;
    }

    public void setDepartamentoService(DepartamentoServiceBean departamentoService) {
        this.departamentoService = departamentoService;
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public boolean isDesactivado() {
        return desactivado;
    }

    public void setDesactivado(boolean desactivado) {
        this.desactivado = desactivado;
    }

    public Collection<SelectItem> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(Collection<SelectItem> departamentos) {
        this.departamentos = departamentos;
    }
    
    
}
