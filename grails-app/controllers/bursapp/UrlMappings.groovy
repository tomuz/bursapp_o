package bursapp

class UrlMappings {

    static mappings = {


        "/"(controller: 'application', action:'index')
        "/ping"(controller: 'ping', action:'ping')

        "/user"( controller: "users",action : "createUser", method: 'POST')
        "/user/$userData"( controller: "users", action: "getUser", method:'GET')
        "/user/login"( controller: "users", action: "login", method: 'POST')
        "/user/logout"(controller: "users", action: "logout", method: 'POST')


        "/operator"( controller: "operators",action : "createOperator", method: 'POST')
        "/operator/$opertadorData"( controller: "operators", action: "getOperator", method:'GET')
        "/operator/login"( controller: "operators", action: "login", method: 'POST')
        "/operator/logout"(controller: "operator", action: "logout", method: 'POST')

        "/user/associateAccount" (controller: "accounts", action: "associateAccount", method: 'POST')
        "/user/getAssociatedAccounts"(controller: "accounts", action: "getAssociatedAccounts", method: 'POST')
        "/user/removeAssociatedAccount"(controller: "accounts", action: "removeAssociatedAccount", method: 'POST')

        "/operations/getFunds"(controller:"operations", action: "getFunds", method: 'POST')
        "/operations"(controller:"operations", action: "createOperation", method: 'POST')

        "/operations/all"(controller:"operations", action: "getAllOperations", method: 'POST')
        "/operations/lasts"(controller:"operations", action: "getLastsOperations", method: 'POST')
        "/operations/pending"(controller:"operations", action: "getPendingOperations", method: 'POST')
        "/operations/date"(controller:"operations", action: "getOperationsByDate", method: 'POST')

        "/messages"(controller:"messages", action: "getMessages", method: 'POST')

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
