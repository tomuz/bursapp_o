package bursapp

class Session {
    int id
    String token
    String status
    int userId

    static mapping = {
        userId column: "user_id"
        token column: "token", length:70
        id column: "id"
        status column: "status", length: 45
        version false
    }
    static constraints = {
    }
}
