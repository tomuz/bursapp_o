package bursapp.clients

import grails.config.Config

/**
 * Created by tbuchaillot on 16/10/17.
 */
class HigyrusClient {

    def restService
    def higyrusUrl
    static List mockedAccounts = [
            ["account_id":1, "username":"higyrus_1","password":"123123","bank_accounts":[["id":"1112","number":"C45661"],["id":"1113","number":"B45662"],["id":"1114","number":"A45664"]]],
            ["account_id":2, "username":"higyrus_2","password":"123123","bank_accounts":[["id":"1115","number":"CC5661"],["id":"1116","number":"BA5662"],["id":"1117","number":"AT5664"]]],
            ["account_id":3, "username":"higyrus_3","password":"123123","bank_accounts":[["id":"110","number":"T0M611"],["id":"109","number":"3SK0123"],["id":"100","number":"A4999"]]]
    ]


    def validateAccount(accountData){
        def response = ["status":400,"response":null]
        try{
            /*
            def check  = mockedAccounts.find {it['username'] == accountData.username && it['password'] == accountData.password}

            if(check){
                response.status = 200
                response.response = check
            }else{
                response.status = 404
                response.response = []
            }
            */
            def jsonData = ['username':accountData.username, 'password':accountData.password]
            response.status = 200
            response.response = restService.postData(higyrusUrl+'account',jsonData)
        }catch (Exception e){
            return response
        }
        return response
    }

    def getAccount(accountId){
        def response = ["status":400,"response":null]

        try{
            /*def check  = mockedAccounts.find {it['account_id'] == accountId}
            if(check){
                response.status = 200
                response.response = check
            }*/
            response.status = 200
            response.response = restService.get(higyrusUrl+'account/'+accountId)
        }catch (Exception e){
            println 'exception ::'+e
            return response
        }
        return response
    }

    def getAllAvailableMoney(fundId,bankAccountId){
        def money = 0

        try{

            def jsonData = ['fundId':fundId, 'bankAccountId':bankAccountId]

            def data = restService.postData(higyrusUrl+'getAvailableMoney',jsonData)
            money = data?.money ?: 0
        }catch (Exception e){
            println 'exception ::'+e
            return money
        }
        return money
    }
}
