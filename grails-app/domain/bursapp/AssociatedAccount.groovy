package bursapp

class AssociatedAccount {
    int id
    int userId
    int accountId
    String status

    static mapping = {
        id column: "id"
        userId column: "user_id"
        accountId column: "account_id"
        status column: "status", length: 45
        version false
    }

    static constraints = {

    }
}