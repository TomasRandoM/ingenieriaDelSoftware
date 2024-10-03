package com.ntm.consorcio.persistence;

import javax.ejb.ApplicationException;
/**
 *
 * @author Tomas
 */
@ApplicationException(rollback=false)
public class NoResultDAOException extends Exception {
    
    public NoResultDAOException() {
    }
    
    public NoResultDAOException(String mensaje) {
        super(mensaje);
    }
}