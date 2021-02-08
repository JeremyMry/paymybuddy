# paymybuddy

RUN THE APP: 



ENDPOINTS:

There is 3 endpoints available for anyone without being authenticate:

POST "/api/auth/signup" --> request type: JSON {"firstName": "john", "lastName": "doe","username": "jdoe", "email": "jdoe@fakemail.com","password": "pwd" }
POST "/api/auth/signin" --> request type: JSON {"usernameOrEmail": "jdoe", "password": "pwd"}
GET "/api/user/findUser" --> request type: requestParam ?email=emailoftheuser



