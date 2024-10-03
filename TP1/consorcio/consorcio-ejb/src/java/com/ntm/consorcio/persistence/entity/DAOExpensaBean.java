/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Expensa;
import com.ntm.consorcio.persistence.ErrorDAOException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Mauro Sorbello
 */
@Stateless
@LocalBean
public class DAOExpensaBean {
        @PersistenceContext private EntityManager em;
/**
     * Persiste el objeto en base de datos
     * @param expensa Expensa
     */
    public void guardarExpensa(Expensa expensa) {
        em.persist(expensa);
    }
    
        /**
     * Actualiza el objeto actual en base de datos
     * @param expensa Expensa
     */
    public void actualizarExpensa(Expensa expensa) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(expensa);
        em.flush();
    }
    
        /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Expensa
     * @throws NoResultException 
     */
    
    //Preguntar profe si esta no es solo buscar expensa, en vez de buscar expensaInmueble
    public Expensa buscarExpensa(String id) throws NoResultException {
        return em.find(Expensa.class, id);
    }
    
    
    public Collection<Expensa> listarExpensaActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT e "
                                    + " FROM Expensa e"
                                    + " WHERE e.eliminado = FALSE"
                                    + " ORDER BY e.fechaDesde DESC").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }  
    public Expensa buscarExpensaActual() throws ErrorDAOException, NoResultDAOException {
        try {  
            Collection<Expensa> expensas = em.createQuery("SELECT e "
                                        + " FROM Expensa e"
                                        + " WHERE e.eliminado = FALSE", Expensa.class)
                                        .getResultList();
            // Si esperas solo una expensa actual, toma la primera
            if (!expensas.isEmpty()) {
                return expensas.iterator().next();
            } else {
                throw new NoResultException("No se encontró expensa");
            }
        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró expensa actual");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
    public Expensa buscarExpensaPorFecha(Date fecha) throws ErrorDAOException, NoResultDAOException  {
        try {  
            Collection<Expensa> expensas =  em.createQuery("SELECT e "
                                            + " FROM Expensa e"
                                            + " WHERE e.fechaDesde <= :fecha"
                                            + " AND e.eliminado = FALSE"
                                            + " AND (e.fechaHasta > :fecha OR e.fechaHasta IS NULL)").
                                            setParameter("fecha", fecha).
                                            getResultList();
            // Si esperas solo una expensa actual, toma la primera
            if (!expensas.isEmpty()) {
                return expensas.iterator().next();
            } else {
                throw new NoResultException("No se encontró la expensa");
            }
        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró expensa");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        } 
    }
    
}

