

package com.ntm.consorcio.logic;

import javax.ejb.ApplicationException;

/**
 *
 * @author Dell
 */
@ApplicationException(rollback=true)
public class ErrorServiceException extends Exception {
    /**
     * Creates a new instance of <code>PaisException</code> without detail message.
     */
    public ErrorServiceException() {
    }
    /**
     * Constructs an instance of <code>PaisException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ErrorServiceException(String msg) {
        super(msg);
    }
}