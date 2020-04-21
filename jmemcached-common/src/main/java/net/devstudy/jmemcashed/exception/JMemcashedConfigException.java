package net.devstudy.jmemcashed.exception;

public class JMemcashedConfigException extends RuntimeException {

    public JMemcashedConfigException(String message) {
        super(message);
    }

    public JMemcashedConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
