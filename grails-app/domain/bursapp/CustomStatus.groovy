package bursapp

/**
 * Created by tbuchaillot on 6/11/17.
 */
class CustomStatus {
    int id
    int bankAccountId
    int userId
    String name
    Boolean available
    Date insertDate
    static mapping = {
        id column: "id"
        userId column: "user_id"
        bankAccountId column: "bank_account_id"
        name column: "name"
        available column: "available"
        insertDate column: "insert_date", sqlType:"datetime"
        version false
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
