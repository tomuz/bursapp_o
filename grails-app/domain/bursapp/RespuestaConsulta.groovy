package bursapp

/**
 * Created by tbuchaillot on 6/11/17.
 */
class RespuestaConsulta {
    int id
    int userId
    int consultaId
    int operatorId
    String body
    String status
    Date insertDate
    static mapping = {
        id column: "id"
        userId column: "user_id"
        consultaId column: "consulta_id"
        operatorId column: "operator_id"
        body column: "body"
        insertDate column: "insert_date", sqlType:"datetime"
        version false
    }

}
