import bursapp.AccountsService
import bursapp.ConsultasService
import bursapp.MessagesService
import bursapp.OperationsService
import bursapp.UsersService
import bursapp.clients.HigyrusClient
import bursapp.clients.LocalRestClient
import grails.config.Config

// Place your Spring DSL code here
beans = {


    restClient(LocalRestClient){
    }
    restService(RestService) {
        restClient = ref("restClient")
    }
    higyrusClient(HigyrusClient){
        higyrusUrl = grailsApplication.config.getProperty('higyrus.baseUrl')
        restService = ref('restService')
    }

    usersService (UsersService) {}

    messagesService (MessagesService){}

    accountsService (AccountsService){
        higyrusClient = ref('higyrusClient')
    }

    consultasService(ConsultasService){
        messagesService = ref('messagesService')
    }

    operationsService (OperationsService){
        higyrusUrl = grailsApplication.config.getProperty('higyrus.baseUrl')
        restService = ref('restService')
        messagesService = ref('messagesService')
    }


}
