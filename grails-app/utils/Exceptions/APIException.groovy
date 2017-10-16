package Exceptions

/**
 * Created by tbuchaillot on 15/10/17.
 */
class APIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public APIException() {
        super();
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(Throwable cause) {
        super(cause);
    }
}
