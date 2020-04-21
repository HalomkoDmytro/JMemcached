package net.devstudy.jmemcashed.exception;

public class JMemcashedException extends RuntimeException{

    public JMemcashedException(String message) {
        super(message);
    }

    public JMemcashedException(String message, Throwable cause) {
        super(message, cause);
    }

    public JMemcashedException(Throwable cause) {
        super(cause);
    }
}
