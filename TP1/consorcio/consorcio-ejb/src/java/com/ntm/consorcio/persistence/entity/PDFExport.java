
package com.ntm.consorcio.persistence.entity;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.ntm.consorcio.persistence.ErrorDAOException;
import java.io.File;
import java.io.FileOutputStream;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * Clase de acceso a datos del PDF
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class PDFExport {
    
    /**
     * Genera el recibo básico
     * @param path String
     * @param total String
     * @param cliente String
     * @param fecha String
     * @param inmuebles String
     * @throws ErrorDAOException 
     */
    public void generarRecibo(String path, String total, String cliente, String fecha, String inmuebles, String path2) throws ErrorDAOException {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); // Crea el directorio si no existe
        }
         String archivo = path + path2; 

        try {
            // Creamos el documento
            Document documento = new Document();

            // Creamos el escritor que va a guardar el pdf en la ruta indicada
            PdfWriter.getInstance(documento, new FileOutputStream(archivo));

            // Abrimos el documento que creamos
            documento.open();

            // Agregamos el contenido al documento
            documento.add(new Paragraph("RECIBO DE PAGO"));
            documento.add(new Paragraph("Fecha: " + fecha));
            documento.add(new Paragraph("Cliente: " + cliente));
            documento.add(new Paragraph("Monto total: $" + total));
            documento.add(new Paragraph("Descripción: Pago de las expensas del consorcio para los inmuebles " + inmuebles));

            // Cerrar el documento para guardarlo
            documento.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorDAOException("Error al generar el recibo");
        }
    }
}
