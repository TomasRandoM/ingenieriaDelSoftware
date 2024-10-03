/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Nacionalidad;
import com.ntm.consorcio.persistence.ErrorDAOException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Acceso a datos de Nacionalidad
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class DAONacionalidadBean {
    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste la nacionalidad en la base de datos
     * @param nacionalidad Nacionalidad
     */
    public void guardarNacionalidad(Nacionalidad nacionalidad) {
        em.persist(nacionalidad);
    }
    
    /**
     * Actualiza la nacionalidad en la base de datos
     * @param nacionalidad Nacionalidad
     */
    public void actualizarNacionalidad(Nacionalidad nacionalidad) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(nacionalidad);
        em.flush();
    }
    
    /**
     * Busca la nacionalidad con el id especificado
     * @param idNacionalidad String
     */
    public Nacionalidad buscarNacionalidad(String idNacionalidad) {
        return em.find(Nacionalidad.class, idNacionalidad);
    }
    
    /**
     * Busca la nacionalidad por nombre
     * @param nombre String
     * @return Nacionalidad
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Nacionalidad buscarNacionalidadPorNombre(String nombre) throws ErrorDAOException, NoResultDAOException {
        try {
            if (nombre.length() > 255) {
                throw new ErrorDAOException("La longitud del nombre es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Nacionalidad)  em.createQuery("SELECT na "
                                          + " FROM Nacionalidad na"
                                          + " WHERE na.nombre = :nombre"
                                          + " AND na.eliminado = FALSE").
                                          setParameter("nombre", nombre).
                                          getSingleResult();
        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró la información solicitada");
        } catch (ErrorDAOException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
    
    /**
     * Busca la lista de nacionalidades actualmente activas
     * @return Collection de Nacionalidad
     * @throws ErrorDAOException 
     */
    public Collection<Nacionalidad> listarNacionalidadActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT n "
                                    + " FROM Nacionalidad n"
                                    + " WHERE n.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
}
