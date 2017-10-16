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

    static mapping = {
        email column: "email", length:120
        name column: "name", length:70
        lastname column: "lastname", length:70
        username column: "username", length: 32
        password column: "password", length: 32
        dateCreated column: "dateCreated", sqlType:"datetime"
        lastUpdated column: "lastUpdated", sqlType:"datetime"
        version false


    }
    static constraints = {
    }
}
