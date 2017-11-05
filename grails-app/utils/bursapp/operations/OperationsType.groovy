package bursapp.operations

/**
 * Created by tbuchaillot on 5/11/17.
 */
public enum OperationsType {
    RESCATE, //Saca plata de la cuenta
    SUSCRIPCION // Mete plata en la cuenta


    public static boolean contains(String s)
    {
        for(OperationsType type:values())
            if (type.name().equals(s))
                return true;
        return false;
    }
}