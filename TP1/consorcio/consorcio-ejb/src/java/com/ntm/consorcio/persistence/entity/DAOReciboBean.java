/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Recibo;
import com.ntm.consorcio.persistence.ErrorDAOException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Mauro Sorbello
 */
@Stateless
@LocalBean
public class DAOReciboBean {
    @PersistenceContext private EntityManager em;
/**
     * Persiste el objeto en base de datos
     * @param recibo Recibo
     */
    public void guardarRecibo(Recibo recibo) {
        em.persist(recibo);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param recibo Recibo
     */
    public void actualizarRecibo(Recibo recibo) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(recibo);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Recibo
     * @throws NoResultException 
     */
    public Recibo buscarRecibo(String id) throws NoResultException {
        return em.find(Recibo.class, id);
    }
    
        /**
     * Busca la lista de objetos de la clase actualmente activos
     * @return Collection<Recibo>
     * @throws ErrorDAOException 
     */
    public Collection<Recibo> listarReciboActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT r "
                                    + " FROM Recibo r"
                                    + " WHERE r.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    } 
  
    /**
     * Busca la lista de objetos de recibo por habitante
     * @param idHabitante String 
     * @return Recibo
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    
    public Collection<Recibo> listarReciboPorHabitante(String idHabitante) throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT r "
                                    + " FROM Recibo r"
                                    + " JOIN r.detallesRecibo d"
                                    + " JOIN d.expensaInmueble ei"
                                    + " JOIN ei.inmueble i"
                                    + " JOIN i.propietario p"
                                    + " JOIN i.inquilino in"
                                    + " WHERE p.id = :idHabitante "
                                    + " OR in.id = :idHabitante ").
                                    setParameter("idHabitante", idHabitante).
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
    
    //Consultar con el profe
    public void agregarDetalle() {
        
    }
}
