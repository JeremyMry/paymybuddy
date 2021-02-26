# paymybuddy

RUN THE APP:

Setup the project in Intellij:

Select "Get From VCS"
Once the project is loaded, click on "add configuration" (top right of the window)
Click on the + icon in the new window
Choose the correct SDK (AdoptOpenJDK11)
Choose the main class (com.paymybuddy.app.PaymybuddyApplication)
Name the config then click on "apply"
PS: Git must be installed and your Github account linked with Intellij

Setup the correct informations about your db into application.properties file (money_transfer/src/main/resources/application.properties):

com.mysql.cj.jdbc.Driver-class-name="yourdriver" (mariadb can also be used)
spring.datasource.url="yoururl"
spring.datasource.username="yourusername"
spring.datasource.password="yourpassword"

Create the database and the tables using the db-scheme.sql file

ENDPOINTS:

There is 3 endpoints available for anyone without being authenticate:

POST "/api/auth/signup" --> request type: JSON {"firstName": "john", "lastName": "doe","username": "jdoe", "email": "jdoe@fakemail.com","password": "pwd" }

POST "/api/auth/signin" --> request type: JSON {"usernameOrEmail": "jdoe", "password": "pwd"}

GET "/api/user/findUser" --> request type: requestParam ?email=emailOfTheUserYouWantToFind

Others endpoints are:

!! You can only access them after you're logged. When you perform the loggin procedure you'll received a token, you must put use it in postman in the "Authorization window". Choose the "bearer token" option and copy the token there.

GET "/api/user/me"

PUT "/api/user/email" --> request type: String / youremail

PUT "/api/user/password" --> request type: String / yournewpassword

PUT "api/user/wallet/add" --> request type: BigDecimal / "150.00" or "150" / must be superior to 0

PUT "api/user/wallet/remove" --> request type: BigDecimal / "150.00" or "150" / must be superior to 0

GET "api/contact/all"

POST "api/contact/create" --> request type: JSON { "email": "contactemail", "firstName": "contactfirstname"}

PUT "api/contact/put" --> request type: JSON { "id": contactId, "oldFirstName": "contactFirstName", "newFirstName": "newContactFirstName"}

DELETE "api/contact/delete" --> request type Long / contactId

GET "api/transaction/made"

GET "api/transaction/received"

POST "api/transaction/create" --> request type: JSON { "creditor": userId, "reference": "yourReference", "amount": yourAmount }
