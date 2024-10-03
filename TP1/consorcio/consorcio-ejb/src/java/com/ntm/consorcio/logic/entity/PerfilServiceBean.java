package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Menu;
import com.ntm.consorcio.domain.entity.Perfil;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOPerfilBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * Clase que implementa los métodos de perfil
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class PerfilServiceBean {
    private @EJB DAOPerfilBean dao;
    
    /**
     * Crea el perfil con los atributos indicados
     * @param nombre String
     * @param detalle String
     * @param menu Collection
     * @throws ErrorServiceException 
     */
    public Perfil crearPerfil(String nombre, String detalle, Collection<Menu> menu) throws ErrorServiceException {
        try {
            if (!verificar(nombre)) {
                throw new ErrorServiceException("Debe ingresar un nombre");
            }
            
            if (!verificar(detalle)) {
                throw new ErrorServiceException("Debe ingresar un detalle");
            }
            
            try {
                Perfil perfil = dao.buscarPerfilPorNombre(nombre);
                throw new ErrorServiceException("Ya existe un perfil con ese nombre");
            } catch (NoResultDAOException ex) { 
            }
            
            Perfil perfil = new Perfil();
            perfil.setEliminado(false);
            perfil.setId(UUID.randomUUID().toString());
            perfil.setNombre(nombre);
            perfil.setDetalle(detalle);
            perfil.setMenu(menu);
            dao.guardarPerfil(perfil);
            return perfil;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }   
    }
    
    /**
     * Busca el perfil con el id indicado
     * @param idPerfil String
     * @return Perfil
     * @throws ErrorServiceException 
     */
    public Perfil buscarPerfil(String idPerfil) throws ErrorServiceException {
        try {
            if (!verificar(idPerfil)) {
                throw new ErrorServiceException("Debe indicar un perfil");
            }
            
            Perfil perfil = dao.buscarPerfil(idPerfil);
            
            if (perfil.isEliminado() == true) {
                throw new ErrorServiceException("El perfil indicado no se encuentra");
            }
            
            return perfil;
            
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }         
    }
    
    /**
     * Modifica el perfil con el id indicado
     * @param idPerfil String
     * @param nombre String
     * @param detalle String
     * @throws ErrorServiceException 
     */
    public void modificarPerfil(String idPerfil, String nombre, String detalle) throws ErrorServiceException {
        try {
            Perfil perfil = buscarPerfil(idPerfil);
            
            if (!verificar(nombre)) {
                throw new ErrorServiceException("Debe ingresar un nombre");
            }
            
            if (!verificar(detalle)) {
                throw new ErrorServiceException("Debe ingresar un detalle");
            }
            
            try {
                Perfil perfilExist = dao.buscarPerfilPorNombre(nombre);
                if (!perfilExist.getId().equals(idPerfil)) {
                    throw new ErrorServiceException("Ya existe un perfil con ese nombre");
                }
            } catch (NoResultDAOException ex) { 
            }
            
            perfil.setNombre(nombre);
            perfil.setDetalle(detalle);
            dao.actualizarPerfil(perfil);
            
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }   
    }
    
    /**
     * Elimina el perfil con el id indicado
     * @param idPerfil String
     * @throws ErrorServiceException 
     */
    public void eliminarPerfil(String idPerfil) throws ErrorServiceException {
      try {
          Perfil perfil = buscarPerfil(idPerfil);
          perfil.setEliminado(true);
          
          dao.actualizarPerfil(perfil);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }   
    }
    
    /**
     * Devuelve una lista con los objetos de la clase activos
     * @return Collection
     * @throws ErrorServiceException 
     */
    public Collection<Perfil> listarPerfilActivo() throws ErrorServiceException {
        try {
            
            return dao.listarPerfilActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Verifica que la String no sea vacía o nula.
     * @param st String
     * @return boolean
     */
    public boolean verificar(String st) {
        if (st.isEmpty() || st == null) {
            return false;
        }
        return true;
    }
}
