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
