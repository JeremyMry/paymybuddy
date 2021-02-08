# paymybuddy

RUN THE APP: 



ENDPOINTS:

There is 3 endpoints available for anyone without being authenticate:

POST "/api/auth/signup" --> request type: JSON {"firstName": "john", "lastName": "doe","username": "jdoe", "email": "jdoe@fakemail.com","password": "pwd" }

POST "/api/auth/signin" --> request type: JSON {"usernameOrEmail": "jdoe", "password": "pwd"}

GET "/api/user/findUser" --> request type: requestParam ?email=emailOfTheUserYouWantToFind

Others endpoints are: 

GET "/api/user/me"

PUT "/api/user/email"

PUT "/api/user/password"*

PUT "api/user/wallet/add"

PUT "api/user/wallet/remove"

GET "api/contact/all"

POST "api/contact/create"

PUT "api/contact/put"

DELETE "api/contact/delete"

GET "api/transaction/made"

GET "api/transaction/received"

POST "api/transaction/create"
