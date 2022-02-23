# Student Fee Management

### Here we demonstrate a simple student fee management system comprising of spring-boot microservices

#### Important Spring modules used:
- SAGA Choreography
- Spring Kafka
- Spring Cloud Stream
- Reslience4j Retry
- Caching
- OpenAPI 3.0

##Requirements:
- Java 8
- Apache Kafka (2.4 or above from https://kafka.apache.org/downloads)

## Steps to Run:
- Import as Maven Project into your favorite IDE (Intellij IDEA/Eclipse)
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