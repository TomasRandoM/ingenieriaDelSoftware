/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.persistence.entity.DAOExpensaBean;
import com.ntm.consorcio.domain.entity.Expensa;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Mauro Sorbello
 */
@Stateless
@LocalBean
public class ExpensaServiceBean {
    private @EJB DAOExpensaBean dao;

    
    public void crearExpensa(Date fechaDesde, double importe) throws ErrorServiceException {
        try {
            Expensa expensaActual = null;
            // Verificamos si la fecha de pago es null
            if (fechaDesde == null) {
                throw new ErrorServiceException("Debe indicar la fecha de inicio");
            } 
            
            try {
                expensaActual = dao.buscarExpensaActual();
                if (expensaActual != null){
                System.out.println(expensaActual.getFechaDesde());
                    if (fechaDesde.compareTo(expensaActual.getFechaDesde()) <= 0){
                        throw new ErrorServiceException("La fecha de inicio debe ser mayor a la fecha de inicio de la expensa anterior.");
                    } else {
                        expensaActual.setFechaHasta(fechaDesde);
                        dao.guardarExpensa(expensaActual);
                    }      
                }
            } catch (NoResultDAOException ex) {}
            

                
            
            /* 
            El método compareTo() devuelve:
            0 si las dos fechas son iguales,
            Un valor negativo si la fecha que invoca el método es anterior,
            Un valor positivo si la fecha que invoca el método es posterior.
            */   
            if (importe <= 0.0){
                throw new ErrorServiceException("El importe de la expensa debe ser mayor a cero");
            }
            
          
            Expensa expensa = new Expensa();
            expensa.setId(UUID.randomUUID().toString()); // Genera un UUID único para el recibo
            expensa.setFechaDesde(fechaDesde);             
            expensa.setImporte(importe);         
            expensa.setEliminado(false);                 // Por defecto no eliminado
            dao.guardarExpensa(expensa);                 

        } catch (ErrorServiceException e) {
            throw e; // Re-lanzar la excepción específica de servicio
        } catch (Exception ex) {
            ex.printStackTrace(); // Imprimir el stack trace para depuración
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    public Expensa buscarExpensa(String id) throws ErrorServiceException {
        try {
            if (id == null) {
                throw new ErrorServiceException("Debe indicar la expensa");
            }

            Expensa expensa = dao.buscarExpensa(id);
            
            if (expensa.getEliminado()){
                throw new ErrorServiceException("No se encuentra la expensa indicada");
            }
            
            return expensa;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
        /**
     * Modifica los atributos del objeto
     * @param idExpensa String con el id
     * @param fechaDesde Date con la fecha Desde
     * @param fechaHasta Date con la fecha hasta
     * @param importe double con el importe
     * @throws ErrorServiceException 
     */
    public void modificarExpensa(String idExpensa, Date fechaDesde, Date fechaHasta, double importe) throws ErrorServiceException {

        try {

            Expensa expensa = buscarExpensa(idExpensa);

            if (fechaDesde == null) {
                throw new ErrorServiceException("Debe indicar la fecha de inicio");
            }
            
            if (fechaHasta == null) {
                throw new ErrorServiceException("Debe indicar la fecha de fin");
            }
            
            if (idExpensa== null || idExpensa.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }


            if (importe <= 0.0){
                throw new ErrorServiceException("El importe de la expensa debe ser mayor a cero");
            }

            /*
            El método compareTo() devuelve:

            0 si las dos fechas son iguales,
            Un valor negativo si la fecha que invoca el método es anterior,
            Un valor positivo si la fecha que invoca el método es posterior.
            */
            /*
            if (fechaHasta.compareTo(expensaExistente.getFechaDesde()) < 0) {
                // La fechaHasta es anterior o igual a la fecha de la expensa existente
                throw new ErrorServiceException("La expensa posee una fecha de incio mayor a la fecha de fin indicada");
            }
            
            if (fechaDesde.compareTo(expensaExistente.getFechaHasta()) > 0){
                throw new ErrorServiceException("La expensa posee una fecha de fin menor a la fecha de comienzo indicada");
            }
            */
            expensa.setFechaDesde(fechaDesde);
            expensa.setFechaHasta(fechaHasta);
            expensa.setImporte(importe);
            
            dao.actualizarExpensa(expensa);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
        /**
     * Elimina el objeto
     * @param id String que representa el id
     * @throws ErrorServiceException 
     */
    public void eliminarExpensa(String id) throws ErrorServiceException {

        try {

            Expensa expensa = buscarExpensa(id);
            expensa.setEliminado(true);
            
            dao.actualizarExpensa(expensa);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Devuelve una lista con los objetos de la clase activos
     * @return Collection<Expensa>
     * @throws ErrorServiceException 
     */
    public Collection<Expensa> listarExpensaActivo() throws ErrorServiceException {
        try {
            return dao.listarExpensaActivo();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Expensa buscarExpensaActual() throws ErrorServiceException  {
        try {  
            return dao.buscarExpensaActual();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        } 
    }
    
    public Expensa buscarExpensa(Date fecha) throws ErrorServiceException  {
        try {  
            return dao.buscarExpensaPorFecha(fecha);
        } catch (NoResultDAOException ex) {
            throw new ErrorServiceException("No se ha encontrado una expensa para el periodo indicado");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        } 
    }
}
