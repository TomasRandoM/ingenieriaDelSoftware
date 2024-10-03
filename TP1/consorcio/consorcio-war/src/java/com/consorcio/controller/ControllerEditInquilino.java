
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.Nacionalidad;
import com.ntm.consorcio.domain.entity.Inquilino;
import com.ntm.consorcio.domain.entity.SexoEnum;
import com.ntm.consorcio.domain.entity.TipoDocumentoEnum;
import com.ntm.consorcio.logic.entity.NacionalidadServiceBean;
import com.ntm.consorcio.logic.entity.InquilinoServiceBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Controlador para editInquilino
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditInquilino {
    
    private @EJB InquilinoServiceBean inquilinoServiceBean;
    private @EJB NacionalidadServiceBean nacionalidadService;
    
    private Inquilino inquilino;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correoElectronico;
    private SexoEnum sexo;
    private String documento;
    private TipoDocumentoEnum tipoDocumento;
    private String idNacionalidad;
    private Date fechaNacimiento;
    private Date minDate;

    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> sexos = new ArrayList();
    private Collection<SelectItem> tiposDocumento = new ArrayList();
    private Collection<SelectItem> nacionalidades = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el inquilino de la sesión
            inquilino = (Inquilino) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("INQUILINO");
            //Setea el campo desactivado en falso
            desactivado = false;
            Calendar calendar = Calendar.getInstance();
            calendar.set(1900, Calendar.JANUARY, 1);
            minDate = calendar.getTime();
            cargarSexos();
            cargarTipos();
            cargarNacionalidades();

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                nombre = inquilino.getNombre();
                apellido = inquilino.getApellido();
                telefono = inquilino.getTelefono();
                correoElectronico = inquilino.getCorreoElectronico();
                sexo = inquilino.getSexo();
                documento = inquilino.getDocumento();
                tipoDocumento = inquilino.getTipoDocumento();
                fechaNacimiento = inquilino.getFechaNacimiento();
                idNacionalidad = inquilino.getNacionalidad().getId();

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
            //Si el caso de uso es alta, crea el inquilino
            if (casoDeUso.equals("ALTA")) {
                inquilinoServiceBean.crearInquilino(nombre, apellido, telefono, documento, tipoDocumento, correoElectronico, sexo, fechaNacimiento, idNacionalidad);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                inquilinoServiceBean.modificarInquilino(inquilino.getId(), nombre, apellido, telefono, documento, tipoDocumento, correoElectronico, sexo, fechaNacimiento, idNacionalidad);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listInquilino";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listInquilino";
    }

    /**
     * Carga los nacionalidades para el menú desplegable
     */
    public void cargarNacionalidades() {
        try {  
            nacionalidades = new ArrayList<>();
            nacionalidades.add(new SelectItem(null, "Seleccione..."));
            for (Nacionalidad nacionalidad: nacionalidadService.listarNacionalidad()) {
                nacionalidades.add(new SelectItem(nacionalidad.getId(), nacionalidad.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Carga los sexos para el menú desplegable
     */
    public void cargarSexos() {
        try {  
            sexos.add(new SelectItem(null, "Seleccione..."));
            sexos.add(new SelectItem(SexoEnum.MASCULINO, "MASCULINO"));
            sexos.add(new SelectItem(SexoEnum.FEMENINO, "FEMENINO"));
            sexos.add(new SelectItem(SexoEnum.OTRO, "OTRO"));
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    /**
     * Carga los tipos para el menú desplegable
     */
    public void cargarTipos() {
        try {  
            tiposDocumento.add(new SelectItem(null, "Seleccione..."));
            tiposDocumento.add(new SelectItem(TipoDocumentoEnum.DOCUMENTO_UNICO, "DOCUMENTO UNICO"));
            tiposDocumento.add(new SelectItem(TipoDocumentoEnum.LIBRETA_ENROLAMIENTO, "LIBRETA DE ENROLAMIENTO"));
            tiposDocumento.add(new SelectItem(TipoDocumentoEnum.LIBRETA_CIVICA, "LIBRETA CIVICA"));
            tiposDocumento.add(new SelectItem(TipoDocumentoEnum.PASAPORTE, "PASAPORTE"));
            tiposDocumento.add(new SelectItem(TipoDocumentoEnum.NO_ESPECIFICADO, "NO ESPECIFICADO"));
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public InquilinoServiceBean getInquilinoServiceBean() {
        return inquilinoServiceBean;
    }

    public void setInquilinoServiceBean(InquilinoServiceBean inquilinoServiceBean) {
        this.inquilinoServiceBean = inquilinoServiceBean;
    }

    public NacionalidadServiceBean getNacionalidadService() {
        return nacionalidadService;
    }

    public void setNacionalidadService(NacionalidadServiceBean nacionalidadService) {
        this.nacionalidadService = nacionalidadService;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
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

    public SexoEnum getSexo() {
        return sexo;
    }

    public void setSexo(SexoEnum sexo) {
        this.sexo = sexo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public TipoDocumentoEnum getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoEnum tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getIdNacionalidad() {
        return idNacionalidad;
    }

    public void setIdNacionalidad(String idNacionalidad) {
        this.idNacionalidad = idNacionalidad;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public Collection<SelectItem> getSexos() {
        return sexos;
    }

    public void setSexos(Collection<SelectItem> sexos) {
        this.sexos = sexos;
    }

    public Collection<SelectItem> getTiposDocumento() {
        return tiposDocumento;
    }

    public void setTiposDocumento(Collection<SelectItem> tiposDocumento) {
        this.tiposDocumento = tiposDocumento;
    }

    public Collection<SelectItem> getNacionalidades() {
        return nacionalidades;
    }

    public void setNacionalidades(Collection<SelectItem> nacionalidades) {
        this.nacionalidades = nacionalidades;
    }
    
    

}

