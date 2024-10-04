package org.ntm.logic;

import org.ntm.domain.entity.Categoria;
import org.ntm.domain.entity.Domicilio;
import org.ntm.persistence.EntityManagerGeneric;

import java.util.UUID;

public class CategoriaService {

    private final EntityManagerGeneric em;

    public CategoriaService() {
        em = EntityManagerGeneric.getInstancia();
    }

    /**
     * Crea el objeto y lo guarda en la base de datos
     * @param nombre String
     * 
     * @throws ErrorServiceException
     */
    public void crearCategoria(String denominacion) throws ErrorServiceException {
        try {

            if (denominacion == null || denominacion.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la denominacion");
            }

            Categoria categoria = new Categoria();
            categoria.setId(UUID.randomUUID().toString());
            categoria.setDenominacion(denominacion);

            em.guardar(categoria);
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Busca al objeto en la base de datos y lo devuelve
     * @param idCategoria String
     * @return Categoria
     * @throws ErrorServiceException
     */
    public Categoria buscarCategoria(String idCategoria) throws ErrorServiceException {
        try {
            if (idCategoria == null || idCategoria.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Categoria categoria = em.buscar(Categoria.class, idCategoria);

            if (categoria.isEliminado()) {
                throw new ErrorServiceException("El categoria no existe");
            }

            return categoria;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Elimina el objeto de la base de datos
     * @param idCategoria String
     * @throws ErrorServiceException
     */
    public void eliminarCategoria(String idCategoria) throws ErrorServiceException {
        try {
            if (idCategoria == null || idCategoria.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Categoria categoria = buscarCategoria(idCategoria);
            categoria.setEliminado(true);
            em.actualizar(categoria);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}