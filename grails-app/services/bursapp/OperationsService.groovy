package bursapp

import Exceptions.APIException
import Exceptions.BadRequestException
import bursapp.operations.OperationsStatus
import bursapp.operations.OperationsType

/**
 * Created by tbuchaillot on 5/11/17.
 */
class OperationsService {
    def restService
    def messagesService
    def higyrusClient
    def higyrusUrl

    def getFunds(){

        def response = ["status":500,"message":"No se pudieron buscar las fondos.","funds":[]]

        try{
            def funds = restService.get(higyrusUrl + 'funds')
            if(funds.active_funds.size() == 0){
                response.status = 400
                response.message = "No hay fondos activos."
            }else{
                response.message = "Se obtuvieron los fondos correctamente."
                response.funds = funds?.active_funds
            }
            response.status = 200
        }catch (Exception e){
            return response
        }

        return response
    }

    def createOperation(user,operation_data, rescueAll = false ){
        def response = ["status":500,"message":"No se pudo crear la operacion.","operation_id": null]

        try{
            def amount = 0
            if(rescueAll){
                amount = higyrusClient.getAllAvailableMoney(operation_data.fund_id,operation_data.bank_account_id)
                if(amount == 0){
                    throw new APIException('Hubo un problema trayendo el monto')
                }
            }else {
                amount = operation_data.amount
            }

            def operation = new Operation(
                    fundId: operation_data.fund_id,
                    userId: user.id,
                    bankAccountId: operation_data.bank_account_id,
                    operatorId: 0,
                    amount: amount,
                    type: operation_data.type,
                    insertDate: new Date(),
                    lastUpdate: new Date(),
                    status: OperationsStatus.PENDING
            )
            operation.save(failOnError:false, flush:true, insert: true)
            if(operation.errors.errorCount >0){
                throw new Exception('Problema creando la operacion')
            }else{
                response.status = 200
                response.message = "Se creo la operacion correctamente."
                response.operation_id = operation.id

                def msg = "Su ##Operation numero "+operation.id+" ha sido registrado."
                switch (operation_data.type){
                    case OperationsType.SUSCRIPCION.toString():
                        msg = msg.replace('##Operation','deposito')
                        break
                    case OperationsType.RESCATE.toString():
                        msg = msg.replace('##Operation','pedido de rescate')
                        break
                    default:
                        msg = msg.replace('##Operation','operacion')
                        break
                }

                messagesService.saveMessage(user.id,operation_data.bank_account_id,msg)

            }
        }catch (APIException ae){
            response.message = ae.message
            response.status = 400
        }catch (Exception e){
            println e
            return  response
        }
        return response

    }


    def getAllOperations(user,bankAccountId){
        def response = ["status":500,"message":"No se pudieron obtener las operaciones.","operations": []]

        try{
            def operations = Operation.withCriteria {
                eq('userId',user.id)
                eq('bankAccountId',bankAccountId)
                eq('status',OperationsStatus.APPROVED.toString())

                order("lastUpdate", "desc")

            }
            def resultsList = []
            operations.each{ op ->
                def data = [""]
                data = op.asMap()
                def fund = restService.get(higyrusUrl+'funds/'+op.fundId)
                if(fund && fund?.name){
                    data.put('fundName' ,fund?.name)
                }
                data.put('operationTitle',getOperationTitle(data.type,data.status))
                data.put('amountDetail',data.type == OperationsStatus.APPROVED.toString() ? "A" : "D")
                resultsList.add(data)
            }


            response.status = 200
            response.message = "Se obtuvieron las operaciones correctamente."
            response.operations = resultsList
        }catch (Exception e){
            println e
            return  response
        }
        return response
    }

    def getAllPendingOperations(user,bankAccountId){
        def response = ["status":500,"message":"No se pudieron obtener las operaciones.","operations": []]

        try{
            def operations = Operation.withCriteria {
                eq('userId',user.id)
                eq('bankAccountId',bankAccountId)
                eq('status',OperationsStatus.PENDING.toString())

                order("lastUpdate", "desc")

            }

            def resultsList = []
            operations.each{ op ->
                def data = [""]
                data = op.asMap()
                def fund = restService.get(higyrusUrl+'funds/'+op.fundId)
                if(fund && fund?.name){
                    data.put('fundName' ,fund?.name)
                }
                data.put('operationTitle',getOperationTitle(data.type,data.status))
                data.put('amountDetail',data.type == OperationsStatus.APPROVED.toString() ? "A" : "D")
                resultsList.add(data)
            }


            response.status = 200
            response.message = "Se obtuvieron las operaciones correctamente."
            response.operations = resultsList
        }catch (Exception e){
            println e
            return  response
        }
        return response
    }

    def getAllOperationsByDate(user,bankAccountId, dateStart, dateFinish){
        def response = ["status":500,"message":"No se pudieron obtener las operaciones.","operations": []]

        try{

            def operations = Operation.withCriteria {
                eq('userId',user.id)
                eq('bankAccountId',bankAccountId)
                between('insertDate', dateStart, dateFinish)
                order("insertDate", "desc")

            }
            def resultsList = []
            operations.each{ op ->
                def data = [""]
                data = op.asMap()
                def fund = restService.get(higyrusUrl+'funds/'+op.fundId)
                if(fund && fund?.name){
                    data.put('fundName' ,fund?.name)
                }
                data.put('operationTitle',getOperationTitle(data.type,data.status))
                data.put('amountDetail',data.type == OperationsStatus.APPROVED.toString() ? "A" : "D")
                resultsList.add(data)
            }


            response.status = 200
            response.message = "Se obtuvieron las operaciones correctamente."
            response.operations = resultsList
        }catch (Exception e){
            println e
            return  response
        }
        return response
    }

    def createOperationStatus(user,bankAccountId,status){
        def response = ["status":500,"message":"No se pudo crear el estado."]
        try{
            if (checkIfStatusExists(user.id,bankAccountId,status)){
                response.message = "El estado ya existe."
                response.status = 400
            }else{
                def newStatus = new CustomStatus(
                        userId: user.id,
                        bankAccountId: bankAccountId,
                        name: status.toUpperCase(),
                        available: true,
                        insertDate: new Date()
                )
                newStatus.save(failOnError:false, flush:true, insert: true)
                if(newStatus.errors.errorCount >0){
                    throw new Exception('Problema guardando el estado.')
                }else {
                    response.status = 200
                    response.message = "Se creo el estado correctamente."
                }
            }
        }catch (Exception e){
            println e.message
            return response
        }
        return response
    }

    def changeOperationStatus(user,operationId,status){

        def response = ["status":500,"message":"No se pudo cambiar el estado de la operacion.",operation: null]
        try{
            def operation = Operation.findById(operationId)
            if(!operation){
                throw new BadRequestException('OperationId invalido.')
            }else if(operation.operatorId != 0 && operation.operatorId != user.id){
                throw new BadRequestException('Esta operacion esta tomada por otro Operador.')
            }
            if(!checkIfStatusExists(operation.userId,operation.bankAccountId,status)){
                throw new BadRequestException('El estado que esta intentando aplicar no existe.')
            }else{
                operation.status = status
                operation.lastUpdate = new Date()
                operation.operatorId = user.id
                operation.save(failOnError:false, flush:true, insert: true)
                if(operation.errors.errorCount >0){
                    throw new Exception('Problema modificando la operacion.')
                }else {
                    response.status = 200
                    response.message = "Se modifico el estado de la operacion correctamente."
                    response.operation = operation


                    def msg = "Su ##Operation numero "+operation.id+" esta en estado "+status.toUpperCase()+"."
                    switch (operation.type){
                        case OperationsType.SUSCRIPCION.toString():
                            msg = msg.replace('##Operation','deposito')
                            break
                        case OperationsType.RESCATE.toString():
                            msg = msg.replace('##Operation','pedido de rescate')
                            break
                        default:
                            msg = msg.replace('##Operation','operacion')
                            break
                    }

                    messagesService.saveMessage(operation.userId,operation.bankAccountId,msg)

                }
            }
        }catch (BadRequestException br){
            response.message = br.message
            response.status = br.status

        }catch (Exception e){
            println e
            return response
        }
        return response
    }

    def getOperationStatuses(user,bankAccountId){
        def response = ["status":500,"message":"No se pudieron obtener los status.", "statuses" : []]
        try{
            def statusList = CustomStatus.withCriteria {
                eq('userId', user.id)
                eq('bankAccountId', bankAccountId)
            }

            def resultList = []
            OperationsStatus.values().each { resultList.add(it.toString())}
            statusList.each{  resultList.add(it.name) }

            response.status = 200
            response.message = "Se obtuvieron los status correctamente."
            response.statuses = resultList

        }catch (Exception e){
            return response
        }
        return response


    }

    private def getOperationTitle(type,status){
        def title = ""
        switch (status){
            case OperationsStatus.PENDING.toString():
                title += "Liquidacion de "
                break
            default:
                title += "Solicitud de "
                break
        }

        switch (type){
            case OperationsType.SUSCRIPCION.toString():
                title += "Suscripcion"
                break
            case OperationsType.RESCATE.toString():
                title += "Rescate"
                break
        }
        return title

    }

    private def checkIfStatusExists(userId,bankAccountId,status){
        def statusExists = CustomStatus.withCriteria {
            eq('userId',userId)
            eq('bankAccountId',bankAccountId)
            eq('name',status.toUpperCase())
        }

        statusExists || OperationsStatus.contains(status)
    }



}
