### Services and their corresponding ports

- Currency Exchange Service 8000, 8001, 8002, ..
- Currency Conversion Service 8100, 8101, 8102, ...
- Netflix Eureka Naming Server 8761
- Spring Cloud API Gateway Server 8765
- Zipkin Distributed Tracing Server 9411

### Services and their corresponding URLs

- Currency Exchange Service =>

  - http://localhost:8000/currency-exchange/from/USD/to/INR

- Currency Conversion Service =>

  - http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/10

  - http://localhost:8100/currency-conversion/feign/from/USD/to/INR/quantity/10

- Eureka

  - http://localhost:8761/

- API Gateway

  - http://localhost:8765/
