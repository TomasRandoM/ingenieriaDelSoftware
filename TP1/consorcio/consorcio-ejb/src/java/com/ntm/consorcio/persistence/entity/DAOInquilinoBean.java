package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Inquilino;
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
 * Acceso a datos de inquilino
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class DAOInquilinoBean {
    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste el objeto en base de datos
     * @param inquilino Inquilino
     */
    public void guardarInquilino(Inquilino inquilino) {
        em.persist(inquilino);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param inquilino Inquilino
     */
    public void actualizarInquilino(Inquilino inquilino) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(inquilino);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return Inquilino
     * @throws NoResultException 
     */
    public Inquilino buscarInquilino(String id) throws NoResultException {
        return em.find(Inquilino.class, id);
    }
    
    /**
     * Busca el objeto con el documento especificado
     * @param documento String
     * @return Inquilino
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public Inquilino buscarInquilinoPorDocumento(String documento) throws ErrorDAOException, NoResultDAOException {
        try {
            if (documento.length() > 255) {
                throw new ErrorDAOException("La longitud del documento es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (Inquilino)  em.createQuery("SELECT p "
                                          + " FROM Inquilino p"
                                          + " WHERE p.documento = :documento"
                                          + " AND p.eliminado = FALSE").
                                          setParameter("documento", documento).
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
    public Collection<Inquilino> listarInquilinoActivo() throws ErrorDAOException {
        try {  
            return em.createQuery("SELECT p "
                                    + " FROM Inquilino p"
                                    + " WHERE p.eliminado = FALSE").
                                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }  
    
}
