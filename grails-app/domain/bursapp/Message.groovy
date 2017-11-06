package bursapp

/**
 * Created by tbuchaillot on 5/11/17.
 */
class Message {
    int id
    int userId
    int bankAccountId
    String message
    Date insertDate
    static mapping = {
        id column: "id"
        userId column: "user_id"
        bankAccountId column: "bank_account_id"
        message column: "message"
        insertDate column: "insert_date", sqlType:"datetime"
        version false
    }

    static constraints = {

    }
}
