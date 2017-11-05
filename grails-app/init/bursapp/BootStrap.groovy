package bursapp

import grails.util.Environment
import grails.core.GrailsApplication


class BootStrap {
    GrailsApplication grailsApplication
    def init = { servletContext ->

        def result = '################## running in UNCLEAR mode.'
        println "Application starting ... "
        switch (Environment.current) {
            case Environment.DEVELOPMENT:
                result = 'now running in DEV mode.'
                seedTestData()
                break;
            case Environment.TEST:
                result = 'now running in TEST mode.'
                break;
            case Environment.PRODUCTION:
                result = 'now running in PROD mode.'
                break;
        }
        println "current environment: $Environment.current"
        println "$result"
    }

    def destroy = {
    }

    private void seedTestData() {
        def user = null
        println "Start loading users into database"
        //user = new User(username: 'tomuz', password:'123', dateCreated: new Date(), lastUpdated: new Date() )
        //assert user.save(failOnError:true, flush:true, insert: true)
        //user.errors = null

       // println "Finished loading $User.count users into database"
    }
}
