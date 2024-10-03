package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Departamento;
import com.ntm.consorcio.domain.entity.Provincia;
import com.ntm.consorcio.logic.entity.DepartamentoServiceBean;
import com.ntm.consorcio.logic.entity.ProvinciaServiceBean;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Controlador de editDepartamento
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditDepartamento {

    private @EJB DepartamentoServiceBean departamentoServiceBean;
    private @EJB ProvinciaServiceBean provinciaService;
    
    private Departamento departamento;
    private String nombre;
    private String idProvincia;
    
    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> provincias = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el departamento de la sesión
            departamento = (Departamento) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("DEPARTAMENTO");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargar();

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = departamento.getNombre();
                idProvincia = departamento.getProvincia().getId();
                cargarPorPais(departamento.getProvincia().getPais().getId());
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
            //Si el caso de uso es alta, crea el departamento
            if (casoDeUso.equals("ALTA")) {
                departamentoServiceBean.crearDepartamento(nombre, idProvincia);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                departamentoServiceBean.modificarDepartamento(departamento.getId(), nombre, idProvincia);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listDepartamento";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listDepartamento";
    }

    /**
     * Carga los provincias para el menú desplegable
     */
    public void cargar() {
        try {  
            provincias = new ArrayList<>();
            provincias.add(new SelectItem(null, "Seleccione..."));
            for (Provincia provincia: provinciaService.listarProvinciaActivo()) {
                provincias.add(new SelectItem(provincia.getId(), provincia.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Carga los provincias para el menú desplegable por país
     */
    public void cargarPorPais(String id) {
        try {  
            provincias = new ArrayList<>();
            provincias.add(new SelectItem(null, "Seleccione..."));
            for (Provincia provincia: provinciaService.listarProvinciaActivoPorPais(id)) {
                provincias.add(new SelectItem(provincia.getId(), provincia.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    

    public DepartamentoServiceBean getDepartamentoServiceBean() {
        return departamentoServiceBean;
    }

    public void setDepartamentoServiceBean(DepartamentoServiceBean departamentoServiceBean) {
        this.departamentoServiceBean = departamentoServiceBean;
    }

    public ProvinciaServiceBean getProvinciaService() {
        return provinciaService;
    }

    public void setProvinciaService(ProvinciaServiceBean provinciaService) {
        this.provinciaService = provinciaService;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
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

    public Collection<SelectItem> getProvincias() {
        return provincias;
    }

    public void setProvincias(Collection<SelectItem> provincias) {
        this.provincias = provincias;
    }

}
