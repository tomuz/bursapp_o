package bursapp

import org.springframework.transaction.annotation.Transactional

@Transactional
class AccountsService {

    def higyrusClient

    def associateAccount(account,user) {
        def response = ["status":500,"message":"No se pudo asociar la cuenta.","account_higyrus":[]]

        if(!account?.username || !account?.password){
            response.message = 'Necesitas ingresar usuario y password de Higyrus.'
            return response
        }
        try{
            def higyrusAccount = higyrusClient.validateAccount(account)

            if(higyrusAccount.status != 200){
                response.message = 'No se pudo autenticar la cuenta Higyrus.'
                response.status = 404
                return response
            }else{
                higyrusAccount = higyrusAccount.response

                def isAlreadyAssociated  = findAssociatedAccount(user.id as int,higyrusAccount.account_id as int )
                if(isAlreadyAssociated){
                    response.status = 200
                    response.message = 'Cuenta ya asociada.'
                    return response
                }

                response.status = 200
                response.message = "Cuenta asociada satisfactoriamente."
                response.account_higyrus.add(higyrusAccount)

               def newAssociation = new AssociatedAccount(
                        accountId: higyrusAccount.account_id,
                        userId: user.id,
                        status: "ASSOCIATED"
                )
                println ('userId:'+ user.id)
                println ('accountId:'+newAssociation.accountId)
                println ('newAssociation'+newAssociation)

                newAssociation.save(failOnError:false, flush:true, insert: true)
                if(newAssociation.errors.errorCount >0){
                    response.message = 'Hubo un problema creando la asociacion.'
                    response.status = 500
                    response.account_higyrus = []
                }
            }

        }catch (Exception e){
            response.message = e.message
            return response
        }

        return response
    }

    def getAssociatedAccounts(user){
        def response = ["status":500,"message":"No se pudo buscar las cuentas asociadas.","account_higyrus":[]]

        try{
            def associatedAccounts = findAssociatedAccountsByUserId(user.id)

            associatedAccounts.each{
                def account = higyrusClient.getAccount(it.accountId)
                if(account){
                    response.account_higyrus.add(account)
                }
            }
            if(response.account_higyrus.size() == 0){
                response.message = "No existen cuentas asociadas para ese usuario."
            }else{
                response.message = "Se obtuvieron las cuentas correctamente."
            }
            response.status = 200
        }catch (Exception e){
            return response
        }

        return response
    }

    def removeAssociatedAccount(account,user){
        def response = ["status":500,"message":"No se pudo desasociar la cuenta."]
        if(!account?.account_id ){
            response.message = "Por favor elija una cuenta a la cual desasociar."
            return response
        }

        try{
            def association = findAssociatedAccount(user.id as int,account.account_id as int )
            if(association){

                association.status = 'DISASSOCIATED'
                association.save(failOnError:true, flush: true)
                if(association.errors.errorCount >0){
                    response.message = 'Hubo un desasociando la cuenta.'
                    response.status = 500
                }else{
                    response.status = 200
                    response.message = "Cuenta desasociada."
                }
            }else{
                response.message = 'Esta cuenta no esta asociada a este usuario.'

            }
        }catch(Exception e){
            response.message = e.message
            return response
        }
        return response

    }

    private def findAssociatedAccountsByUserId(userId){
        def results = AssociatedAccount.withCriteria {
            eq('userId',userId)
            eq('status','ASSOCIATED')
        }
        return results
    }

    @Transactional
    private def findAssociatedAccount(int userId,int accountId){
        def results = AssociatedAccount.withCriteria (uniqueResult: true){
            eq('userId',userId)
            eq('accountId',accountId)
            eq('status','ASSOCIATED')
        }
        return results
    }
}
