package bursapp.operations

/**
 * Created by tbuchaillot on 5/11/17.
 */
public enum OperationsStatus {
    PENDING,  //El elemento se creo pero todavia no se aprobo por un operador
    APPROVED_PENDING_EJECUTION,  //El elemento se aprobo pero todavia no se realizo la transaccion
    CANCELLED,//El elemento se cancelo por un operador
    REJECTED,//El elemento se aprobo por un operador pero la transaccion fue cancelada
    SUCCESS, //El elemento se aprobo por un operador y la operacion se ejecuto correctamente


}