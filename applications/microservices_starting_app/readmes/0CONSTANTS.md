### Services and their corresponding ports

- Limits Service 8080, 8081, ...
- Spring Cloud Config Server 8888

### Services and their corresponding URLs

- Limits Service http://localhost:8080/limits http://localhost:8080/actuator/refresh (POST)
- Spring Cloud Config Server http://localhost:8888/limits-service/default http://localhost:8888/limits-service/dev
