/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Consorcio;

import com.ntm.consorcio.persistence.ErrorDAOException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collection;

/**
 *
 * @author Martinotebook
 */
@Stateless
@LocalBean
public class DAOConsorcioBean {
    
    @PersistenceContext 
    private EntityManager em;

    private static final int MAX_NAME_LENGTH = 255;

    /**
     * Persiste el objeto Consorcio en la base de datos
     * @param consorcio Consorcio
     */
    public void guardarConsorcio(Consorcio consorcio) {
        em.persist(consorcio);
    }

    /**
     * Actualiza el objeto Consorcio en la base de datos
     * @param consorcio Consorcio
     */
    public void actualizarConsorcio(Consorcio consorcio) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(consorcio);
        em.flush();
    }

    /**
     * Busca un consorcio por su ID
     * @param id String con el ID del consorcio
     * @return Consorcio
     */
    public Consorcio buscarConsorcio(String id) throws NoResultException {
        return em.find(Consorcio.class, id);
    }

    /**
     * Busca un consorcio por su nombre
     * @param nombre String con el nombre del consorcio
     * @return Consorcio
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Consorcio buscarConsorcioPorNombre(String nombre) throws ErrorDAOException, NoResultDAOException {
        try {
            if (nombre.length() > MAX_NAME_LENGTH) {
                throw new ErrorDAOException("La longitud del nombre es incorrecta. Debe tener menos de " + MAX_NAME_LENGTH + " caracteres");
            }

            return em.createQuery("SELECT c FROM Consorcio c WHERE c.nombre = :nombre AND c.eliminado = FALSE", Consorcio.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró un consorcio con el nombre indicado");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
        }
    }

    /**
     * Lista los consorcios activos (no eliminados)
     * @return Colección de consorcios activos
     * @throws ErrorDAOException 
     */
    public Collection<Consorcio> listarConsorcioActivo() throws ErrorDAOException {
        try {
            return em.createQuery("SELECT c FROM Consorcio c WHERE c.eliminado = FALSE", Consorcio.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
        }
    }
}
