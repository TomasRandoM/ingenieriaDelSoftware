/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Menu;
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
 * @author Mauro Sorbello
 */
@Stateless
@LocalBean
public class DAOMenuBean {
    @PersistenceContext private EntityManager em;
    /**
     * Persiste el objeto en base de datos
     * @param menu Menu
     */
    public void guardarMenu(Menu menu) {
        em.persist(menu);
    }
    
     /**
     * Actualiza el objeto actual en base de datos
     * @param Menu menu
     */
    public void actualizarMenu(Menu menu) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(menu);
        em.flush();
    } 
    
    public Menu buscarMenu(String id) throws NoResultException {
        return em.find(Menu.class, id);
    }
        
    public Menu buscarMenuPorNombre(String nombre) throws ErrorDAOException, NoResultDAOException {
        try {
            if (nombre.length() > 255) {
                throw new ErrorDAOException("La longitud del nombre es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Menu)  em.createQuery("SELECT M "
                                          + " FROM Menu m"
                                          + " WHERE m.nombre = :nombre"
                                          + " AND m.eliminado = FALSE").
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
    
    public Menu buscarMenuPorOrden(int orden) throws ErrorDAOException, NoResultException, NoResultDAOException{
        try {
            return (Menu)  em.createQuery("SELECT m "
                                          + " FROM Menu m"
                                          + " WHERE m.eliminado = FALSE"
                                          + " AND m.orden = :orden").
                                          setParameter("orden", orden).
                                          getSingleResult();
        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró la información");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
    
    public Menu buscarMenuPorSubMenu(String idSubMenu) throws ErrorDAOException, NoResultException, NoResultDAOException{
        try {
            return (Menu)  em.createQuery("SELECT m "
                                          + " FROM Menu m, IN (m.submenu) sub"
                                          + " WHERE sub.id = :idSubMenu").
                                          setParameter("idSubMenu", idSubMenu).
                                          getSingleResult();
        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró la información");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
    
    public Collection<Menu> listarMenuActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT m "
                                    + " FROM Menu m"
                                    + " WHERE m.eliminado = FALSE"
                                    + " ORDER BY m.orden DESC").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }  
}
