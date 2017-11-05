package bursapp

import Exceptions.BadRequestException
import bursapp.operations.OperationsType

/**
 * Created by tbuchaillot on 5/11/17.
 */
import grails.converters.JSON


class OperationsController {

    def operationsService
    def usersService

    def createOperation(){
        def json = request.JSON

        def result
        def status = 400

        try{
            def user = usersService.getUserFromToken(json.token)
            if(user){
                def operation_data = checkNewOperation(json)
                result = operationsService.createOperation(user,operation_data)
                status = result.status
            }else{
                result = ['message':'No hay usuarios activos con esa session.']
                status = 404
            }

        }catch (BadRequestException br){
            result = ['message':br.message]
            status = 400
        }catch (Exception e){
            result = ['message':'Hubo un problema en el servidor.']
            status = 500
        }

        render (result as JSON, status: status)
    }


    def getOperations(){
        def json = request.JSON

        def result
        def status = 400

        try{
            def user = usersService.getUserFromToken(json.token)
            if(user){
                def operation_data = unmarshallOperation(json)
                result = operationsService.createOperation(user,operation_data)
                status = result.status
            }else{
                result = ['message':'No hay usuarios activos con esa session.']
                status = 404
            }

        }catch (BadRequestException br){
            result = ['message':br.message]
            status = 400
        }catch (Exception e){
            result = ['message':'Hubo un problema en el servidor.']
            status = 500
        }

        render (result as JSON, status: status)
    }

    def getFunds(){
        def accountJson = request.JSON

        def result
        def status = 400

        try{
            def user = usersService.getUserFromToken(accountJson.token)
            if(user){
                result = operationsService.getFunds()
                status = result.status
            }else{
                result = ['message':'No hay usuarios activos con esa session.']
                status = 404
            }

        }catch (Exception e){
            result = ['message':'Hubo un problema en el servidor.']
            status = 500
        }

        render (result as JSON, status: status)
    }


    private def checkNewOperation(json){
        def requiredFields = ['fund_id','account_id','amount','type']
        def hasRequired = true
        requiredFields.each {  hasRequired = hasRequired && json.containsKey(it) }

        if(!hasRequired){
            throw new BadRequestException('Para ejecutar una operacion se necesitan los campos:'+requiredFields.join(','))
        }else if (!OperationsType.contains(json.type)){
            throw new BadRequestException('Tipo invalido de operacion')
        }else{
            return json
        }
    }
}
