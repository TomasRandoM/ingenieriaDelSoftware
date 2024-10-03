package com.consorcio.controller;

import com.ntm.consorcio.domain.entity.EstadoExpensaInmueble;
import com.ntm.consorcio.domain.entity.Expensa;
import com.ntm.consorcio.domain.entity.ExpensaInmueble;
import com.ntm.consorcio.domain.entity.Inmueble;
import com.ntm.consorcio.logic.entity.InmuebleServiceBean;
import com.ntm.consorcio.logic.entity.ExpensaServiceBean;
import com.ntm.consorcio.logic.entity.ExpensaInmuebleServiceBean;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

/**
 * Controlador de editExpensaInmueble
 * @version 1.0.0
 * @author Mauro Sorbello
 */
@ManagedBean
@ViewScoped
public class ControllerEditExpensaInmueble {
    
    private @EJB InmuebleServiceBean inmuebleService;
    private @EJB ExpensaServiceBean expensaService;
    private @EJB ExpensaInmuebleServiceBean expensaInmuebleService;
    
    private ExpensaInmueble expensaInmueble;
    private Date periodoFecha;
    private String periodo;
    private String periodoConDia;
    private Date fechaFin;
    private EstadoExpensaInmueble estadoExpensaInmueble;
    private Date fechaVencimiento;
    private String idExpensa;
    private String idInmueble;
    private boolean eliminado;

    
    private String casoDeUso;
    private boolean desactivado;
    private Collection<SelectItem> expensas = new ArrayList();
    private Collection<SelectItem> inmuebles = new ArrayList();
    private Collection<SelectItem> estados = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            //Recibe el caso de uso de la sesión
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            //Recibe el país de la sesión
            expensaInmueble = (ExpensaInmueble) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("EXPENSAINMUEBLE");
            //Setea el campo desactivado en falso
            desactivado = false;
            cargarEstados();
            cargarInmuebles();
            //cargarExpensas();
            
            //Verifica el caso de uso. Si es consultar o modificar recibe el nombre
            if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                estadoExpensaInmueble = expensaInmueble.getEstado();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-");
                periodo= sdf.format(expensaInmueble.getPeriodo());
                
                idInmueble = expensaInmueble.getInmueble() == null ? null : expensaInmueble.getInmueble().getId();
                idExpensa = expensaInmueble.getExpensa() == null ? null : expensaInmueble.getExpensa().getId();
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
        try {
            SimpleDateFormat sdfe = new SimpleDateFormat("yyyy-MM-dd");
            periodoFecha = sdfe.parse(periodo);
        } catch (ParseException ex) {
            Logger.getLogger(ControllerEditExpensaInmueble.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            //Si el caso de uso es alta, crea el país
            if (casoDeUso.equals("ALTA")) {
                expensaInmuebleService.crearExpensaInmueble(periodoFecha, estadoExpensaInmueble,idInmueble,idExpensa);
                Messages.show("Alta exitosa", TypeMessages.MENSAJE);
            // Si el caso de uso es modificar, lo modifica
            } else if (casoDeUso.equals("MODIFICAR")) {
                expensaInmuebleService.modificarExpensaInmueble(expensaInmueble.getId(),periodoFecha,idInmueble,idExpensa, estadoExpensaInmueble);
                Messages.show("Modificación exitosa", TypeMessages.MENSAJE);
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
        
        return "listExpensaInmueble";
    }
    
    /**
     * Función para ejecutar luego de presionar cancelar
     * @return String
    */
    public String cancelar() {
        return "listExpensaInmueble";
    }
    
    /**
     * Carga las expensas para el menú desplegable
     */
    public void cargarExpensas(){
        //SimpleDateFormat sdfe = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String[] partes = periodo.split("-");
            int anio = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]) - 1;
            Calendar calendar = Calendar.getInstance();
            calendar.set(anio, mes, 1);
            int ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, ultimoDia);
            periodoFecha = calendar.getTime();
            expensas = new ArrayList<>();
            expensas.add(new SelectItem(null, "Seleccione..."));
            Expensa expensa = expensaService.buscarExpensa(periodoFecha);
            idExpensa = expensa.getId();
            expensas.add(new SelectItem(expensa.getId(), "Importe Actual $:" + String.valueOf(expensa.getImporte())));
            RequestContext.getCurrentInstance().update("formPpal:idExpensa");
        } catch(Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public void enviarWhatsapp(){
        
    }
    
    public void cargarInmuebles(){
        
        try {
            inmuebles = new ArrayList<>();
            inmuebles.add(new SelectItem(null, "Seleccione..."));
            for (Inmueble inmueble: inmuebleService.listarInmueblesActivos()) {
              inmuebles.add(new SelectItem(inmueble.getId(), "Piso: " +inmueble.getPiso()  + " | Puerta: " + inmueble.getDpto() + "| Propietario: " + inmueble.getPropietario().getNombreApellido()));
            }
        }catch(Exception e){
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }   
    }
    
     /**
     * Carga los estados para el menú desplegable
     */
    

    public void cargarEstados(){
      try {
            estados.add(new SelectItem(null, "Seleccione..."));
            estados.add(new SelectItem(EstadoExpensaInmueble.PAGADO, "PAGADO"));
            estados.add(new SelectItem(EstadoExpensaInmueble.PAGADO_VENCIDO, "PAGADO_VENCIDO"));  
            estados.add(new SelectItem(EstadoExpensaInmueble.PENDIENTE, "PENDIENTE"));      
            estados.add(new SelectItem(EstadoExpensaInmueble.PENDIENTE_VENCIDO, "PENDIENTE_VENCIDO"));      
            estados.add(new SelectItem(EstadoExpensaInmueble.ANULADO, "ANULADO"));      

      } catch(Exception e) {
        Messages.show(e.getMessage(), TypeMessages.ERROR);
      }
    }

    public InmuebleServiceBean getInmuebleService() {
        return inmuebleService;
    }

    public void setInmuebleService(InmuebleServiceBean inmuebleService) {
        this.inmuebleService = inmuebleService;
    }

    public ExpensaServiceBean getExpensaService() {
        return expensaService;
    }

    public void setExpensaService(ExpensaServiceBean expensaService) {
        this.expensaService = expensaService;
    }

    public ExpensaInmuebleServiceBean getExpensaInmuebleService() {
        return expensaInmuebleService;
    }

    public void setExpensaInmuebleService(ExpensaInmuebleServiceBean expensaInmuebleService) {
        this.expensaInmuebleService = expensaInmuebleService;
    }

    public ExpensaInmueble getExpensaInmueble() {
        return expensaInmueble;
    }

    public void setExpensaInmueble(ExpensaInmueble expensaInmueble) {
        this.expensaInmueble = expensaInmueble;
    }

    public Date getPeriodoFecha() {
        return periodoFecha;
    }

    public void setPeriodoFecha(Date periodoFecha) {
        this.periodoFecha = periodoFecha;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        if (periodo != null && periodo.matches("\\d{4}-\\d{2}")) {
            this.periodo = periodo + "-01"; // Agrega "-01" al final
        } else {
            this.periodo = periodo; // Manejo de caso no válido si es necesario
        }
    }
    public String getPeriodoConDia() {
        return periodoConDia;
    }

    public void setPeriodoConDia(String periodoConDia) {
        this.periodoConDia = periodoConDia;
    }

    

    public EstadoExpensaInmueble getEstadoExpensaInmueble() {
        return estadoExpensaInmueble;
    }

    public void setEstadoExpensaInmueble(EstadoExpensaInmueble estadoExpensaInmueble) {
        this.estadoExpensaInmueble = estadoExpensaInmueble;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getIdExpensa() {
        return idExpensa;
    }

    public void setIdExpensa(String idExpensa) {
        this.idExpensa = idExpensa;
    }

    public String getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(String idInmueble) {
        this.idInmueble = idInmueble;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
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

    public Collection<SelectItem> getExpensas() {
        return expensas;
    }

    public void setExpensas(Collection<SelectItem> expensas) {
        this.expensas = expensas;
    }

    public Collection<SelectItem> getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(Collection<SelectItem> inmuebles) {
        this.inmuebles = inmuebles;
    }

    public Collection<SelectItem> getEstados() {
        return estados;
    }

    public void setEstados(Collection<SelectItem> estados) {
        this.estados = estados;
    }
    
    
}
