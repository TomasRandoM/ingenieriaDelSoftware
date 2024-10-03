/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Perfil;
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
 * Acceso a datos de perfil
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class DAOPerfilBean {

    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste el objeto en base de datos
     * @param perfil Perfil
     */
    public void guardarPerfil(Perfil perfil) {
        em.persist(perfil);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param perfil Perfil
     */
    public void actualizarPerfil(Perfil perfil) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(perfil);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Perfil
     * @throws NoResultException 
     */
    public Perfil buscarPerfil(String id) throws NoResultException {
        return em.find(Perfil.class, id);
    }
    
    /**
     * Busca el objeto con el documento especificado
     * @param nombre String
     * @return Perfil
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Perfil buscarPerfilPorNombre(String nombre) throws ErrorDAOException, NoResultDAOException {
        try {
            if (nombre.length() > 255) {
                throw new ErrorDAOException("La longitud del nombre es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Perfil)  em.createQuery("SELECT p "
                                          + " FROM Perfil p"
                                          + " WHERE p.nombre = :nombre "
                                          + " AND p.eliminado = FALSE").
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
     * Busca la lista de objetos de la clase actualmente activos
     * @return Collection
     * @throws ErrorDAOException 
     */
    public Collection<Perfil> listarPerfilActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Perfil p"
                                    + " WHERE p.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
}
