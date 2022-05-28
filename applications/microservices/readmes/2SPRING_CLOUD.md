## Components of Spring Cloud
**Components of Spring Cloud** => provide solutions to the challenges of microservices we discussed

1. **Configuration Management** => _Spring Cloud Config Server_

    - In microservice architecture, we have multiple microservices, multiple environments for each of these microservices and multiple instances in many of those environments. Therefore, there would be a lot of configuration for these microservices that the operations team needs to manage.
    
    - _Spring Cloud Config Server_ => provides an approach where you can store all the configuration for all the different environments of all the microservices in a GIT repository (centralised location).

   ![x](/images/im1.png)

    -  This helps us keep the configuration in one place and that makes it very easy to maintain the configuration for all microservices. 
