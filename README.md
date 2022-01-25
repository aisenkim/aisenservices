# AisenServices

# Description

This is a project to learn about microservices using Spring Boot. 

# Link

1. Link to generate banner.txt
    1. [https://devops.datenkollektiv.de/banner.txt/index.html](https://devops.datenkollektiv.de/banner.txt/index.html)

# Things Learned

### Section: Your First Microservices

1. Creating the top(main) pom file 
    1. Everything under <dependencyManagement> tag will not be imported to children module pom file
2. Setting up docker-compose file for pgAdmin and postgres 
    1. They are linked together through a network 
        
        ![Screen Shot 2022-01-20 at 11.29.19 AM.png](AisenServices%208c179c72ee254882927f7cf140be0956/Screen_Shot_2022-01-20_at_11.29.19_AM.png)
        

### Section: Microservices Communication via HTTP

1. Change port of each microservice under application.yml
2. Add banner.txt file under resources 
3. Ideally, Mircoservice should have separate database (under service, should have separate container name postgres-fraud) but due to short resource, this project will use one  
4. When creating a customer, using RestTemplate, communicate with another micro service (fraud) to check and update fraud db to see if user is fraudulent or not.  

### Section: Service Discovery With Eureka

1. Eureka server acts like load balancer
    
    ![Screen Shot 2022-01-20 at 10.18.19 PM.png](AisenServices%208c179c72ee254882927f7cf140be0956/Screen_Shot_2022-01-20_at_10.18.19_PM.png)
    
    ** Eureka Server later can be replaced by using k8s
    
2. Always remember to name the application name inside application.yml file 
    1. The application name is shown on the eureka dashboard
3. Running multiple instances of an application from intellij
    1. First click run configuration
    2. copy and paste the application and rename it
    3. Program arguments: —server.port=8085
4. Inside customer service file, change the url for RestComponent to communicate to Fraud microservice to `"http://FRAUD/api/v1/fraud-check/{customerId}",` replacing [localhost:8081](http://localhost:8081) → FRAUD 
    1. Changing to application name so that Eureka Server can handle it

1. Without @LoadBalanced, RestTemplate is confused about which instance of Fraud to run
    
    ![Screen Shot 2022-01-21 at 12.45.14 AM.png](AisenServices%208c179c72ee254882927f7cf140be0956/Screen_Shot_2022-01-21_at_12.45.14_AM.png)
    
    1. Using round robin to alternate between multiple instances

### Section: Open Feign

1. Remove RestController and configuration 
2. Don’t need RestController from Customer Service but instead use a Feign Client (interface) to make request to different services
3. Reduces redundancy especially with dto(requests and responses)
