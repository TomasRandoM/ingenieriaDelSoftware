/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Submenu;
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
public class DAOSubMenuBean {
    @PersistenceContext private EntityManager em;
    /**
     * Persiste el objeto en base de datos
     * @param subMenu SubMenu
     */
    public void guardarSubMenu(Submenu subMenu) {
        em.persist(subMenu);
    }
    
     /**
     * Actualiza el objeto actual en base de datos
     * @param subMenu SubMenu
     */
    public void actualizarSubMenu(Submenu subMenu) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(subMenu);
        em.flush();
    } 
    
    public Submenu buscarSubMenu(String id) throws NoResultException {
        return em.find(Submenu.class, id);
    }
    
    public Submenu buscarSubMenuPorMenuYOrden(String idMenu,int orden) throws ErrorDAOException, NoResultException, NoResultDAOException{
        try {
            if (idMenu.length() > 255) {
                throw new ErrorDAOException("La longitud del id es incorrecta. Debe tener menos de 255 caracteres");
            }
            if (orden <= 0){
                throw new ErrorDAOException("Debe indicar el orden.");
            }
            return (Submenu)  em.createQuery("SELECT m "
                                          + " FROM Menu m"
                                          + " JOIN m.submenu sm"
                                          + " WHERE m.id = :idMenu"
                                          + " AND sm.orden = :orden").
                                          setParameter("idMenu", idMenu).
                                          setParameter("orden", orden).
                                          getSingleResult();
        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró la información");
        } catch (ErrorDAOException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
            
    public Submenu buscarSubMenuPorNombre(String nombre) throws ErrorDAOException, NoResultDAOException {
        try {
            if (nombre.length() > 255) {
                throw new ErrorDAOException("La longitud del nombre es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Submenu)  em.createQuery("SELECT sm "
                                          + " FROM Submenu sm"
                                          + " WHERE sm.nombre = :nombre"
                                          + " AND sm.eliminado = FALSE").
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
    
    public Collection<Submenu> listarSubMenuActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT sm "
                                    + " FROM Submenu sm"
                                    + " WHERE sm.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }  
        public Collection<Submenu> listarSubMenuPorMenu(String idMenu) throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT sub "
                                    + " FROM Menu m, IN (m.submenu) sub "
                                    + " WHERE m.id = :idMenu "
                                    + " ORDER BY sub.orden DESC").
                                    setParameter("idMenu", idMenu).
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }  
}
