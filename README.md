This is simple API create with support of Spring Boot, Maven and Java.

### To run application follow this steps:
 1. Install MySQL (if you have it already ignore this step)
 2. Create database BlazeDemo
 3. In file src/main/resources/application.yml fill in username and password with credentials of your DB
 4. Execute src/main/resources/add_tables.sql file to create tables with test data
 5. In command line cd to project root and execute: mvn clean install -DskipTests
 6. Execute from command line via: java -jar target/SHC-0.0.1-SNAPSHOT.jar command
 7. Notes regarding killing a running Tomcat process on Windows
    7.1) Go to (Open) Command Prompt (Press Window + R then type cmd Run this).
    7.1) Run following commands for all listening ports: netstat -aon | find /i "listening"
         Apply port filter: netstat -aon | find /i "listening" | find "8080"
    7.3) Finally with the PID we can run the following command to kill the process 
         Copy PID from result set: taskkill /F /PID
         Ex: taskkill /F /PID 189
 
### To run tests follow this steps:
 1. Open terminal
 2. Execute: ./mvn test
 
### Test categories:
 1. Unit tests (src/test/com/demo/unit)
 2. Rest tests (src/test/com/demo/rest)
 3. Bdd tests (src/test/com/demo/bdd)
