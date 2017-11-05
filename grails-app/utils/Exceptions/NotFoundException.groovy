package Exceptions

/**
 * Created by tbuchaillot on 5/11/17.
 */
class NotFoundException  extends  APIException{

    def NotFoundException(message, error = "not_found", cause = []) {
        super(message, error, cause)
    }

    def status = 404
}
