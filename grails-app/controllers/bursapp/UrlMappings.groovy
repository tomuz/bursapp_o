package bursapp

class UrlMappings {

    static mappings = {
        /*
        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")
        */

        "/"(controller: 'application', action:'index')
        "/ping"(controller: 'ping', action:'ping')

        "/user"( controller: "users",action : "createUser", method: 'POST')
        "/user/$userData"( controller: "users", action: "getUser", method:'GET')

        "405"(view: '/methodNotAllowed')
        "500"(view: '/error')
        "404"(view: '/notFound')
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
    }
}
