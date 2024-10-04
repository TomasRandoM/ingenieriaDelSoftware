package org.ntm.logic;

import org.ntm.domain.entity.Articulo;
import org.ntm.domain.entity.Domicilio;
import org.ntm.persistence.EntityManagerGeneric;

import java.util.UUID;

public class ArticuloService {

    private final EntityManagerGeneric em;

    public ArticuloService() {
        em = EntityManagerGeneric.getInstancia();
    }

    /**
     * Crea el objeto y lo persiste en la base de datos
     * @param cantidad int
     * @param denominacion String
     * @param precio int
     * @throws ErrorServiceException
     */
    public Articulo crearArticulo(int cantidad, String denominacion, int precio) throws ErrorServiceException {
        try {

            if (cantidad <= 0) {
                throw new ErrorServiceException("Debe indicar la cantidad");
            }
            if (denominacion == null || denominacion.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la denominacion");
            }
            if (precio <= 0) {
                throw new ErrorServiceException("Debe indicar el precio");
            }

            Articulo articulo = new Articulo();
            articulo.setId(UUID.randomUUID().toString());
            articulo.setCantidad(cantidad);
            articulo.setDenominacion(denominacion);
            articulo.setPrecio(precio);
            articulo.setEliminado(false);

            em.guardar(articulo);
            return articulo;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Busca al objeto en la base de datos y lo devuelve
     * @param idArticulo String
     * @return Articulo
     * @throws ErrorServiceException
     */
    public Articulo buscarArticulo(String idArticulo) throws ErrorServiceException {
        try {
            if (idArticulo == null || idArticulo.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Articulo articulo = em.buscar(Articulo.class, idArticulo);

            if (articulo.isEliminado()) {
                throw new ErrorServiceException("El articulo no existe");
            }

            return articulo;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Elimina el objeto de la base de datos
     * @param idArticulo String
     * @throws ErrorServiceException
     */
    public void eliminarArticulo(String idArticulo) throws ErrorServiceException {
        try {
            if (idArticulo == null || idArticulo.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Articulo articulo = buscarArticulo(idArticulo);
            articulo.setEliminado(true);
            em.actualizar(articulo);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}