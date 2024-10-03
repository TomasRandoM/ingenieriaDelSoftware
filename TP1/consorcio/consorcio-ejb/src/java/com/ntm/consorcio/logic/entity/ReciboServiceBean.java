
package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.DetalleRecibo;
import com.ntm.consorcio.domain.entity.ExpensaInmueble;
import com.ntm.consorcio.domain.entity.FormaDePago;
import com.ntm.consorcio.domain.entity.Recibo;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOReciboBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Mauro Sorbello
 */
@Stateless
@LocalBean
public class ReciboServiceBean {
    private @EJB DAOReciboBean dao;
    private @EJB ExpensaInmuebleServiceBean serviceExpensaInmueble;
    private @EJB PDFServiceBean servicePDF;
    private @EJB InmuebleServiceBean inmuebleService;
    private @EJB CuentaCorreoServiceBean correoService;
      /**
     * Crea un objeto de la clase
     * @param fechaPago Date
     * @param formaDePago FormaDePago
     * @throws ErrorServiceException 
     */
    public void crearRecibo(Date fechaPago, FormaDePago formaDePago) throws ErrorServiceException {
        try {
            // Verificamos si la fecha de pago es null
            if (fechaPago == null) {
                throw new ErrorServiceException("Debe indicar fecha de pago");
            }
            Recibo recibo = new Recibo();
            recibo.setId(UUID.randomUUID().toString()); // Genera un UUID único para el recibo
            recibo.setFechaPago(fechaPago);             // Asigna la fecha de pago
            recibo.setFormaDePago(formaDePago);         // Asigna la forma de pago
            recibo.setEliminado(false);                 // Por defecto no eliminado

            dao.guardarRecibo(recibo);                  // Guardar el recibo

        } catch (ErrorServiceException e) {
            throw e; // Re-lanzar la excepción específica de servicio
        } catch (Exception ex) {
            ex.printStackTrace(); // Imprimir el stack trace para depuración
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
     /**
     * Modifica un objeto de la clase
     * @param fechaPago Date
     * @param formaDePago FormaDePago
     * @param idRecibo String
     * @throws ErrorServiceException 
     */
    public void modificarRecibo(String idRecibo, Date fechaPago, FormaDePago formaDePago) throws ErrorServiceException {
        try {
            // Verificamos si la fecha de pago es null
            if (fechaPago == null) {
                throw new ErrorServiceException("Debe indicar fecha de pago");
            }
            
            if (idRecibo == null || idRecibo.isEmpty()) {
                throw new ErrorServiceException("Debe indicar un recibo");
            }
            
        
            Recibo recibo = buscarRecibo(idRecibo);
                
            recibo.setFechaPago(fechaPago);             // Asigna la fecha de pago
            recibo.setFormaDePago(formaDePago);         // Asigna la forma de pago

            dao.guardarRecibo(recibo);                  // Guardar el recibo

        } catch (ErrorServiceException e) {
            throw e; // Re-lanzar la excepción específica de servicio
        } catch (Exception ex) {
            ex.printStackTrace(); // Imprimir el stack trace para depuración
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Crea un detalle de recibo
     * @param idRecibo String
     * @param cantidad int
     * @param subtotal double
     * @param idExpensaInmueble String
     * @throws ErrorServiceException 
     */
    public void crearDetalleRecibo(String idRecibo, int cantidad, String idExpensaInmueble) throws ErrorServiceException {
        try {
            ExpensaInmueble expensaInmueble;

            if (cantidad <= 0) {
                throw new ErrorServiceException("Debe indicar la cantidad");
            }
            
            
            if (idExpensaInmueble == null || idExpensaInmueble.isEmpty()) {
                throw new ErrorServiceException("Debe indicar un ExpensaInmueble");
            }
            
            try {
                expensaInmueble = serviceExpensaInmueble.buscarExpensaInmueble(idExpensaInmueble);
                
            } catch (Exception ex) {
                
                throw new ErrorServiceException("No existe la expensa inmueble indicada");
            }
            
            Recibo recibo = buscarRecibo(idRecibo);
            double subt = expensaInmueble.getExpensa().getImporte();
            DetalleRecibo detalleRecibo = new DetalleRecibo();
            detalleRecibo.setId(UUID.randomUUID().toString()); // Genera un UUID único para el recibo
            detalleRecibo.setCantidad(cantidad);
            detalleRecibo.setExpensaInmueble(expensaInmueble);
            detalleRecibo.setSubtotal(subt);
            detalleRecibo.setEliminado(false);                 // Por defecto no eliminado
            
            Collection<DetalleRecibo> collection;
            
            if (recibo.getDetalleRecibo() == null) {
                collection = new ArrayList<>();
            } else {
                collection = recibo.getDetalleRecibo();
            }
            collection.add(detalleRecibo);
            recibo.setDetalleRecibo(collection);
            recibo.setTotal(calcularTotal(collection));
            dao.actualizarRecibo(recibo);                  

        } catch (ErrorServiceException e) {
            throw e; // Re-lanzar la excepción específica de servicio
        } catch (Exception ex) {
            ex.printStackTrace(); // Imprimir el stack trace para depuración
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica el Detalle Recibo
     * @param idRecibo String
     * @param idDetalleRecibo String
     * @param cantidad int
     * @param subtotal double
     * @param idExpensaInmueble String
     * @throws ErrorServiceException 
     */
    public void modificarDetalleRecibo(String idRecibo, String idDetalleRecibo, int cantidad, String idExpensaInmueble) throws ErrorServiceException {
        try {
            ExpensaInmueble expensaInmueble;
            Recibo recibo = buscarRecibo(idRecibo);
            
            if (cantidad <= 0) {
                throw new ErrorServiceException("Debe indicar la cantidad");
            }
            
            if (idExpensaInmueble == null || idExpensaInmueble.isEmpty()) {
                throw new ErrorServiceException("Debe indicar un ExpensaInmueble");
            }
            
            if (idDetalleRecibo == null || idDetalleRecibo.isEmpty()) {
                throw new ErrorServiceException("Debe indicar un DetalleRecibo");
            }
            
            try {
                expensaInmueble = serviceExpensaInmueble.buscarExpensaInmueble(idExpensaInmueble);
            } catch (Exception ex) {
                throw new ErrorServiceException("No existe la expensa inmueble indicada");
            }
            double subtotal = expensaInmueble.getExpensa().getImporte();
            Collection<DetalleRecibo> collection;
            
            if (recibo.getDetalleRecibo() == null) {
                throw new ErrorServiceException("No existen detalles de recibo en el recibo indicado");
            } else {
                collection = recibo.getDetalleRecibo();
            }
            
            DetalleRecibo elem = null;
            for (DetalleRecibo dr : collection) {
                if (dr.getId().equals(idDetalleRecibo)) {
                    elem = dr;
                }
            }
            
            if (elem == null) {
                throw new ErrorServiceException("El recibo no posee el detalle indicado");
            }
            
            collection.remove(elem);
            
            elem.setCantidad(cantidad);
            elem.setExpensaInmueble(expensaInmueble);
            elem.setSubtotal(subtotal);
            
            collection.add(elem);
            recibo.setTotal(calcularTotal(collection));
            recibo.setDetalleRecibo(collection);
            dao.actualizarRecibo(recibo);                  

        } catch (ErrorServiceException e) {
            throw e; // Re-lanzar la excepción específica de servicio
        } catch (Exception ex) {
            ex.printStackTrace(); // Imprimir el stack trace para depuración
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Elimina el detalleRecibo
     * @param idRecibo String
     * @param idDetalleRecibo String
     * @throws ErrorServiceException 
     */
    public void eliminarDetalleRecibo(String idRecibo, String idDetalleRecibo) throws ErrorServiceException {
        try {
            Recibo recibo = buscarRecibo(idRecibo);
            
            if (idDetalleRecibo == null || idDetalleRecibo.isEmpty()) {
                throw new ErrorServiceException("Debe indicar un DetalleRecibo");
            }
            
            Collection<DetalleRecibo> collection;
            
            if (recibo.getDetalleRecibo() == null) {
                throw new ErrorServiceException("No existen detalles de recibo en el recibo indicado");
            } else {
                collection = recibo.getDetalleRecibo();
            }
            
            DetalleRecibo elem = null;
            for (DetalleRecibo dr : collection) {
                if (dr.getId().equals(idDetalleRecibo)) {
                    elem = dr;
                }
            }
            
            if (elem == null) {
                throw new ErrorServiceException("El recibo no posee el detalle indicado");
            }
            
            collection.remove(elem);
            
            elem.setEliminado(true);
            
            collection.add(elem);
            recibo.setTotal(calcularTotal(collection));
            recibo.setDetalleRecibo(collection);
            dao.actualizarRecibo(recibo);                  

        } catch (ErrorServiceException e) {
            throw e; // Re-lanzar la excepción específica de servicio
        } catch (Exception ex) {
            ex.printStackTrace(); // Imprimir el stack trace para depuración
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Elimina el recibo especificado
     * @param id String
     * @throws ErrorServiceException 
     */
    public void eliminarRecibo(String id) throws ErrorServiceException {

        try {

            Recibo recibo = buscarRecibo(id);
            recibo.setEliminado(true);
            
            dao.actualizarRecibo(recibo);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
     /**
     * Busca el objeto y lo devuelve si lo encuentra
     * @param id String con el id
     * @return Objeto recibo
     * @throws ErrorServiceException 
     */
    public Recibo buscarRecibo(String id) throws ErrorServiceException {
        try {
            if (id == null) {
                throw new ErrorServiceException("Debe indicar el recibo");
            }

            Recibo recibo = dao.buscarRecibo(id);
            
            if (recibo.getEliminado()){
                throw new ErrorServiceException("No se encuentra el recibo indicado");
            }

            return recibo;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public double calcularTotal(Collection<DetalleRecibo> detalles){
        double total = 0.0;
        
        for (DetalleRecibo detalle : detalles) {
            if (detalle.isEliminado() == false) {
                total += detalle.getSubtotal();
            }
        }
        return total;
    }
    
    public void agregarDetalle(String idRecibo, DetalleRecibo detalleRecibo) {
        
    }
    
    public Collection<Recibo> listarReciboPorHabitante(String id) throws ErrorServiceException {
        try {
            
            return dao.listarReciboPorHabitante(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<Recibo> listarReciboActivo() throws ErrorServiceException {
        try {
            
            return dao.listarReciboActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<DetalleRecibo> listarDetalleReciboActivo(Recibo rec) throws ErrorServiceException {
        try {
            Collection<DetalleRecibo> collection = new ArrayList<>();
            Collection<DetalleRecibo> collectionIter = rec.getDetalleRecibo();
            if (collectionIter != null) {
                for (DetalleRecibo dr : collectionIter) {
                    if (dr.isEliminado() == false) {
                        collection.add(dr);
                    }
                }
            }
            
            return collection;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Devuelve la lista de departamentos del recibo
     * @param detalle Collection Recibo
     * @return String
     */
    public String getInfoDpto(Collection<DetalleRecibo> detalle) {
        String solution = "";
        String aux;
        int count = 0;
        for (DetalleRecibo dr : detalle) {
            aux = dr.getExpensaInmueble().getInmueble().getPisoDpto();
            if (count == 0) {
                solution = aux;
            } else {
                solution = solution + ", " + aux;
            }
            count = count + 1;
        }
        return solution;
    }
    
    /**
     * Llama a la clase de servicio de pdf para generar el recibo con todos los datos y luego llama a la clase de servicio de CuentaCorreo para enviarlo.
     * @param idRecibo String
     * @throws ErrorServiceException 
     */
    public void generarYEnviarRecibo(String idRecibo) throws ErrorServiceException {
        try {
            
            SimpleDateFormat formato = new SimpleDateFormat("dd_MM_yyyy");

            //Buscamos el recibo
            Recibo recibo = buscarRecibo(idRecibo);
            
            //Recuperamos los datos del recibo necesarios para generar el pdf
            double total = recibo.getTotal();
            Date fecha = recibo.getFechaPago();
            // Transformar la fecha a String
            String fechaSt = formato.format(fecha);
            Collection<DetalleRecibo> detalle = recibo.getDetalleRecibo();
            DetalleRecibo detalleR;
            Iterator<DetalleRecibo> iterator = detalle.iterator();
            
            if (!iterator.hasNext()) {
                throw new ErrorServiceException("El recibo no posee detalles");
            }
            
            detalleR = iterator.next();
            //Obtenemos cliente y mail en String
            String data = inmuebleService.obtenerResponsable(detalleR.getExpensaInmueble().getInmueble());
            String[] parts = data.split(",");
            String cliente = parts[1];
            String mail = parts[0];
            
            String inmuebles = getInfoDpto(detalle);
            
            //Creamos el path donde se guardará el pdf
            String path = "/" + cliente.replace(" ", "") + "Recibo" + fechaSt + ".pdf";
            //Se llama a la clase de servicio para generar el recibo en pdf
            servicePDF.generarRecibo("recibos", total, cliente, fecha, inmuebles, path);
            //Cuerpo del mail
            String body = "Saludos " + cliente + ". Le enviamos el recibo correspondiente al pago realizado por las expensas del consorcio. Saludos.";
            //Se llama a la clase de servicio para mandar el mail con el archivo generado
            correoService.sendEmail(mail, "Recibo Expensas", body, "recibos" + path);
            
        } catch (ErrorServiceException ex) { 
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema");
        }
        
    }
}
