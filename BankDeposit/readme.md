Bank deposits

REST service
1. Request Method GET:
    - search bank deposit by "id" -> http://<name_of_host:port>/deposits/{depositId}
        example - search id=1: http://localhost:8080/deposits/1
    - search bank deposit by "name" -> http://<name_of_host:port>/deposits/{depositName}
        example - search name=depositName1: http://localhost:8080/deposits/depositName1
    - get full list of deposits -> http://<name_of_host:port>/deposits/

    - search bank depositor by "id" -> http://<name_of_host:port>/depositors/{depositorId}
         example - search id=1: http://localhost:8080/depositors/1
    - search bank depositor by "name" -> http://<name_of_host:port>/depositors/{depositorName}
         example - search name=depositorName1: http://localhost:8080/depositors/depositorName1
    - get full list of depositors -> http://<name_of_host:port>/depositors/
    - search bank depositor by "id" of deposit -> http://<name_of_host:port>/depositors/deposit/{depositIdDeposit}
        example - search all depositors with deposit id=1: http://localhost:8080/depositors/deposit/1
    - search bank depositor between dates of open deposit
            -> http://<name_of_host:port>/depositors/date/{startDate}/{endDate}
        example - search all depositors between dates of open deposit
                  from "2014-01-01" to "2014-12-01": http://localhost:8080/depositors/date/2014-01-01/2014-12-01
    - search bank depositor between dates of the alleged liquidation or full liquidation deposit
             -> http://<name_of_host:port>/depositors/date/return/{startDate}/{endDate}
        example - search all depositors between dates of the alleged liquidation deposit
                  from "2014-01-01" to "2014-12-01": http://localhost:8080/depositors/date/return/2014-01-01/2014-12-01

2. Request Method POST:
    - add bank deposits -> http://<name_of_host:port>/deposits/
        example: {"depositName":"depositName1","depositMinTerm":12,"depositMinAmount":100,"depositCurrency":"usd","depositInterestRate":4,"depositAddConditions":"condition1"}
    - add bank depositors -> http://<name_of_host:port>/depositors/
        example: {"depositorName":"depositorName1","depositorIdDeposit":1,"depositorDateDeposit":"2014-12-01","depositorAmountDeposit":10000,"depositorAmountPlusDeposit":1000,"depositorAmountMinusDeposit":11000,"depositorDateReturnDeposit":"2014-12-01","depositorMarkReturnDeposit":0}
3. Request Method PUT:
    - modify the existing data deposits -> http://<name_of_host:port>/deposits/
        example: {"depositId":1,"depositName":"depositName1","depositMinTerm":12,"depositMinAmount":100,"depositCurrency":"usd","depositInterestRate":4,"depositAddConditions":"condition1"}
    - modify the existing data depositors -> http://<name_of_host:port>/depositors/
        example: {"depositorId":1,"depositorName":"update","depositorIdDeposit":1,"depositorDateDeposit":"2014-12-01","depositorAmountDeposit":10000,"depositorAmountPlusDeposit":1000,"depositorAmountMinusDeposit":11000,"depositorDateReturnDeposit":"2014-12-01","depositorMarkReturnDeposit":0}
4. Request Method DELETE:
    - delete the information about deposits http://<name_of_host:port>/deposits/{depositId}
        example - delete deposit with id=1: http://localhost:8080/deposits/1
    - delete the information about depositors http://<name_of_host:port>/depositors/{depositorId}
            example - delete depositor with id=1: http://localhost:8080/depositors/1
    - delete the information about depositors by id deposit http://<name_of_host:port>/depositors/deposit/{depositId}
                example - delete depositor by deposit with id=1: http://localhost:8080/depositors/deposit/1

WEB service:
1.  Start -> url: http://<name_of_host:port>/depositsList
2.  If the database is empty redirect service at -> url: http://<name_of_host:port>/inputFormDeposit
3.  After entering information about the bank deposit service again redirected
    to the start page -> url: http://<name_of_host:port>/depositsList
4.  Then you can:
        (table data for bank deposits)
    - enter data on new bank deposits (button "ADD DEPOSIT")
    - modify the existing data (the button "UPDATE")
    - delete the information entered (the button DELETE)
    - enter data on new bank depositors (button "ADD DEPOSITOR_(index)")

        (table data for bank depositors)
    - modify the existing data (the button "UPDATE")
    - delete the information entered (the button DELETE)
    - filter the data by:
        - dates creation of bank deposit
        - dates of the alleged liquidation of bank deposit
