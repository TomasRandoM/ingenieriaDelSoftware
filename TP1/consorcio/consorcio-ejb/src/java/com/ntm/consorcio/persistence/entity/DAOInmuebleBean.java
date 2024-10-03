/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.persistence.entity;
import com.ntm.consorcio.domain.entity.Inmueble;
import com.ntm.consorcio.persistence.ErrorDAOException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collection;
/**
 *
 * @author Martinotebook
 */
@Stateless
@LocalBean
public class DAOInmuebleBean {
    
    @PersistenceContext
    private EntityManager em;

    /**
     * Persiste un objeto Inmueble en la base de datos.
     * @param inmueble Inmueble a guardar.
     */
    public void guardarInmueble(Inmueble inmueble) {
        em.persist(inmueble);
    }

    /**
     * Actualiza un objeto Inmueble en la base de datos.
     * @param inmueble Inmueble a actualizar.
     */
    public void actualizarInmueble(Inmueble inmueble) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(inmueble);
        em.flush();
    }

    /**
     * Busca un Inmueble por su ID.
     * @param id ID del inmueble.
     * @return Inmueble encontrado.
     * @throws NoResultDAOException Si no se encuentra el inmueble.
     */
    public Inmueble buscarInmueble(String id) throws NoResultException {
        return em.find(Inmueble.class, id);
    }

    /**
     * Busca un Inmueble por su piso y departamento.
     * @param piso Piso del inmueble.
     * @param dpto Departamento del inmueble.
     * @return Inmueble encontrado.
     * @throws NoResultDAOException Si no se encuentra el inmueble.
     */
    public Inmueble buscarInmueblePorPisoYDpto(String piso, String dpto) throws NoResultDAOException, ErrorDAOException {
        try {
            return (Inmueble) em.createQuery("SELECT inm "
                                              + " FROM Inmueble inm"
                                              + " WHERE inm.piso = :piso AND inm.dpto = :dpto"
                                              + " AND inm.eliminado = FALSE")
                                .setParameter("piso", piso)
                                .setParameter("dpto", dpto)
                                .getSingleResult();
        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró el inmueble con el piso y departamento especificados.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }

    /**
     * Lista todos los Inmuebles activos (no eliminados).
     * @return Colección de Inmuebles activos.
     * @throws ErrorDAOException En caso de error del sistema.
     */
    public Collection<Inmueble> listarInmueblesActivos() throws ErrorDAOException {
        try {
            return em.createQuery("SELECT inm "
                                    + " FROM Inmueble inm"
                                    + " WHERE inm.eliminado = FALSE")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error del sistema.");
        }
    }

}
