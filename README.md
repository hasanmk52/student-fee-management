# Student Fee Management

#### Here we demonstrate a simple student fee management system comprising of spring-boot microservices

#### Highlights:
- SAGA Choreography
- Spring Kafka
- Spring Cloud Stream
- Spring Webflux
- Resilience-4j
- Caching
- OpenAPI 3.0

<ins>**Student Service**</ins>: Maintains Student information and provision to ADD/UPDATE student.

<ins>**Order Service**</ins>: Create a fee order request and fetch order info. Also Contains lookup information for Grade fees.  

<ins>**Payment Service**</ins>: Create a user account(basic account and balance info) for the student. Also contains API to fetch Payment Receipt Info.

###Minimum Requirements:
- Java 8
- Apache Kafka 2.8 or above (Download from https://kafka.apache.org/downloads)

### Steps to Run:
- Import as Existing Maven Project into your favorite IDE (Intellij IDEA/Eclipse)
- Start kafka cluster locally
    - After downloading copy the kafka directory to desired location.
    - Then inside the kafka directory run the below two commands in separate terminal windows
  ```bash
     $ bin/zookeeper-server-start.sh config/zookeeper.properties
     $ bin/kafka-server-start.sh config/server.properties
    ```
- Run the individual services in order (Student Service, Order Service, Payment Service) in order in the IDE as Spring boot applications or run mvn clean package and run the services as standalone jars
- Refer the Postman Test Collections to Test the APIs 

### OpenAPI Swagger Links:

- http://localhost:8081/student-service/swagger-ui-student.html
- http://localhost:8082/order-service/swagger-ui-order.html
- http://localhost:8083/payment-service/swagger-ui-payment.html

### H2 DB Console Links:

- http://localhost:8081/student-service/h2-console
- http://localhost:8082/order-service/h2-console
- http://localhost:8083/payment-service/h2-console