package bursapp

import org.springframework.transaction.annotation.Transactional
import bursapp.User

@Transactional
class UsersService {

    static def EMAIL_PATTERN = /[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})/

    private def findUserById(userId){
        return User.findById(userId)
    }
    private def findUserByUsername(def username){
        return User.findByUsername(username)
    }
    private def findUserByEmail(def email){
        return User.findByEmail(email)
    }

    def getUser(int userId) {
        def user = findUserById(userId)
        user.password = 'Cannot show password :)'
        return user
    }

    def getUser(String data){
        def user
        println data
        if((data ==~ EMAIL_PATTERN)){
            println 'email'
            user  = findUserByEmail(data)
        }else{
            user = findUserByUsername(data)
            println user
        }
        if(user){
            user.password = 'Cannot show password :)'
        }
        return user
    }

    def createUser(userJson){
        def message = ""
        def status = 201


        def required = ['email','username','password']


        if(!userJson.keySet().containsAll(required)){
            message =  required.join(",") + ' are requiered.'
            status = 400
        }else if(required.every{ userJson[it].size() <= 6}){
            message = required.join(",") + ' must be greater than 4 characters.'
            status = 400
        }else if (!(userJson.email ==~ EMAIL_PATTERN)){
            message = 'Wrong email format.'
            status = 400
        }else{
            def user = new User(
                    username : userJson.username,
                    password : userJson.password,
                    email: userJson.email,
                    name: userJson?.name?:"",
                    lastname: userJson?.lastname?:"",
                    dateCreated: new Date(),
                    lastUpdated: new Date()
            )

            user.save(failOnError:true, flush:true, insert: true)
            if(user.errors.errorCount > 0){
                message = 'There was a problem saving the user.'
                status = 500
            }else{
                message = 'User '+userJson.username+' created.'
                status = 201
            }
        }

        return [message:message,status:status]

    }
}
