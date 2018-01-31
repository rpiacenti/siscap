package br.com.correios.ppjsiscap.util;

import javax.ejb.ApplicationException;


@SuppressWarnings("serial")
@ApplicationException(rollback=false)
public class EnvioEmailException extends Exception {

    public EnvioEmailException() {
        super();
    }

    public EnvioEmailException(String errMsg) {
        super(errMsg);
    }

    public EnvioEmailException(Exception exc) {
        this(null, exc);
    }

    public EnvioEmailException(String errMsg, Exception exc) {
        super(errMsg);
        Throwable t = exc;
        if (t != null) {
            Throwable cause = t.getCause();
            while (cause instanceof EnvioEmailException) {
                t = cause;
                cause = t.getCause();
            }
        }
        initCause(t);
    }
}
