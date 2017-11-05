package bursapp

import bursapp.operations.OperationsStatus

/**
 * Created by tbuchaillot on 5/11/17.
 */
class OperationsService {
    def restService
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

    def createOperation(user,operation_data ){
        def response = ["status":500,"message":"No se pudo crear la operacion.","operation_id": null]

        try{
            def operation = new Operation(
                    fundId: operation_data.fund_id,
                    userId: user.id,
                    accountId: operation_data.account_id,
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
            }
        }catch (Exception e){
            println e
            return  response
        }
        return response

    }

    def getAllOperations(){

    }
}
