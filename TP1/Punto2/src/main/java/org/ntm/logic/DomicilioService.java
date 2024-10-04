package org.ntm.logic;

import org.ntm.domain.entity.Domicilio;
import org.ntm.persistence.EntityManagerGeneric;

import java.util.UUID;

public class DomicilioService {

    private final EntityManagerGeneric em;

    public DomicilioService() {
        em = EntityManagerGeneric.getInstancia();
    }

    /**
     * Crea el objeto y lo guarda en la base de datos
     * @param nombreCalle String
     * @param numero int
     * @throws ErrorServiceException
     */
    public Domicilio crearDomicilio(String nombreCalle, int numero) throws ErrorServiceException {
        try {
            if (nombreCalle.isEmpty() || nombreCalle == null) {
                throw new ErrorServiceException("Debe indicar el nombre de la calle");
            }
            if (numero < 0) {
                throw new ErrorServiceException("Numeración incorrecta");
            }

            Domicilio domicilio = new Domicilio();
            domicilio.setId(UUID.randomUUID().toString());
            domicilio.setNumero(numero);
            domicilio.setEliminado(false);
            domicilio.setNombreCalle(nombreCalle);

            em.guardar(domicilio);
            return domicilio;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Busca al objeto en la base de datos y lo devuelve
     * @param idDomicilio String
     * @return Domicilio
     * @throws ErrorServiceException
     */
    public Domicilio buscarDomicilio(String idDomicilio) throws ErrorServiceException {
        try {
            if (idDomicilio == null || idDomicilio.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Domicilio domicilio = em.buscar(Domicilio.class, idDomicilio);

            if (domicilio.isEliminado()) {
                throw new ErrorServiceException("El domicilio no existe");
            }
            return domicilio;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Elimina el objeto de la base de datos
     * @param idDomicilio String
     * @throws ErrorServiceException
     */
    public void eliminarDomicilio(String idDomicilio) throws ErrorServiceException {
        try {
            if (idDomicilio == null || idDomicilio.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Domicilio domicilio = buscarDomicilio(idDomicilio);
            domicilio.setEliminado(true);
            em.actualizar(domicilio);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}