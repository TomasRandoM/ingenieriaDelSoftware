package org.ntm.logic;

import org.ntm.domain.entity.Articulo;
import org.ntm.domain.entity.DetalleFactura;
import org.ntm.domain.entity.Factura;
import org.ntm.domain.entity.Cliente;
import org.ntm.persistence.EntityManagerGeneric;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class FacturaService {

    private final EntityManagerGeneric em;

    public FacturaService() {
        em = EntityManagerGeneric.getInstancia();
    }

    /**
     * Crea el objeto y lo guarda en la base de datos
     * @param fecha String
     * @param numero int
     * @param total int
     * @param idCliente String
     * @throws ErrorServiceException
     */
    public Factura crearFactura(String fecha, int numero, int total, String idCliente) throws ErrorServiceException {
        try {

            if (fecha == null || fecha.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la fecha");
            }
            if (numero <= 0) {
                throw new ErrorServiceException("Debe indicar el numero");
            }
            if (total <= 0) {
                throw new ErrorServiceException("Debe indicar el total");
            }
            if (idCliente == null || idCliente.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id del cliente");
            }

            ClienteService dom = new ClienteService();
            Cliente cliente = dom.buscarCliente(idCliente);

            Factura factura = new Factura();
            factura.setId(UUID.randomUUID().toString());
            factura.setFecha(fecha);
            factura.setNumero(numero);
            factura.setTotal(total);
            factura.setCliente(cliente);
            factura.setDetalleFacturas(new ArrayList<DetalleFactura>());
            factura.setEliminado(false);

            em.guardar(factura);
            return factura;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Busca al objeto en la base de datos y lo devuelve
     * @param idFactura String
     * @return Factura
     * @throws ErrorServiceException
     */
    public Factura buscarFactura(String idFactura) throws ErrorServiceException {
        try {
            if (idFactura == null || idFactura.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Factura factura = em.buscar(Factura.class, idFactura);

            if (factura.isEliminado()) {
                throw new ErrorServiceException("El factura no existe");
            }

            return factura;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Elimina el objeto de la base de datos
     * @param idFactura String
     * @throws ErrorServiceException
     */
    public void eliminarFactura(String idFactura) throws ErrorServiceException {
        try {
            if (idFactura == null || idFactura.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Factura factura = buscarFactura(idFactura);
            factura.setEliminado(true);
            em.actualizar(factura);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public void agregarDetalle(int cantidad, int subtotal, String idArticulo, String idFactura) throws ErrorServiceException {
        try {
            if (cantidad <= 0) {
                throw new ErrorServiceException("Debe indicar una cantidad");
            }
            if (subtotal <= 0) {
                throw new ErrorServiceException("Debe indicar una subtotal");
            }
            if (idArticulo == null || idArticulo.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id del articulo");
            }
            if (idFactura == null || idFactura.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id del factura");
            }

            ArticuloService art = new ArticuloService();
            Articulo articulo = art.buscarArticulo(idArticulo);

            Factura factura = buscarFactura(idFactura);
            DetalleFactura df = new DetalleFactura();
            df.setId(UUID.randomUUID().toString());
            df.setCantidad(cantidad);
            df.setSubtotal(subtotal);
            df.setArticulo(articulo);
            df.setEliminado(false);

            Collection<DetalleFactura> col = factura.getDetalleFacturas();
            col.add(df);
            //PENDIENTE: Agregar esta parte en un método aparte que modifique la factura
            factura.setDetalleFacturas(col);
            em.actualizar(factura);
            em.guardar(df);
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }

    }
}