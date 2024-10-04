package org.ntm.logic;

import org.ntm.domain.entity.Cliente;
import org.ntm.domain.entity.Domicilio;
import org.ntm.persistence.EntityManagerGeneric;

import java.util.UUID;

public class ClienteService {

    private final EntityManagerGeneric em;

    public ClienteService() {
        em = EntityManagerGeneric.getInstancia();
    }

    /**
     * Crea el objeto y lo guarda en la base de datos
     * @param nombre String
     * @param apellido String
     * @param dni int
     * @param idDomicilio String
     * @throws ErrorServiceException
     */
    public Cliente crearCliente(String nombre, String apellido, int dni, String idDomicilio) throws ErrorServiceException {
        try {

            if (nombre.isEmpty() || nombre == null) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
            if (apellido.isEmpty() || apellido == null) {
                throw new ErrorServiceException("Debe indicar el apellido");
            }
            if (dni == 0) {
                throw new ErrorServiceException("Debe indicar el dni");
            }
            if (idDomicilio.isEmpty() || idDomicilio == null) {
                throw new ErrorServiceException("Debe indicar el id del domicilio");
            }

            DomicilioService dom = new DomicilioService();
            Domicilio domicilio = dom.buscarDomicilio(idDomicilio);

            Cliente cliente = new Cliente();
            cliente.setId(UUID.randomUUID().toString());
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDni(dni);
            cliente.setDomicilio(domicilio);

            em.guardar(cliente);
            return cliente;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Busca al objeto en la base de datos y lo devuelve
     * @param idCliente String
     * @return Cliente
     * @throws ErrorServiceException
     */
    public Cliente buscarCliente(String idCliente) throws ErrorServiceException {
        try {
            if (idCliente == null || idCliente.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Cliente cliente = em.buscar(Cliente.class, idCliente);

            if (cliente.isEliminado()) {
                throw new ErrorServiceException("El cliente no existe");
            }

            return cliente;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Elimina el objeto de la base de datos
     * @param idCliente String
     * @throws ErrorServiceException
     */
    public void eliminarCliente(String idCliente) throws ErrorServiceException {
        try {
            if (idCliente == null || idCliente.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Cliente cliente = buscarCliente(idCliente);
            cliente.setEliminado(true);
            em.actualizar(cliente);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}