
```
ATM demo project
```

endpoints: 
  /withdraw:
    method: GET
    params: accountNumber, PIN, amount
    response: list of bills that add up to amount
    errors: invalid account(401)
            wrong PIN(401)
            missing params(422)
            invalid amount(422)
  
  /deposit:
    method: PUT
    params: accountNumber, PIN, list of bills
    response: amount added to account
    errors: invalud account(401)
            wrong PIN(401)
            amount too low(422)
            fake bill(eg 3 dollar bill)(422)
            empty bill list(422)
    
  /consult:
    method: GET
    params: accountNumber, PIN
    response: current balance
    errors: invalid account(401)
            wrong PIN(401)
            missing param(422)
    
logging:
  wrong PIN
  fake bill
  amount added to account number
  amount left in ATM after withdrawal
  less than X money left in ATM
  accountNumber consulted
  
tests:
  /withdraw, /deposit, /consult result in error if invalid accountNumber/ wrong PIN (403)
  missing parameters result in error
  empty lists cannot be used for /deposit
  fake bills on /deposit result in error
  decomposition of a number into a list of bills works correctly
  decomposition of a number into a list of bills only accepts valid numbers (eg >=50; whole numbers)
  E2E:
    list of bills gotten from /withdraw adds up to initial amount
    amount before deposit + amount deposited adds up to amount after deposit
  
