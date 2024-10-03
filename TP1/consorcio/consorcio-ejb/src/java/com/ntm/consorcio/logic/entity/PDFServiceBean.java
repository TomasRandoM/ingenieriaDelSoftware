
package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.ErrorDAOException;
import com.ntm.consorcio.persistence.entity.PDFExport;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * Clase de servicio de PDF
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class PDFServiceBean {
    private @EJB PDFExport dao;
    
    /**
     * Transforma los valores a String y llama al dao para generar el recibo
     * @param path
     * @param total
     * @param cliente
     * @param fecha
     * @param inmuebles
     * @throws ErrorServiceException 
     */
    public void generarRecibo(String path, double total, String cliente, Date fecha, String inmuebles, String path2) throws ErrorServiceException {
        try {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        String fechaString = f.format(fecha);
        dao.generarRecibo(path, String.valueOf(total), cliente, fechaString, inmuebles, path2);
        } catch (ErrorDAOException ex) {
            throw new ErrorServiceException("Error generando el pdf");
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
