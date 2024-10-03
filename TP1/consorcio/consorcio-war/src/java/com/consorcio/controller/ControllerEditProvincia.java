package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Pais;
import com.ntm.consorcio.domain.entity.Provincia;
import com.ntm.consorcio.logic.entity.PaisServiceBean;
import com.ntm.consorcio.logic.entity.ProvinciaServiceBean;
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
 * Controlador para editProvincia
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditProvincia implements Serializable {

    private @EJB ProvinciaServiceBean provinciaServiceBean;
    private @EJB PaisServiceBean paisService;
    
    private Provincia provincia;
    private String nombre;
    private String idPais;
    
    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> paises = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el país de la sesión
            provincia = (Provincia) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PROVINCIA");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargar();

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = provincia.getNombre();
                idPais = provincia.getPais().getId();
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
                provinciaServiceBean.crearProvincia(nombre, idPais);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                provinciaServiceBean.modificarProvincia(provincia.getId(), nombre, idPais);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listProvincia";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listProvincia";
    }

    /**
     * Carga los paises para el menú desplegable
     */
    public void cargar() {
        try {  
            paises = new ArrayList<>();
            paises.add(new SelectItem(null, "Seleccione..."));
            for (Pais pais: paisService.listarPaisActivo()) {
                paises.add(new SelectItem(pais.getId(), pais.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public ProvinciaServiceBean getProvinciaServiceBean() {
        return provinciaServiceBean;
    }

    public void setProvinciaServiceBean(ProvinciaServiceBean provinciaServiceBean) {
        this.provinciaServiceBean = provinciaServiceBean;
    }

    public PaisServiceBean getPaisService() {
        return paisService;
    }

    public void setPaisService(PaisServiceBean paisService) {
        this.paisService = paisService;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
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

    public Collection<SelectItem> getPaises() {
        return paises;
    }

    public void setPaises(Collection<SelectItem> paises) {
        this.paises = paises;
    }
    
    
}
