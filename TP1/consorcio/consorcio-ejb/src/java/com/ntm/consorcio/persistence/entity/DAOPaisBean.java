package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Pais;
import com.ntm.consorcio.persistence.ErrorDAOException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import java.io.Serializable;
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
public class DAOPaisBean implements Serializable {
    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste el objeto en base de datos
     * @param pais Pais
     */
    public void guardarPais(Pais pais) {
        em.persist(pais);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param pais Pais
     */
    public void actualizarPais(Pais pais) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(pais);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Pais
     * @throws NoResultException 
     */
    public Pais buscarPais(String id) throws NoResultException {
        return em.find(Pais.class, id);
    }
    
    /**
     * Busca el objeto con el nombre especificado
     * @param nombre String
     * @return Pais
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Pais buscarPaisPorNombre(String nombre) throws ErrorDAOException, NoResultDAOException {
        try {
            if (nombre.length() > 255) {
                throw new ErrorDAOException("La longitud del nombre es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Pais)  em.createQuery("SELECT pa "
                                          + " FROM Pais pa"
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
     * @return Collection<Pais>
     * @throws ErrorDAOException 
     */
    public Collection<Pais> listarPaisActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Pais p"
                                    + " WHERE p.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }  
}
