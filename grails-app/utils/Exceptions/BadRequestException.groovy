package Exceptions

/**
 * Created by tbuchaillot on 5/11/17.
 */
class BadRequestException extends APIException {
    def status = 400

    def BadRequestException(message, error = "bad_request", cause = []) {
        super(message, error, cause)
    }

}

