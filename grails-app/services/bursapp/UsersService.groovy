package bursapp

import org.springframework.transaction.annotation.Transactional
import static java.util.UUID.randomUUID

@Transactional
class UsersService {

    static def EMAIL_PATTERN = /[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})/

    def getUser(int userId) {
        def user = findUserById(userId)
        user.password = 'No se puede mostrar la password :)'
        return user
    }

    def getUser(String data){
        def user
        if((data ==~ EMAIL_PATTERN)){
            user  = findUserByEmail(data)
        }else{
            user = findUserByUsername(data)
        }
        if(user){
            user.password = 'No se puede mostrar la password :)'
        }
        return user
    }

    def createUser(userJson){
        def message = ""
        def status = 201


        def required = ['email','username','password']


        if(!userJson.keySet().containsAll(required)){
            message =  required.join(",") + '  son requeridos.'
            status = 400
        }else if(required.every{ userJson[it].size() <= 6}){
            message = required.join(",") + ' tiene que ser mayor a 6 caracteres.'
            status = 400
        }else  if(getUser(userJson.username as String)){
            message = 'El usuario ya existe.'
            status = 400
        }else if (!(userJson.email ==~ EMAIL_PATTERN)){
            message = 'Email incorrecto.'
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
                message = 'Hubo un problema guardando el user.'
                status = 500
            }else{
                message = 'User '+userJson.username+' creado.'
                status = 201
            }
        }

        return [message:message,status:status]
    }


    def authenticate(userData){
        def response = ['status':500,'message':'No se pudo loguear.','token':null, 'user':null]

        if(!userData?.username || !userData?.password){
            response.status = 400
            response.message = 'Necesitas un username y password para poder ingresar.'
            return response
        }
        try{
            def user = credentialsAreCorrect(userData)

            if(user){
                def session =findActiveSessionByUserId(user.id)  //TODO ver si le devolvemos el token cuando ya esta logged y con que status.

                if(session){
                    response.status = 200
                    response.message = "Usuario ya logueado."
                    response.token = session.token
                    response.user = user
                    return response
                }

                response.status = 200
                response.message = 'Logged in.'

                def newToken = randomUUID() as String
                newToken = newToken.toUpperCase()

                def newSession = new Session(
                        userId:  user.id,
                        token: newToken,
                        status: 'ACTIVE'
                )
                newSession.save(failOnError:true, flush:true, insert: true)
                if(newSession.errors.errorCount >0){
                    response.message = 'Hubo un problema creando la session.'
                    response.status = 500
                }else{
                    response.token = newToken
                    response.user = user
                }
            }else{
                response.status = 401
                response.message = 'El username o password es incorrecto.'
            }
        }catch (Exception e){
            response.message = e
            return  response
        }
        return response
    }

    def logOut(userData){
        def response = ["status":400,"message":"Cannot logout."]

        if(!userData?.token){
            response.message = "Token incorrecto."
            return response
        }

        try{
            def session = findActiveSessionByToken(userData.token)
            if(session){
                session.status = 'INACTIVE'
                session.save(failOnError:true, flush:true, insert: true)
                if(session.errors.errorCount >0){
                    response.message = 'There was a problem creating the session.'
                    response.status = 500
                }else{
                    response.status = 200
                    response.message = "Logged out."
                }
            }else{
                response.status = 400
                response.message = "Active token not found."
            }

        }catch (Exception e){
            return  response
        }

        return response
    }

    @Transactional
    def getUserFromToken(token){
        def session =findActiveSessionByToken(token)
        if(session){
            def user = getUser(session.userId)
            return user
        }else {
            return null
        }
    }

    private def credentialsAreCorrect(userData){
        def results = User.withCriteria {
            eq('username',userData.username)
            eq('password',userData.password)
        }

        return results[0]
    }

    //User functions
    private def findUserById(userId){
        return User.findById(userId)
    }
    private def findUserByUsername(def username){
        return User.findByUsername(username)
    }
    private def findUserByEmail(def email){
        return User.findByEmail(email)
    }

    //Session functions
    private def findActiveSessionByUserId(def userId){
        def results = Session.withCriteria {
            eq('userId',userId)
            eq('status','ACTIVE')
        }
        return results[0]
    }

    private def findSessionByUserId(def userId){
        return Session.findByUserId(userId)
    }

    private def findActiveSessionByToken(def token){
        def results = Session.withCriteria {
            eq('token',token)
            eq('status','ACTIVE')
        }
        return results[0]
    }
}
