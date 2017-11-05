import Exceptions.APIException
import grails.converters.JSON

/**
 * Created by tbuchaillot on 5/11/17.
 */

class RestService {

    def restClient

    def postData(uri, jsonData) throws APIException {

        def jsonResult


        restClient.post(uri: uri.toString(),
                data: jsonData,
                headers: [ "Encoding" : "UTF-8", "Content-Type" : "application/json"],
                success: { jsonResult = it.data },
                failure: {
                    def errorMsg = "Error on POST to URI: [${uri}], Data: ${jsonData as JSON}, Reason: [${it?.properties}]\n"
                    throw new APIException(errorMsg.toString() , it.exception)
                })


        return jsonResult
    }

    def get(uri) {
        get(uri, [:])
    }

    def get(uri, params) {
        def jsonResult

        uri = addParametersToURI( uri, params )
        try {
            restClient.get(
                    uri: uri,
                    headers: [ "Encoding" : "UTF-8", "Content-Type" : "application/json"],
                    success: { jsonResult = it.data },
                    failure: { jsonResult = it.data }
            )
        } catch (Exception exception) {

        }

        return jsonResult
    }



    def putData(uri, jsonData) throws APIException {

        def jsonResult


        restClient.put(uri: uri.toString(),
                data: jsonData,
                headers: [ "Encoding" : "UTF-8", "Content-Type" : "application/json"],
                success: { jsonResult = it.data },
                failure: {
                    def errorMsg = "Error on PUT to URI: [${uri}], Data: ${jsonData as JSON}, StatusCode: [${it?.status?.statusCode}], Reason: ${it?.data}\n"

                    if (it.exception) {
                        throw new APIException(errorMsg.toString() , it.exception)
                    } else {
                        throw new APIException(errorMsg.toString())
                    }
                })


        return jsonResult
    }

    def private addParametersToURI( uri, params ) {
        if(!params || params.isEmpty())
            return uri

        uri += "?"
        params.each { k, v ->
            uri += (k+"="+v+"&")
        }

        uri = uri.substring(0,uri.size()-1)

        return uri
    }

}