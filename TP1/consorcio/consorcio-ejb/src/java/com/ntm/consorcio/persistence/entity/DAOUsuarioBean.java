package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Usuario;
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
 * Acceso a datos de Usuario
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class DAOUsuarioBean {
    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste el objeto en base de datos
     * @param usuario Usuario
     */
    public void guardarUsuario(Usuario usuario) {
        em.persist(usuario);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param usuario Usuario
     */
    public void actualizarUsuario(Usuario usuario) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(usuario);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Usuario
     * @throws NoResultException 
     */
    public Usuario buscarUsuario(String id) throws NoResultException {
        return em.find(Usuario.class, id);
    }
    
    /**
     * Busca el objeto con el documento especificado
     * @param nombre String
     * @param apellido String
     * @return Usuario
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Usuario buscarUsuarioPorUsuario(String usuario) throws ErrorDAOException, NoResultDAOException {
        try {
            if (usuario.length() > 255) {
                throw new ErrorDAOException("La longitud del usuario es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Usuario)  em.createQuery("SELECT p "
                                          + " FROM Usuario p"
                                          + " WHERE p.usuario = :usuario "
                                          + " AND p.eliminado = FALSE").
                                          setParameter("usuario", usuario).
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
     * Busca al usuario con el nombre y clave especificados
     * @param usuario String
     * @param clave String
     * @return Usuario
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Usuario buscarUsuarioPorUsuarioClave(String usuario, String clave) throws ErrorDAOException, NoResultDAOException {
        try {
            if (usuario.length() > 255) {
                throw new ErrorDAOException("La longitud del usuario es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            if (clave.length() > 255) {
                throw new ErrorDAOException("La longitud de la clave es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Usuario)  em.createQuery("SELECT p "
                                          + " FROM Usuario p"
                                          + " WHERE p.usuario = :usuario "
                                          + " AND p.clave = :clave "
                                          + " AND p.eliminado = FALSE").
                                          setParameter("usuario", usuario).
                                          setParameter("clave", clave).
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
    public Collection<Usuario> listarUsuarioActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Usuario p"
                                    + " WHERE p.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }  
    
}
