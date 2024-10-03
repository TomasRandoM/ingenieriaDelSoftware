
package com.consorcio.controller;


import com.ntm.consorcio.domain.entity.Pais;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.entity.PaisServiceBean;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 * Controlador de vista de listPais
 * @version 1.0.0
 * @author Tomas Rando
 */
@ManagedBean
@ViewScoped
public class ControllerListPais {
    
    private @EJB PaisServiceBean serviceBean;
    private Collection<Pais> paisList = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            listarPaises();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega todos los paises activos a paisList
     * @throws ErrorServiceException 
     */
    public void listarPaises() throws ErrorServiceException {
        try {
            paisList.clear();
            paisList.addAll(serviceBean.listarPaisActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:paisTabla");   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Getter de paisList
     * @return Collection
     */
    public Collection<Pais> getPaisList() {
        return this.paisList;
    }
    
    /**
     * Setter de paisList
     * @param paisList Collection
     */
    public void setListPais(Collection<Pais> paisList) {
        this.paisList = paisList;
    }
    
    /**
     * Método para manejar la opción de alta
     * @return String
     */
    public String alta() {
        try{
            guardarSession("ALTA", null);
            return "editPais";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de consulta
     * @param pais Pais
     * @return String
     */
    public String consultar(Pais pais) {
        try{
            guardarSession("CONSULTAR", pais);
            return "editPais";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de modificación
     * @param pais Pais
     * @return String
     */
    public String modificar(Pais pais) {
        try{
            guardarSession("MODIFICAR", pais);
            return "editPais";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    
    /**
     * Método para manejar la opción de baja 
     * @param pais 
     */
    public void baja(Pais pais) {
        try{
            serviceBean.eliminarPais(pais.getId());
            listarPaises();
            Messages.show("Baja realizada exitosamente", TypeMessages.MENSAJE);
            RequestContext.getCurrentInstance().update("formPpal:msj");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }
    
    /**
     * Guarda el caso de uso y el país en la sesión
     * @param casoDeUso String
     * @param pais Pais
     */
    private void guardarSession(String casoDeUso, Pais pais){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        //Guarda el caso de uso en el atributo CASO_DE_USO
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase()); 
        //Guarda el país en el atributo PAIS
        session.setAttribute("PAIS", pais);  
    }
    
}

