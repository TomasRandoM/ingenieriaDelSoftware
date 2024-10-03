package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Direccion;
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
 * Acceso a datos
 * @author Tomas Rando
 * @version 1.0.0
 */
@Stateless
@LocalBean
public class DAODireccionBean {
    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste el objeto en base de datos
     * @param direccion Direccion
     */
    public void guardarDireccion(Direccion direccion) {
        em.persist(direccion);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param direccion Direccion
     */
    public void actualizarDireccion(Direccion direccion) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(direccion);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Direccion
     * @throws NoResultException 
     */
    public Direccion buscarDireccion(String id) throws NoResultException {
        return em.find(Direccion.class, id);
    }
    
    /**
     * Busca el objeto con la numeración y calle especificados
     * @param numeracion String con la numeración
     * @param calle String con la calle
     * @return Direccion
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Direccion buscarDireccionPorNumeracionYCalle(String numeracion, String calle) throws ErrorDAOException, NoResultDAOException {
        try {
            if (numeracion.length() > 255) {
                throw new ErrorDAOException("La longitud de la numeración es incorrecta. Debe tener menos de 255 caracteres");
            }
            if (calle.length() > 255) {
                throw new ErrorDAOException("La longitud de la calle es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Direccion)  em.createQuery("SELECT pa "
                                          + " FROM Direccion pa"
                                          + " WHERE pa.numeracion = :numeracion"
                                          + " AND pa.calle = :calle"
                                          + " AND pa.eliminado = FALSE").
                                          setParameter("numeracion", numeracion).
                                          setParameter("calle", calle).
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
     * @return Collection<Direccion>
     * @throws ErrorDAOException 
     */
    public Collection<Direccion> listarDireccionActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Direccion p"
                                    + " WHERE p.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
    
    /**
     * Busca la lista de objetos de la clase actualmente activos con la localidad especificada
     * @param id String
     * @return Collection
     * @throws ErrorDAOException 
     */
    public Collection<Direccion> listarDireccionActivoPorLocalidad(String id) throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Direccion p"
                                    + " WHERE p.eliminado = FALSE"
                                    + " AND p.localidad.id = :id").
                                    setParameter("id", id).
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
}
