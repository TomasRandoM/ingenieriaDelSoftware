
package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.CuentaCorreo;
import com.ntm.consorcio.domain.entity.Consorcio;
import com.ntm.consorcio.logic.entity.CuentaCorreoServiceBean;
import com.ntm.consorcio.logic.entity.ConsorcioServiceBean;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * Controlador de editCuentaCorreo
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerEditCuentaCorreo {
    private @EJB CuentaCorreoServiceBean cuentaCorreoServiceBean;
    private @EJB ConsorcioServiceBean consorcioService;
    
    private CuentaCorreo cuentaCorreo;
    private String correo;
    private String smtp;
    private String puerto;
    private String clave;
    private boolean tls;
    private String idConsorcio;
    
    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> consorcios = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el cuentaCorreo de la sesión
            cuentaCorreo = (CuentaCorreo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CUENTA_CORREO");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargar();

            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                correo = cuentaCorreo.getCorreo();
                smtp = cuentaCorreo.getSmtp();
                puerto = cuentaCorreo.getPuerto();
                clave = cuentaCorreo.getClave();
                tls = cuentaCorreo.isTls();
                idConsorcio = cuentaCorreo.getConsorcio().getId();

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
            //Si el caso de uso es alta, crea el cuentaCorreo
            if (casoDeUso.equals("ALTA")) {
                cuentaCorreoServiceBean.crearCuentaCorreo(correo, clave, puerto, smtp, tls, idConsorcio);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                cuentaCorreoServiceBean.modificarCuentaCorreo(cuentaCorreo.getId(), correo, clave, puerto, smtp, tls, idConsorcio);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        return "listCuentaCorreo";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listCuentaCorreo";
    }

    /**
     * Carga los consorcios para el menú desplegable
     */
    public void cargar() {
        try {  
            consorcios = new ArrayList<>();
            consorcios.add(new SelectItem(null, "Seleccione..."));
            for (Consorcio consorcio: consorcioService.listarConsorcioActivo()) {
                consorcios.add(new SelectItem(consorcio.getId(), consorcio.getNombre()));
            }            
        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public CuentaCorreoServiceBean getCuentaCorreoServiceBean() {
        return cuentaCorreoServiceBean;
    }

    public void setCuentaCorreoServiceBean(CuentaCorreoServiceBean cuentaCorreoServiceBean) {
        this.cuentaCorreoServiceBean = cuentaCorreoServiceBean;
    }

    public ConsorcioServiceBean getConsorcioService() {
        return consorcioService;
    }

    public void setConsorcioService(ConsorcioServiceBean consorcioService) {
        this.consorcioService = consorcioService;
    }

    public CuentaCorreo getCuentaCorreo() {
        return cuentaCorreo;
    }

    public void setCuentaCorreo(CuentaCorreo cuentaCorreo) {
        this.cuentaCorreo = cuentaCorreo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public String getIdConsorcio() {
        return idConsorcio;
    }

    public void setIdConsorcio(String idConsorcio) {
        this.idConsorcio = idConsorcio;
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

    public Collection<SelectItem> getConsorcios() {
        return consorcios;
    }

    public void setConsorcios(Collection<SelectItem> consorcios) {
        this.consorcios = consorcios;
    }
    
    
    
}
