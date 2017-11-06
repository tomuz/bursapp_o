package bursapp

class User {
    int id
    String username
    String password
    String email
    String name
    String lastname
    Date dateCreated
    Date lastUpdated
    String type
    static mapping = {
        email column: "email", length:120
        name column: "name", length:70
        lastname column: "lastname", length:70
        username column: "username", length: 32
        password column: "password", length: 64
        type column: "type", length: 45
        dateCreated column: "insert_date", sqlType:"datetime"
        lastUpdated column: "last_update", sqlType:"datetime"
        version false
    }
    static constraints = {
    }
}
