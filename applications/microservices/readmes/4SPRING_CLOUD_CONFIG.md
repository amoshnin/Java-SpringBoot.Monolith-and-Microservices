1. Create a Spring Boot application that will act as a SpringCloudConfigServer
2. Create a config directory which will store all configurations for all microservices (and make it a github repository)
3. In application.properties of SpringCloudConfigServer add:

```
server.port=8888
spring.application.name=spring_cloud_config_server
spring.cloud.config.server.git.uri=file://${user.home}/library/dev/dmeo/applications/microservices/config-repo
```

4. Code for starting out the SpringCloudConfigServer, you must add the annotation `@EnableConfigServer`:

```
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

```

The `spring.cloud.config.server.git.uri` would store path to a directory where you store configurations for all microservices

5. Create each microservices that you need and in each of them - add the following to application.properties:

```
spring.config.import=optional:configserver:http://localhost:8888
spring.application.name=limits_service
spring.profiles.active=qa
```

- The value of `spring.profiles.active` configures the environment of that microservices (qa/dev/default/...).
- The value of `spring.application.name` configures the name of that microservice. It is important that it matches the names of files in config directory, for instance, they could be `limits_service.properties`, `limits_service-dev.properties`, `limits_service-qa.properties`

6. In each microservice, create the following Configuration class which will retrieve values from its corresponding configuration file stored in the configuration repo through the SpringCloudConfigServer:

```
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("limits-service")
public class Configuration {
    private int minimum;
    private int maximum;
}
```

Therefore, the corresponding values in the configuration file for limits_service would be (limits_service.properties):

```
limits_service.minimum: 6
limits_service.maximum: 777
```

Then, in any Controller you require, you can @Autowired this Configuration, to have access to it:

```
import com.example.demo.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/limits")
public class LimitController {
    @Autowired
    private Configuration configuration;

    @GetMapping(path="item")
    public Limit getItem() {
        return new Limit(this.configuration.getMinimum(), this.configuration.getMaximum());
    }
}
```

The model limit in this case looks like this:

```
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Limit {
    private int minimum;
    private int maximum;
}
```
