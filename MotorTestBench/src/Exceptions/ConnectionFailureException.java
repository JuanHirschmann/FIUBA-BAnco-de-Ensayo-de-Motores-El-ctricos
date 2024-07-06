package Exceptions;

/**
 * ConnectionFailureException
 */
public class ConnectionFailureException extends Exception{

    public ConnectionFailureException () {

    }

    public ConnectionFailureException (String message) {
        super (message);
    }

    public ConnectionFailureException (Throwable cause) {
        super (cause);
    }

    public ConnectionFailureException (String message, Throwable cause) {
        super (message, cause);
    }
    
}