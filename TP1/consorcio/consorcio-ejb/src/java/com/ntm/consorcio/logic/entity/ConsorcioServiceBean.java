package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Consorcio;
import com.ntm.consorcio.domain.entity.Direccion;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOConsorcioBean;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Clase que implementa los métodos correspondientes a la lógica de negocio de Consorcio
 */
@Stateless
@LocalBean
public class ConsorcioServiceBean implements Serializable {

    private @EJB DAOConsorcioBean dao;
    private @EJB DireccionServiceBean direccionService;

    /**
     * Crea un nuevo Consorcio
     * @param nombre String con el nombre del consorcio
     * @param idDireccion String con el ID de la dirección
     * @throws ErrorServiceException 
     */
    public void crearConsorcio(String nombre, String idDireccion) throws ErrorServiceException {
        Direccion direccion;
        try {

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                dao.buscarConsorcioPorNombre(nombre);
                throw new ErrorServiceException("Existe un consorcio con el nombre indicado");
            } catch (NoResultDAOException ex) {
                // Si no encuentra el consorcio, continúa con la creación
            }

            try {
                direccion = direccionService.buscarDireccion(idDireccion);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró la dirección seleccionada");
            }

            Consorcio consorcio = new Consorcio();
            consorcio.setId(UUID.randomUUID().toString());
            consorcio.setNombre(nombre);
            consorcio.setEliminado(false);
            consorcio.setDireccion(direccion);

            dao.guardarConsorcio(consorcio);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    /**
     * Modifica un consorcio existente
     * @param idConsorcio String con el ID del consorcio
     * @param nombre String con el nuevo nombre
     * @param idDireccion String con el ID de la nueva dirección
     * @throws ErrorServiceException 
     */
    public void modificarConsorcio(String idConsorcio, String nombre, String idDireccion) throws ErrorServiceException {
        Direccion direccion;
        try {

            Consorcio consorcio = buscarConsorcio(idConsorcio);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                Consorcio consorcioExistente = dao.buscarConsorcioPorNombre(nombre);
                if (!consorcioExistente.getId().equals(idConsorcio)) {
                    throw new ErrorServiceException("Existe un consorcio con el nombre indicado");
                }
            } catch (NoResultDAOException ex) {
                // No hay conflicto de nombres, continúa
            }

            try {
                direccion = direccionService.buscarDireccion(idDireccion);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró la dirección seleccionada");
            }

            consorcio.setNombre(nombre);
            consorcio.setDireccion(direccion);
            dao.actualizarConsorcio(consorcio);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    /**
     * Busca un consorcio por su ID
     * @param id String con el ID del consorcio
     * @return Objeto Consorcio
     * @throws ErrorServiceException 
     */
    public Consorcio buscarConsorcio(String id) throws ErrorServiceException {
        try {

            if (id == null) {
                throw new ErrorServiceException("Debe indicar el consorcio");
            }

            Consorcio consorcio = dao.buscarConsorcio(id);

            if (consorcio.getEliminado()) {
                throw new ErrorServiceException("No se encuentra el consorcio indicado");
            }

            return consorcio;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    /**
     * Elimina un consorcio
     * @param id String que representa el ID del consorcio
     * @throws ErrorServiceException 
     */
    public void eliminarConsorcio(String id) throws ErrorServiceException {
        try {

            Consorcio consorcio = buscarConsorcio(id);
            consorcio.setEliminado(true);

            dao.actualizarConsorcio(consorcio);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    /**
     * Lista todos los consorcios activos
     * @return Collection<Consorcio>
     * @throws ErrorServiceException 
     */
    public Collection<Consorcio> listarConsorcioActivo() throws ErrorServiceException {
        try {
            return dao.listarConsorcioActivo();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
