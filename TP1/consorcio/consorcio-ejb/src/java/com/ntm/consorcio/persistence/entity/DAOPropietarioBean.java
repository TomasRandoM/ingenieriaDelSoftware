package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Propietario;
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
 * Acceso a datos de propietario
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class DAOPropietarioBean {
    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste el objeto en base de datos
     * @param propietario Propietario
     */
    public void guardarPropietario(Propietario propietario) {
        em.persist(propietario);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param propietario Propietario
     */
    public void actualizarPropietario(Propietario propietario) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(propietario);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Propietario
     * @throws NoResultException 
     */
    public Propietario buscarPropietario(String id) throws NoResultException {
        return em.find(Propietario.class, id);
    }
    
    /**
     * Busca el objeto con el documento especificado
     * @param nombre String
     * @param apellido String
     * @return Propietario
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Propietario buscarPropietarioPorNombre(String nombre, String apellido) throws ErrorDAOException, NoResultDAOException {
        try {
            if (nombre.length() > 255) {
                throw new ErrorDAOException("La longitud del nombre es incorrecta. Debe tener menos de 255 caracteres");
            }
            if (apellido.length() > 255) {
                throw new ErrorDAOException("La longitud del apellido es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Propietario)  em.createQuery("SELECT p "
                                          + " FROM Propietario p"
                                          + " WHERE p.nombre = :nombre "
                                          + " AND p.apellido = :apellido "
                                          + " AND p.eliminado = FALSE").
                                          setParameter("nombre", nombre).
                                          setParameter("apellido", apellido).
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
    public Collection<Propietario> listarPropietarioActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Propietario p"
                                    + " WHERE p.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }  
    
}
