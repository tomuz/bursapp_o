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
        def rescueAll = null
        try{
            def user = usersService.getUserFromToken(json.token)
            if(user){
                def operation_data = checkNewOperation(json)
                if(operation_data?.rescue_all && operation_data?.rescue_all == true){
                    result = operationsService.createOperation(user,operation_data, true)
                }else{
                    result = operationsService.createOperation(user,operation_data)
                }
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


    def getAllOperations(){
        def json = request.JSON

        def result
        def status = 400

        try{
            def user = usersService.getUserFromToken(json.token)
            if(user){
                if(!json?.bank_account_id){
                    throw new BadRequestException('Es necesario el numero de cuenta.')
                }
                result = operationsService.getAllOperations(user,json?.bank_account_id)
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

    def getPendingOperations(){
        def json = request.JSON

        def result
        def status = 400

        try{
            def user = usersService.getUserFromToken(json.token)
            if(user){
                if(!json?.bank_account_id){
                    throw new BadRequestException('Es necesario el numero de cuenta.')
                }
                result = operationsService.getAllPendingOperations(user,json?.bank_account_id)
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

    def getLastsOperations(){
        def json = request.JSON


        def result
        def status = 400
        def dateStart = new Date() - 30
        def dateFinish = new Date()
        try{
            def user = usersService.getUserFromToken(json.token)
            if(user){
                if(!json?.bank_account_id){
                    throw new BadRequestException('Es necesario el numero de cuenta, la fecha de inicio y la fecha de fin.')
                }
                result = operationsService.getAllOperationsByDate(user,json?.bank_account_id, dateStart, dateFinish)
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


    def getOperationsByDate(){
        def json = request.JSON


        def result
        def status = 400
        def DATE_PATTERN = 'yyyy-MM-dd HH:mm:ss'
        def dateStart
        def dateFinish
        try{
            def user = usersService.getUserFromToken(json.token)
            if(user){
                if(!json?.bank_account_id || !json?.date_start || !json?.date_finish){
                    throw new BadRequestException('Es necesario el numero de cuenta, la fecha de inicio y la fecha de fin.')
                }else {
                    try{
                        dateStart = Date.parse(DATE_PATTERN,json?.date_start)
                        dateFinish = Date.parse(DATE_PATTERN,json?.date_finish)
                    }catch (Exception e){
                        throw new BadRequestException("El formato de las fechas es yyyy-MM-dd HH:mm:ss .")
                    }
                }
                result = operationsService.getAllOperationsByDate(user,json?.bank_account_id, dateStart, dateFinish)
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
        def requiredFields = ['fund_id','bank_account_id','amount','type']
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
