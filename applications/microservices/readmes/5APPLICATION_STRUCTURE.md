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