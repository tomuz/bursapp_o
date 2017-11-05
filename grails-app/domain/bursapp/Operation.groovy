package bursapp

/**
 * Created by tbuchaillot on 5/11/17.
 */
class Operation {

    int id
    int fundId
    int userId
    int accountId
    String type
    String status
    Double amount
    Date insertDate
    Date lastUpdate
    int operatorId

    static mapping = {
        id column: "id"
        userId column: "user_id"
        accountId column: "account_id"
        fundId column: "fund_id"
        operatorId column: "operator_id"
        amount column: "amount"
        status column: "status"
        type column: "type"
        insertDate column: "insert_date", sqlType:"datetime"
        lastUpdate column: "last_update", sqlType:"datetime"
        version false
    }

    static constraints = {

    }
}
