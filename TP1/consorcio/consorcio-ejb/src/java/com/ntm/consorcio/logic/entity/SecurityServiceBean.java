package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.logic.ErrorServiceException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * Clase de servicio que implementa la lógica para la encriptación de claves
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class SecurityServiceBean {
    public String hashClave(String clave) throws ErrorServiceException {
        try {
            //Se utiliza el algoritmo SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //Calcula el hash y devuelve los bytes
            byte[] hashedBytes = md.digest(clave.getBytes());

            // Convierte los bytes en String
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                //Convierte cada byte a hexadecimal y lo añade al StringBuilder
                sb.append(String.format("%02x", b));
            }
            //Se devuelve la clave con el hash como String
            return sb.toString(); 
        } catch (NoSuchAlgorithmException ex) {
            throw new ErrorServiceException("Error encriptando la clave");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
