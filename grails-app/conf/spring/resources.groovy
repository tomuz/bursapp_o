import bursapp.AccountsService
import bursapp.UsersService
import bursapp.clients.HigyrusClient

// Place your Spring DSL code here
beans = {
    higyrusClient(HigyrusClient){}

    usersService (UsersService) {}

    accountsService (AccountsService){
        higyrusClient = ref('higyrusClient')
    }


}
