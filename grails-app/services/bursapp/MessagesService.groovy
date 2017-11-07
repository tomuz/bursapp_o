package bursapp

import bursapp.operations.OperationsStatus

/**
 * Created by tbuchaillot on 5/11/17.
 */
class MessagesService {

    def getMessages(userId, bankAccountId){
        def response = ["status":500,"message":"No se pudieron obtener los mensajes.","messages": []]

        try{
            def messages = Message.withCriteria {
                eq('userId',userId)
                eq('bankAccountId',bankAccountId)

                order("insertDate", "desc")

            }

            response.status = 200
            response.message = "Se obtuvieron los mensajes correctamente."
            response.messages = messages
        }catch (Exception e){
            println e
            return  response
        }
        return response

    }

    def saveMessage(userId, bankAccountId, message){
        try{
            def newMessage = new Message(
                    userId: userId,
                    bankAccountId: bankAccountId,
                    message: message,
                    insertDate: new Date()
            )
            newMessage.save(failOnError:false, flush:true, insert: true)
            if(newMessage.errors.errorCount >0){
                throw new Exception('Problema creando el mensaje.')
            }

        }catch (Exception e){
            println 'error saving the msg '+e
        }
    }
}
