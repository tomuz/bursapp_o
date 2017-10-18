package bursapp.clients

/**
 * Created by tbuchaillot on 16/10/17.
 */
class HigyrusClient {

    static List mockedAccounts = [
            ["account_id":1, "username":"higyrus_1","password":"123123"],
            ["account_id":2, "username":"higyrus_2","password":"123123"],
            ["account_id":3, "username":"higyrus_3","password":"123123"]
    ]


    def validateAccount(accountData){
        def response = ["status":400,"response":null]
        try{
            def check  = mockedAccounts.find {it['username'] == accountData.username && it['password'] == accountData.password}
            if(check){
                response.status = 200
                response.response = check
            }else{
                response.status = 404
                response.response = []
            }
        }catch (Exception e){
            return response
        }
        return response
    }

    def getAccount(accountId){
        def response = ["status":400,"response":null]
        try{
            def check  = mockedAccounts.find {it['account_id'] == accountId}
            if(check){
                response.status = 200
                response.response = check
            }
        }catch (Exception e){
            return response
        }
        return response
    }
}
