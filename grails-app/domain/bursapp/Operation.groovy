package bursapp

/**
 * Created by tbuchaillot on 5/11/17.
 */

class Operation {

    int id
    int fundId
    int userId
    int bankAccountId
    String type
    String status
    Double amount
    Date insertDate
    Date lastUpdate
    int operatorId

    static mapping = {
        id column: "id"
        userId column: "user_id"
        bankAccountId column: "bank_account_id"
        fundId column: "fund_id"
        operatorId column: "operator_id"
        amount column: "amount"
        status column: "status"
        type column: "type"
        insertDate column: "insert_date", sqlType:"datetime"
        lastUpdate column: "last_update", sqlType:"datetime"
        version false
    }

    static constraints = {

    }
    public Map asMap() {
        def map = [:] as HashMap
        this.class.getDeclaredFields().each {
            if (it.modifiers == java.lang.reflect.Modifier.PRIVATE) {
                if (it.name != 'org_grails_datastore_gorm_GormValidateable__errors'){
                    map.put(it.name, this[it.name])
                }
            }
        }
        return map
    }

}
