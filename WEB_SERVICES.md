## Definition of Web Service
**Web service** => software system designed to support interoperable machine-to-machine interaction over a network. With 3 key features:
1. Web service is designed for a machine-to-machine (or application-to-application) interaction
2. Should be interoperable - not platform dependent. Any application should be able to communicate with my application irrespective of the technologies used.
3. Should allow communication over a network (So, not physical transfer of files) Instead, something like through HTTP requests to backend endpoints.

- (ex: a backend server which exposes HTTP endpoints that can be queried by any application is a web service)
- (ex: a front-end application like Facebook is not a web service as it is desigend for human interaction only)

For my application to be a (web service), any application must be able to communicate with it (be interoperable), for instance:
- Application A written in C++ should be able to talk to my application
- Application B written in Java should be able to talk to my application
- Application C written in PHP should be able to talk to my application

Irrespective of their technologies, they should be able to talk to my application, that's when my application can be considered a Web Service.

## Interaction of (web-service) with applications

### Process of data exchange btw (a web-service and applications)

When (application A) communicates with (a web-service) 
(supposing it's a todo service)
- Application A would have to send a **Request** to the web-service with some input
  
    (ex: give me the todos of Tom)


- Web-Service would send back a **Response** to the application with some output

  (ex: these are the todos of Tom: [todo1, todo2, ...])

### How can we make web-services platform independent?

Commonly used data exchange formats in (requests and responses) that would be interoperable between different technolgies are: (JSON and XML).

Both (JSON and XML) can be (created and interpreted) by Java/C++/PHP/...

So, applications built with different technologies would be able to (send requests and receive responses) to/from the web-service

### How does application know the format of Request/Response?

Application A needs to send a **request** to the web-service.
Then, once application A receives the **response** it needs to be able to process it.

Every web-service, must offer a service definition, which includes:
- Request and Response format (JSON/XML/other)
- Request structure. What structure should a consumer follow to create a request to the web-service?
- Response structure. What is the structure of the response returned by this web-service?
- Endpoint (URL). Where is this web-service available?

## Key terminology related to web-services

- **Request** = input to a (web-service) by an (application)
- **Response** = output from a (web-service) to an (application)
- **Message exchange format (XML and JSON)** = format of the (request and response)
- **Service provider (server)** = web-service provides services that then get consumed by clients/consumers
- **Service consumer (client)** = who is using the services providede by a web-service
  
    (ex: PHP application, Java application are clients/consumers)
- **Service definition** = contract between the (service provider) and (service consumer)
- **Transport (HTTP and MQ)** = it defines how a web-service is called:
  - if web-service is exposed over Internet, so I will call it with a URL => HTTP 
  - if web-service is exposed over Queue => MQ   
    1. (service requester) would place a message in the queue
    2. (service provider) will be listening on the queue 
    3. as soon as there's a request on the queue, service provider would take the request
    4. service provider will then process this request, create the response and put it back in the queue
    5. then, service requester would get the response from the queue. transport which is used is MQ.
  
    ![q](/images/mq.png)
   