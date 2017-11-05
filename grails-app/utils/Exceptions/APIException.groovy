package Exceptions

/**
 * Created by tbuchaillot on 15/10/17.
 */
class APIException extends RuntimeException {

    def status = 500
    def error
    def internalCause = []
    private static final long serialVersionUID = 1L;

    public APIException() {
        super();
    }

    def APIException(message, error, cause) {
        super(message.toString(), (cause in Throwable) ? cause : null)
        this.error = error
        this.internalCause = cause
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
