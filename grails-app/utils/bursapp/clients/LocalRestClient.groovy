package bursapp.clients

import grails.util.Holders
import Exceptions.NotFoundException
import grails.converters.JSON

/**
 * Created by tbuchaillot on 5/11/17.
 */
class LocalRestClient {

    static transactional = false


    void get(Map<String,?> args){
        //tomo los argumentos mockeados
        def mockData = getMockData(args)
        //ejecuto las acciones segun los datos obtenidos
        executeClosures(mockData,args)
    }

    void post(Map<String,?> args){
        //tomo los argumentos mockeados
        def mockData = getMockData(args)
        //ejecuto las acciones segun los datos obtenidos
        executeClosures(mockData,args)
    }

    void put(Map<String,?> args){
        //tomo los argumentos mockeados
        def mockData = getMockData(args)
        //ejecuto las acciones segun los datos obtenidos
        executeClosures(mockData,args)
    }

    void delete(Map<String,?> args){
        //tomo los argumentos mockeados
        def mockData = getMockData(args)
        //ejecuto las acciones segun los datos obtenidos
        executeClosures(mockData,args)
    }


    def getMockData(args){
        def uri = args.uri
        def body = args.data
        def originUri = uri

        uri = body?uri+"&body=$body":uri
        println( "MOCK uri: $uri")
        def result = Holders.config.get(uri)
        def exists = result?.status

        if(exists){
            def json = result.json
            def error = result.error
            def status = result.status
            return ["status":status, "data":json, "error":error]
        }else {
            if(body) {
                //busco en la urls con comodin
                //un argumento a la vez
                for(e in body) {
                    def bodyAux = body.clone()
                    bodyAux[e.key] = "*"
                    uri = originUri +"&body=$bodyAux"
                    println( "MOCK uri with wildcard: $uri")
                    result = Holders.config.get(uri)
                    exists = result?.status
                    if(exists){
                        def json = result.json
                        def error = result.error
                        def status = result.status
                        return ["status":status, "data":json, "error":error]
                    }
                }

                uri = originUri + "&body=?"
                result = Holders.config.get(uri)
                exists = result?.status
                println( "MOCK uri with wildcard: $uri")
                if(exists){
                    def json = result.json
                    def error = result.error
                    def status = result.status
                    return ["status":status, "data":json, "error":error]
                }
            }else{
                //busco alguna uri con wildcard que matchee, se corta al final
                String uriWithWildcard = uri
                while(uriWithWildcard.lastIndexOf("/") > 0){
                    String auxUri = uriWithWildcard.substring(0,uriWithWildcard.lastIndexOf("/"))+"/*"
                    println( "MOCK uri with wildcard: $auxUri")
                    result = Holders.config.get(auxUri)
                    exists = result?.status
                    if(exists){
                        def json = result.json
                        def error = result.error
                        def status = result.status
                        return ["status":status, "data":json, "error":error]
                    }else {
                        uriWithWildcard = uriWithWildcard.substring(0,uriWithWildcard.lastIndexOf("/"))
                    }
                }
            }
        }
        uri = body?originUri+"&body=$body":uri
        throw new NotFoundException("Not Found uri $uri")
    }

    def executeClosures(mockData, args){
        Closure success = 	args.success
        Closure failure = 	args.failure
        //segun el status muestro la correpondiente respuesta
        //if (mockData.status==200){
        if (!mockData.error){
            def json = mockData.data
            def jsonResult = JSON.parse(json)
            //def it = ["data":jsonResult]
            def it = ["data": jsonResult, "status": [statusCode: mockData.status]]
            success(it)

        }else{

            def error = mockData.error
            String statusLine = error.status.toString()
            error.status=statusLine
            def it = error
            failure(it)
        }
    }
}
