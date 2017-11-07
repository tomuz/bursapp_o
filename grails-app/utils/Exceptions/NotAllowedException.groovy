package Exceptions

/**
 * Created by tbuchaillot on 6/11/17.
 */
class NotAllowedException extends APIException{
    def status = 405

    def NotAllowedException(message, error = "not_allowed", cause = []) {
        super(message, error, cause)
    }
}
