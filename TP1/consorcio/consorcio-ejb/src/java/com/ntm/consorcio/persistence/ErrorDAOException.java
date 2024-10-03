package com.ntm.consorcio.persistence;
import javax.ejb.ApplicationException;
/**
 *
 * @author Tomas
 */
@ApplicationException(rollback=false)
public class ErrorDAOException extends Exception {
    
    public ErrorDAOException() {
    }
    
    public ErrorDAOException(String mensaje) {
        super(mensaje);
    }
}