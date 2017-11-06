/**
 * Created by tbuchaillot on 5/11/17.
 */

'/higyrus/account/1' {
    status = 200
    error = []
    json = """
            {
                "account_id":1,
                "username":"higyrus_1",
                "password":"123123",
                "bank_accounts":
                    [
                        {
                            "id":"1112",
                            "number":"C45661"
                        },
                        {
                            "id":"1113",
                            "number":"B45662"
                        },
                        {
                            "id":"1114",
                            "number":"A45664"
                        }
                    ]
            }
    """
}

'/higyrus/account&body=[username:higyrus_1, password:123123]' {
    status = 200
    error = []
    json = """
            {
                "account_id":1,
                "username":"higyrus_1",
                "password":"123123",
                "bank_accounts":
                    [
                        {
                            "id":"1112",
                            "number":"C45661"
                        },
                        {
                            "id":"1113",
                            "number":"B45662"
                        },
                        {
                            "id":"1114",
                            "number":"A45664"
                        }
                    ]
            }
    """
}


'/higyrus/account/2' {
    status = 200
    error = []
    json = """
            {
                "account_id":2,
                "username":"higyrus_2",
                "password":"123123",
                "bank_accounts":
                    [
                        {
                            "id":"1112",
                            "number":"C45661"
                        },
                        {
                            "id":"1113",
                            "number":"B45662"
                        },
                        {
                            "id":"1114",
                            "number":"A45664"
                        }
                    ]
            }
    """
}

'/higyrus/account&body=[username:higyrus_2, password:123123]' {
    status = 200
    error = []
    json = """
            {
                "account_id":2,
                "username":"higyrus_2",
                "password":"123123",
                "bank_accounts":
                    [
                        {
                            "id":"1112",
                            "number":"C45661"
                        },
                        {
                            "id":"1113",
                            "number":"B45662"
                        },
                        {
                            "id":"1114",
                            "number":"A45664"
                        }
                    ]
            }
    """
}


'/higyrus/account/3' {
    status = 200
    error = []
    json = """
            {
                "account_id":3,
                "username":"higyrus_3",
                "password":"123123",
                "bank_accounts":
                    [
                        {
                            "id":"1112",
                            "number":"T0M129C"
                        },
                        {
                            "id":"1113",
                            "number":"3SK031"
                        },
                        {
                            "id":"1114",
                            "number":"99P4T01"
                        }
                    ]
            }
    """
}

'/higyrus/account&body=[username:higyrus_3, password:123123]' {
    status = 200
    error = []
    json = """
            {
                "account_id":3,
                "username":"higyrus_3",
                "password":"123123",
                "bank_accounts":
                    [
                        {
                            "id":"1112",
                            "number":"T0M129C"
                        },
                        {
                            "id":"1113",
                            "number":"3SK031"
                        },
                        {
                            "id":"1114",
                            "number":"99P4T01"
                        }
                    ]
            }
    """
}

'/higyrus/funds'{
    status = 200
    error = []
    json = """
        {
            "active_funds":
                [
                    {
                        "id":1,
                        "name":"CAFCI 1542"
                    },
                    {
                        "id":2,
                        "name":"CAFCI 803"
                    }
                ]
        }
    """
}

'/higyrus/funds/1'{
    status = 200
    error = []
    json = """

        {
            "id":1,
            "name":"CAFCI 1542"
        }

    """
}
'/higyrus/funds/2'{
    status = 200
    error = []
    json = """

        {
            "id":2,
            "name":"CAFCI 803"
        }

    """
}

'/' {
    status = 200
    error = []
    json = """
            {
            }
    """
}