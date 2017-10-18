package bursapp



import grails.converters.JSON

class AccountsController {
	static responseFormats = ['json', 'xml']

    def usersService
    def accountsService

    def associateAccount(){
        def accountJson = request.JSON

        def result
        def status = 400

        try{
            def user = usersService.getUserFromToken(accountJson.token)
            if(user){
                result = accountsService.associateAccount(accountJson, user)
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

    def removeAssociatedAccount(){
        def accountJson = request.JSON

        def result
        def status = 400

        try{
            User user = usersService.getUserFromToken(accountJson.token)
            if(user){
                result = accountsService.removeAssociatedAccount(accountJson, user)
                status = result.status
            }else{
                result = ['message':'No hay usuarios activos con esa session.']
                status = 404
            }

        }catch (Exception e){
            result = ['message':'Hubo un problema en el servidor: '+e.message]
            status = 500
        }

        render (result as JSON, status: status)
    }

    def getAssociatedAccounts(){
        def accountJson = request.JSON

        def result
        def status = 400

        try{
            def user = usersService.getUserFromToken(accountJson.token)
            if(user){
                result = accountsService.getAssociatedAccounts(user)
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

}
