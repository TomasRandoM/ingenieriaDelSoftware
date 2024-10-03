package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.CuentaCorreo;
import com.ntm.consorcio.persistence.ErrorDAOException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Acceso a datos de cuentaCorreo
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class DAOCuentaCorreoBean {
    @PersistenceContext private EntityManager em;
    
    /**
     * Persiste el objeto en base de datos
     * @param cuentaCorreo CuentaCorreo
     */
    public void guardarCuentaCorreo(CuentaCorreo cuentaCorreo) {
        em.persist(cuentaCorreo);
    }
    
    /**
     * Actualiza el objeto actual en base de datos
     * @param cuentaCorreo CuentaCorreo
     */
    public void actualizarCuentaCorreo(CuentaCorreo cuentaCorreo) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(cuentaCorreo);
        em.flush();
    }
    
    /**
     * Busca el objeto con el id ingresado
     * @param id String
     * @return CuentaCorreo
     * @throws NoResultException 
     */
    public CuentaCorreo buscarCuentaCorreo(String id) throws NoResultException {
        return em.find(CuentaCorreo.class, id);
    }
    
    /**
     * Busca el objeto con el documento especificado
     * @param correo String
     * @return CuentaCorreo
     * @throws ErrorDAOException
     * @throws NoResultDAOException 
     */
    public CuentaCorreo buscarCuentaCorreoPorCorreo(String correo) throws ErrorDAOException, NoResultDAOException {
        try {
            if (correo.length() > 255) {
                throw new ErrorDAOException("La longitud del correo es incorrecta. Debe tener menos de 255 caracteres");
            }
            
            return (CuentaCorreo)  em.createQuery("SELECT p "
                                          + " FROM CuentaCorreo p"
                                          + " WHERE p.correo = :correo "
                                          + " AND p.eliminado = FALSE").
                                          setParameter("correo", correo).
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
    
    public CuentaCorreo buscarCuentaCorreoActiva() throws ErrorDAOException, NoResultDAOException {
        try {    
            return (CuentaCorreo)  em.createQuery("SELECT p "
                                          + " FROM CuentaCorreo p"
                                          + " WHERE p.eliminado = FALSE").
                                          getSingleResult();
        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró la información solicitada");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    } 
    
}
