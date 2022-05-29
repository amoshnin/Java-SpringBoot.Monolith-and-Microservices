## Application structure Overview

We're building a complex disitrubted system => with (mutliple microservices, naming server, api gateway, distributed tracing, ...)

Overall structure of the project:

![x](../images/im6.png)

1. Microservice 1: (first Spring Boot Application) Currency Exchange Microservice => what is the exchange rate of one currency to another

   - From this microservice we will be exposing a simple URL:

   ![x](../images/im5.png)

2. Microservice 2: (second Spring Boot Application) Currency Conversion Microservice => convering 10 USD into INR

   - From this microservice we will be exposing a simple URL:

   ![x](../images/im7.png)

   - For the (Currency Conversion Microservice) to provide its featurs, it will call the (Currency Exchange Microservice)

   - It will ask what is the value of USD in INR today?

   - Then it would take the return value and multiply it with 10 (based on our request to convert 10 USD into INR).

## Load Balancing

- Later, we will have multiple instances of (CurrencyConversionMicroservice) and (CurrencyExchangeMicroservice)
- When calling from (CurrencyConversionMicroservice), I would need to know:
  - which instance of the (CurrencyExchangeMicroservice) is providing the response
  - is instance 1 of (CurrencyExchangeMicroservice) runnnig on port 8000 or 8001 or 8002
- That would help us identify whether our load balancers and naming servers are working properly

  ![x](../images/im8.png)

### Running multiple instances of microservice = is same as running the same SpringBoot app on different ports

This can be achieved by, going into editing Run/Debug Configurations in InteliJ.

1. You copy the application
2. Rename it, so:

   - first application => CurrencyExchangeServiceApplication 8000
   - second application => CurrencyExchangeServiceApplication 8001

     Configure this 2nd application to run on port 8001. Click Modify options -> Click Add VM options -> Paste -Dserver.port=8001

     Whatever you provide here as an environment variable, it would override whatever is configured in application.properties

We can return the port details to our client by @Autowired the Environment class provided by SpringBoot in any of our controllers:

```
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(path="currency-exchange")
public class CurrencyExchangeController {

    @Autowired
    private Environment environment;

    @GetMapping("from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        CurrencyExchange currencyExchange = new CurrencyExchange(1000L, from, to, BigDecimal.valueOf(50));
        String port = this.environment.getProperty("local.server.port");
        currencyExchange.setEnvironment(port);
        return currencyExchange;
    }
}
```

## Calling one microserivce (CurrencyExchangeMicroservice) from another microservice (CurrencyConversionMicroservice)

- This is done using _RestTemplate_ which allows us to make REST API calls.

- Below is a code for a controller of the 'CurrencyConversionMicroservice' microservice in which a method is calling the 'CurrencyExchangeMicroservice'

```
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@RequestMapping(path="currency-conversion")
public class CurrencyConversionController {
    @GetMapping(path="from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversionValue(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate()
                .getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);// Sending a GET request and GETTING an object back
        CurrencyConversion currencyConversion = responseEntity.getBody();
        // Note: CurrencyConversion structure matches the response we obtain from CurrencyExchange Microservice (url above). Therefore, these values automatically get mapped.
            // -- CurrencyConversion structure --
            // public class CurrencyConversion {
            //    private Long id;
            //    private String from;
            //    private String to;
            //    private BigDecimal quantity;
            //    private BigDecimal conversionMultiple;
            //    private BigDecimal totalCalculatedAmount;
            //    private String environment;
            //}
            // -- Response we obtain from CurrencyExchange Service (url above) --
            //{
                //	"id":10001,
                //	"from":"USD",
                //	"to":"INR",
                //	"conversionMultiple":65.00,
                //	"environment":"8000 instance-id"
            //}

        return new CurrencyConversion(
                currencyConversion.getId(),
                from,
                to,
                quantity,
                currencyConversion.getConversionMultiple(),
                quantity.multiply(currencyConversion.getConversionMultiple()),
                currencyConversion.getEnvironment());
    }
}

```

## Problems with the above methodology of calling other microservices (solution as Feign)

- To make a simple REST API call from a microserice to another microservice we needed to write about 20 lines of code.

- Imagine what would happen if in a Microservices architecture you have hundreads of microservices, they'd all be calling each other and you'd need to repeat this kind of code everywhere.

To resolve this issue, **Spring Cloud** provides you with a framework called **Feign**.

- **Feign** => it makes it very easy to call other microservices

- (note: feign in this case is added to our CurrencyConversionService, bcs that is the microservices from which we are calling the CurrencyExchangeMicroservice)

So, adding the dependency of Feign:

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

Then, add the following in the starter of our microservice `@EnableFeignClients`:

```
@EnableFeignClients
@SpringBootApplication
public class CurrencyConversionServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CurrencyConversionServiceApplication.class, args);
	}
}
```

Then we have to create a Proxy class for the specific microservices we're going to call (proxy it must be an interface):

```
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="currency-exchange-service", url="localhost:8000")
public interface CurrencyExchangeProxy {
    @GetMapping("currency-exchange/from/{from}/to/{to}")
    public CurrencyConversion retrieveExchangeValue(@PathVariable String from, @PathVariable String to);
    // Note: CurrencyConversion structure matches the response we obtain from CurrencyExchange Microservice (url above). Therefore, these values automatically get mapped.
        //     -- CurrencyConversion structure --
        //     public class CurrencyConversion {
        //        private Long id;
        //        private String from;
        //        private String to;
        //        private BigDecimal quantity;
        //        private BigDecimal conversionMultiple;
        //        private BigDecimal totalCalculatedAmount;
        //        private String environment;
        //    }
        //     -- Response we obtain from CurrencyExchange Service (url above) --
        //    {
        //    	"id":10001,
        //    	"from":"USD",
        //    	"to":"INR",
        //    	"conversionMultiple":65.00,
        //    	"environment":"8000 instance-id"
        //    }
}
```

Then we use this proxy class to make a REST API call to the other microservice (CurrencyExchange) in our controller (CurrencyConversionController):

```
package com.example.currency_conversion_service.CurrencyConversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

@RestController
@RequestMapping(path="currency-conversion")
public class CurrencyConversionController {
    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping(path="from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversionValue(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = this.proxy.retrieveExchangeValue(from ,to);
        return new CurrencyConversion(
                currencyConversion.getId(),
                from,
                to,
                quantity,
                currencyConversion.getConversionMultiple(),
                quantity.multiply(currencyConversion.getConversionMultiple()),
                currencyConversion.getEnvironment());
    }
}
```

## Problem that we still face: when communicating between two microservices (in CurrencyExchangeProxy) we're hardcoding URL of service we trying to communicate to

So, if I would want to get the (Currency Conversion Service) to talk to a different instance of (Currency Exchange Service) I would have to manually change that URL from localhost:8000 to localhost:8001 or localhost:8002 and so on

The state we want to get to:

- be able to dynamically launch (Currency Exchange Service) instances

- and distribute load between all the active instances of (Currency Exchange Service)

As instances come up and go down, we want to be able to automatically discover them and load balance between them.

![x](../images/im8.png)

Even if Feign would provide us the ability to enter multiple URLs, such as:

```
@FeignClient(name="currency-exchange-service", url="localhost:8000;localhost:8001;localhost:8002")
```

It still would not be a good solution. Let's say instance on port 8000 went down and a new instances was brought up on 8003 - so you would have to be changing the code all the time. That is the reason why we go for something called **Service Registry** or **Naming Server**.

![x](../images/im9.png)

What will happen: In a microservice architecture, all the instances of all the microservices would register with a Service Registry.

- Let's say the (Currency Conversion Microservice) wants to talk to the (Currency Exchange Microservice)

- It would ask the Service Registry for the adressses of all the active instances of (Currency Exchange Microservice)

- Then the service registry would return those back to the (Currency Conversion Microservice)

- Then, (Currency Conversion Microservice) would load balance between all the active instances of (Currency Exchange Microservice) (send out requests)

### Creating a Naming Server with Eureka - (it involves creating a new SpringBoot application)

Dependices needed to create the Naming Server (with Eureka) are the following:

- Spring Boot DevTools

- Spring Boot Actuator

- Eureka Server

- Lombok

Then, in the starting of application we add this annotation (`@EnableEurekaServer`):

```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class NamingServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(NamingServerApplication.class, args);
	}
}
```

Then, in `application.properties` we add the following configuations:

```
spring.application.name=naming_server
server.port=8761

# configurations to ensure that this specific server doesn't register with itself
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

Then, we can launch the Naming Server. This is the way Eureka looks like:
![x](../images/im10.png)
At the moment, we have no instances of any microservices registered.

### Getting (Currency Exchange Service) and (Currency Conversion Service) to communicate with (Naming Server) (Eureka)

**Step 1: Connecting (Currency Exchange Service) and (Currency Conversion Service) to the Naming Server (Eureka)**

1. Open the pom.xml of (Currency Exchange Service) and (Currency Conversion Service), add the following dependecy to each of them (they are both microservices):

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

2. Add the following line in `application.properties`:

```
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
```

3. Let's try and launch one of the instances of (Currency Exchange Service) (on port 8000)

Now, after simply adding this dependency and adding a line in `application.properties`
We can see that after launching the microservice, it gets immediately registered in Eureka.

![x](../images/im11.png)

4. Launching the other microservice (Currency Conversion Service) we see that it similarly gets registerd in Eureka:

![x](../images/im12.png)

**Step 2: Afterwards, we'd make the (Currency Conversion Service) talk to (Currency Exchange Service) through the Naming Server**

## Load Balancing with (Eureka, Feign) and (Spring Cloud Load Balancer)

![x](../images/im8.png)

Load balancing between the multiple instances of (Currency Exchange Service) from (Currency Conversion Service)

To make it load balance btw multiple instances we go into the (Currency Conversion Service) proxy:

- All we have to do is replace this annotation above the proxy inteface `@FeignClient(name="currency-exchange-service", url="localhost:8000")` for this annotation `@FeignClient(name="currency-exchange-service")`

- That way FeignClient will talk to Eureka to pick up the instances of (Currency Exchange Service) & perform load balancing btw them

Note: problem fixed at this stage is to rename our two microservices in their corresponding application.properties files, from:
(spring.application.name=currency_conversion_service and spring.application.name=currency_exchange_service)
to (spring.application.name=currency-conversion-service and spring.application.name=currency-exchange-service)

Such that the name passed into Feign matches the names that appear in Eureka Naming Server

(these naming are supposed to be done right from the start)

Now, we can launch the second instance of (Currency Exchange Service) on port 8001.
If we look at the Eureka Dashboard of the Naming Server, we can see that now there are two instances of (Currency Exchange Service) running:

![x](../images/im13.png)

Now if we make multiple requests to the (Currency Conversion Service) endpoint to get some conversion, we would see that:

- sometiems it is being processed by 1st instance of (Currency Exchange Service) at port 8000

  ![x](../images/im14.png)

- and other times it is being processed by the 2nd instance of (Currency Exchange Service) at port 8001

  ![x](../images/im15.png)

What is in here?
Inside the (Currency Conversion Service) there is a Load Balancer component which is: (talking to the Naming Server), (finding the instances of a microsrvice to we're trying to communicate with), (doing automatic load balacing between the available instances) => this is called **Client Side Load Balancing**

- This **Client Side Load Balancing** is happening through **Feign**

- **Feign** uses the **Spring Cloud Load Balancer** to distribute the load among the multiple instances which are returned by Eureka

- (in earlier version of Spring Cloud, load balancer that was used was Ribbon, but now they shifted to Spring Cloud Load Balancer as the load balancer)

## Setting up Spring Cloud API Gateway

Now, we have only impemeneted two microservices: (Currency Conversion Service=SpringBoot app) and (Currency Exchange Service=SpringBoot app) as well as the(Naming Server with Eureka=SpringBoot app).

But in typical microservices architectures there would be hundreds of microservices like these. And these microservices have a lot of common features such as: (authentication, authorization, logging, rate limiting) - where do we implement all these features?

```
Solution to this problem is to use the **API Gateway**.

Note: in the older versions of Spring Cloud, the popular API Gateway to use was Zul.
But since Zul is no longer supported by Netflix, Spring Cloud has moved on and now the recommended option as an API Gateway is _Spring Cloud Gateway_)
```

## Implementing _Spring Cloud Gateway_ by creating new SpringBoot application, with libraries:

### Launching API Gateway

- Lombok =>
- Spring Boot DevTools =>
- Spring Boot Actuator =>
- Eureka Discovery Client => bcs we want the API Gatway to connect to Naming Server (Eureka) (we also used this library in both of the microservices we've created before (Currency Conversion Service) and (Currency Exchange Service))
- Gateway =>

Add the following lines to the `application.properties` and launch (when launching make sure that the Naming Server is also launched obviously):

```
spring.application.name=application-gateway
server.port=8765
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
```

Now, after launching both microservices (with 1 instance of Currency Conversion Service and 2 instances of Currency Exchange Service on port 8000 and 8001), launching API Gateway and launching Naming Server - the Eureka dashboard looks like this:

![x](../images/im16.png)

### Enabling Discovery Locator with Eureka for Spring Cloud Gateway

- Now, the idea is that we can access our services through this API Gateway URL (http://localhost:8765)

  - For example, if we were to access Currency Exchange Service, we would combine:

    - API Gateway URL = http://localhost:8765
    - Name of Currency Exchange Service in Eureka Deasboard = CURRENCY-EXCHANGE-SERVICE
    - Path to the Currency Exchange Service = currency-exchange/from/USD/to/INR

So, obtaining: http://localhost:8765/CURRENCY-EXCHANGE-SERVICE/currency-exchange/from/USD/to/INR

But, now we get an error when we execute this URL.

We want the API Gateway to:

1. Talk to Eureka with this name (CURRENCY-EXCHANGE-SERVICE)
2. Find the server location (http://localhost:8000 or with any other corresponding port)
3. Then execute request to this URL (currency-exchange/from/USD/to/INR)

To enable this feature in Spring Cloud API Gateway we add the following `application.properties` in API Gateway server:

```
spring.cloud.gateway.discovery.locator.enabled=true
```
