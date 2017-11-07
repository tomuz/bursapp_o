package bursapp

/**
 * Created by tbuchaillot on 6/11/17.
 */
class Consulta {
    int id
    int userId
    String subject
    String body
    String status
    Date insertDate
    static mapping = {
        id column: "id"
        userId column: "user_id"
        subject column: "subject"
        body column: "body"
        status column: "status"
        insertDate column: "insert_date", sqlType:"datetime"
        version false
    }

}
