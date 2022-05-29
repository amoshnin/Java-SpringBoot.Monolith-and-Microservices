## Application structure Overview

We're building a complex disitrubted system => with (mutliple microservices, naming server, api gateway, distributed tracing, ...)

Overall structure of the project:

![x](../images/im6.png)

1. Microservice 1: Currency Exchange Microservice => what is the exchange rate of one currency to another

   - From this microservice we will be exposing a simple URL:

   ![x](../images/im5.png)

2. Microservice 2: Currency Conversion Microservice => convering 10 USD into INR

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
