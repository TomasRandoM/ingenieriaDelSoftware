

package com.ntm.consorcio.logic;

import javax.ejb.ApplicationException;

/**
 *
 * @author Tomas Rando
 */
@ApplicationException(rollback=false)
public class ErrorServiceLoginException extends Exception {
    /**
     * Creates a new instance of <code>PaisException</code> without detail message.
     */
    public ErrorServiceLoginException() {
    }
    /**
     * Constructs an instance of <code>PaisException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ErrorServiceLoginException(String msg) {
        super(msg);
    }
}