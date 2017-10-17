package bursapp

import Exceptions.APIException
import grails.converters.JSON

class UsersController {
	static responseFormats = ['json']

    def usersService

    def getUser(){
        def userData = params.userData
        if(!userData){
            throw new APIException("Please insert an userId")
        }

        def result
        def status = 200
        try{
            if (userData.isInteger()){
                userData = userData as Integer
            }

            def getUser = usersService.getUser(userData)
            if(getUser){
                result = ['user':getUser]
            }else{
                status = 404
                result = ['user':'No user with '+userData]
            }
        }catch (Exception e){
            result = ['message':'there was a problem in the server :'+e]
            status = 500
        }

        render (result as JSON, status: status)
    }


    def createUser(){
        def userJson = request.JSON

        def result
        def status = 200
        try{
            def createResponse = usersService.createUser(userJson)
            result = ['message':createResponse.message]
            status = createResponse.status
        }catch (Exception e){
            result = ['message':'There was a problem in the server']
            status = 500
        }

        render (result as JSON, status: status)

    }

    def login(){
        def userData = request.JSON

        def result
        def status
        try{
            result = usersService.authenticate(userData)
            status = result?.status ?: 500
        }catch (Exception e){
            result = ['message':'There was a problem in the server '+e]
            status = 500
        }

        render (result as JSON, status: status)
    }

    def logout(){
        def userData = request.JSON

        def result
        def status
        try{
            result = usersService.logOut(userData)
            status = result?.status ?: 500
        }catch (Exception e){
            result = ['message':'There was a problem in the server']
            status = 500
        }

        render (result as JSON, status: status)
    }

    def associateAccount(){

    }

}
