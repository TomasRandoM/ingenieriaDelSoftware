package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Departamento;
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
public class DAODepartamentoBean {
    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste el objeto en base de datos
     * @param departamento Departamento
     */
    public void guardarDepartamento(Departamento departamento) {
        em.persist(departamento);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param departamento Departamento
     */
    public void actualizarDepartamento(Departamento departamento) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(departamento);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Departamento
     * @throws NoResultException 
     */
    public Departamento buscarDepartamento(String id) throws NoResultException {
        return em.find(Departamento.class, id);
    }
    
    /**
     * Busca el objeto con el nombre especificado
     * @param nombre String
     * @return Departamento
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Departamento buscarDepartamentoPorNombre(String nombre) throws ErrorDAOException, NoResultDAOException {
        try {
            if (nombre.length() > 255) {
                throw new ErrorDAOException("La longitud del nombre es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Departamento)  em.createQuery("SELECT pa "
                                          + " FROM Departamento pa"
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
     * @return Collection<Departamento>
     * @throws ErrorDAOException 
     */
    public Collection<Departamento> listarDepartamentoActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Departamento p"
                                    + " WHERE p.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }
    
    /**
     * Busca la lista de objetos de la clase actualmente activos por provincia
     * @return Collection<Departamento>
     * @throws ErrorDAOException 
     */
    public Collection<Departamento> listarDepartamentoActivoPorProvincia(String id) throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Departamento p"
                                    + " WHERE p.eliminado = FALSE"
                                    + " AND p.provincia.id = :id").
                                    setParameter("id", id).
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }  
}
