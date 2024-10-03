package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Localidad;
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
public class DAOLocalidadBean {
    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste el objeto en base de datos
     * @param localidad Localidad
     */
    public void guardarLocalidad(Localidad localidad) {
        em.persist(localidad);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param localidad Localidad
     */
    public void actualizarLocalidad(Localidad localidad) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(localidad);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Localidad
     * @throws NoResultException 
     */
    public Localidad buscarLocalidad(String id) throws NoResultException {
        return em.find(Localidad.class, id);
    }
    
    /**
     * Busca el objeto con el nombre especificado
     * @param nombre String
     * @return Localidad
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Localidad buscarLocalidadPorNombre(String nombre) throws ErrorDAOException, NoResultDAOException {
        try {
            if (nombre.length() > 255) {
                throw new ErrorDAOException("La longitud del nombre es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Localidad)  em.createQuery("SELECT pa "
                                          + " FROM Localidad pa"
                                          + " WHERE pa.nombre = :nombre"
                                          + " AND pa.eliminado = FALSE").
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
     * @return Collection<Localidad>
     * @throws ErrorDAOException 
     */
    public Collection<Localidad> listarLocalidadActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Localidad p"
                                    + " WHERE p.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
    
    /**
     * Busca la lista de objetos de la clase actualmente activos por departamento
     * @return Collection
     * @throws ErrorDAOException 
     */
    public Collection<Localidad> listarLocalidadActivoPorDepartamento(String id) throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Localidad p"
                                    + " WHERE p.eliminado = FALSE"
                                    + " AND p.departamento.id = :id").
                                    setParameter("id", id).
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
}