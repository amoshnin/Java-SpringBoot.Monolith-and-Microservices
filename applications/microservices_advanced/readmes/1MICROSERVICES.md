## FAQ
- _Why are microservices needed?_
- _What are the challenges associated with microservices?_
- _How does Spring Cloud help us solve the challenges associated with microservices?_
## Topics
- Spring Cloud Config Server and Bus
- Implement (Load Balancing) with Ribbon and Feign
- Implement (Naming Server) with Eureka
- Implementing (API Gateway) with Zuul (to get more visibility into the microservices)
- Implement (Distributed Tracing) with Zipkin (to get more visibility into the microservices)
- Make microservices (Fault Tolerant) with Hystrix
## Theory
### Definition of Microservices
**Microservices** = is an architectural style that structures an application as a collection of services that are: 
- Highly maintainable and testable. 
- Loosely coupled. 
- Independently deployable. 
- Organized around business capabilities.
### Challenges with building Microservices
1. **Bounded Context**
  
    - Instead of a one big monolith application, we're going to build 5/10/20/more small microservices all of which together make up our web-service. 

   - Challenge consists of identifying the boundaries of which of these microservices - what (to do and what not to do) in each of these microservices.

   - For new applications, this is especially difficult. Deciding boundaries of microservices is an evolutionary process, that you don't usually get right the first time

   - Instead, as you keep gaining more domain/business knowledge, you should use it to improve the boundaries of your microservices.

2. **Configuration management**

    - In microservice architecture, we usually have 5-50 different microservices. Each of these microservices have multiple instances in each environment and there are multiple environments. 

    - Let's say there are 10 microservices with 5 environments and 50 instances. There is going to be tons of configuration, hence a lot of work for the operations team.

3. **Dynamic (scale up and scale down)**

    - Loads on different microservices will be different at different instances of time. For example, at a particular point in time I might need 2 instances of microservice2, but later, at a different point in time I might need 10 instances of microservice2. 

    - So, I require a technology that would be able to (bring new instances of microservices up) and (bring down older instances of microservices) when they are not needed at that time.

    - In addition, I also require dynamic load balancing because we want to distribute the load between all the instances of each microservice such that each instance is being used to its fullest extent. 

    - Say, two new instances of microservice2 are coming up, I would want to ensure that all the new ones are also being used to their fullest extent dynamically as new instances come up.

    - So, we need the ability to (dynamically bring in new instances and bring old instances down - scale up and scale down) and (dynamically distribute the load amongst all active instances)

4. **Visibility**

    - If I say that the functionality of our web-service is now distributed amongst 10 microservices, and there is a bug. How do you identify which microservice has the bug? 

    - We need a centralised log where I can go and find out what happened for a specific request - which microservice caused the problem?

    - We also need monitoring around these microservices. Since we have hundreds of microservices, we need to be able to identify the microservices that are (down/don't have enough disk space/...)

    - Instead, as you keep gaining more domain/business knowledge, you should use it to improve the boundaries of your microservices.

   - Simple request might involve 10 microservices, how do I determine which microservice was the cause for a defect. How do I know if all my microservices are up and running?

5. **Pack of cards**

    - In microservice architecture you have one microservice calling another microservice, then another calling another and etc. If microservice architecture is not well-designed, there would be certain microservices which be fundamental to the entire system - if that microservice goes down, then the entire application can go down.  

    - Therefore, it's very important for you to have fault tolerance in your microservices.

   - How do I prevent one microservice being down, taking down the entire application - build fault tolerance into my microservices?