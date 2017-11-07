package bursapp.operations

/**
 * Created by tbuchaillot on 5/11/17.
 */
public enum OperationsStatus {
    PENDING,  //El elemento se creo pero todavia no se aprobo por un operador
    CANCELLED,//El elemento se aprobo por un operador pero la transaccion fue cancelada
    APPROVED, //El elemento se aprobo por un operador y la operacion se ejecuto correctamente


    public static boolean contains(String s)
    {
        for(OperationsStatus type:values())
            if (type.name().equals(s))
                return true;
        return false;
    }
}