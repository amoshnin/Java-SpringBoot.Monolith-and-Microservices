Enterpresises are heading towards microservices architectures => which involves building multiple small focused microservices that communicate with each other.

![x](../images/im20.png)

- Advantage: One of biggest advantages of microservices is the flexibility to build applications in different programming languages (Java, Go, Python, JavaScript). So we can leverage the unique advantages that each of those languages provide for our specific use-cases.

- Problem: As we implement different microservices in different programming languages - deployments of these microservices become complex.

  - Say [MovieService and Customer Service] are implemented in Java and [the other ones] are implemented in Python. But we don't want different deployment procedures for each of these microservice types.

  - How can we get one common way to deploy all microservices irrespective of the language/framework that is used to build these microservices?

  - That's where containers come into picture. The most popular container tool is Docker.
