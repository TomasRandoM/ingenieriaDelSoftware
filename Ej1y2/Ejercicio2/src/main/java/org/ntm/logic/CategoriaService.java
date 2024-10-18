package org.ntm.logic;

import org.ntm.domain.entity.Articulo;
import org.ntm.domain.entity.Categoria;
import org.ntm.domain.entity.Domicilio;
import org.ntm.persistence.EntityManagerGeneric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class CategoriaService {

    private final EntityManagerGeneric em;

    public CategoriaService() {
        em = EntityManagerGeneric.getInstancia();
    }

    /**
     * Crea el objeto y lo guarda en la base de datos
     * @param denominacion String
     * @throws ErrorServiceException
     */
    public Categoria crearCategoria(String denominacion) throws ErrorServiceException {
        try {

            if (denominacion == null || denominacion.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la denominacion");
            }

            Categoria categoria = new Categoria();
            categoria.setId(UUID.randomUUID().toString());
            categoria.setDenominacion(denominacion);
            categoria.setEliminado(false);
            categoria.setArticulos(new ArrayList<Articulo>());
            em.guardar(categoria);
            return categoria;
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

    /**
     * Agrega artículos a la categoría especificada
     * @param idArticulo String
     * @param idCategoria String
     * @throws ErrorServiceException
     */
    public void agregarArticulo(String idArticulo, String idCategoria) throws ErrorServiceException {
        try {
            if (idArticulo == null || idArticulo.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id del artículo");
            }
            if (idCategoria == null || idCategoria.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id del categoria");
            }

            Categoria categoria = buscarCategoria(idCategoria);
            ArticuloService articuloService = new ArticuloService();

            Articulo articulo = articuloService.buscarArticulo(idArticulo);

            Collection<Articulo> col = categoria.getArticulos();
            col.add(articulo);
            categoria.setArticulos(col);
            em.actualizar(categoria);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

}