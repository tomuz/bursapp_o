package bursapp

import Exceptions.BadRequestException
import grails.converters.JSON

/**
 * Created by tbuchaillot on 5/11/17.
 */
class MessagesController {

    def usersService
    def messagesService
    def getMessages(){
        def json = request.JSON

        def result
        def status = 400

        try{
            def user = usersService.getUserFromToken(json.token)
            if(user){
                if(!json?.bank_account_id){
                    throw  new BadRequestException('Es necesario el bank_account_id.')
                }
                result = messagesService.getMessages(user.id,json?.bank_account_id)
                status = result.status
            }else{
                result = ['message':'No hay usuarios activos con esa session.']
                status = 404
            }

        }catch (BadRequestException br){
            result = ['message': br.message]
            status = 400

        }catch (Exception e){
            result = ['message':'Hubo un problema en el servidor.']
            status = 500
        }

        render (result as JSON, status: status)
    }

}
