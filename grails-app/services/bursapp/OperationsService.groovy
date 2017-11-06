package bursapp

import bursapp.operations.OperationsStatus
import bursapp.operations.OperationsType

/**
 * Created by tbuchaillot on 5/11/17.
 */
class OperationsService {
    def restService
    def messagesService
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

            if(rescueAll){
                def amount = restService.getAllAvia
            }

            def operation = new Operation(
                    fundId: operation_data.fund_id,
                    userId: user.id,
                    bankAccountId: operation_data.bank_account_id,
                    operatorId: 0,
                    amount: operation_data.amount,
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

                messagesService.saveMessage(user,operation_data.bank_account_id,msg)

            }
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
}
