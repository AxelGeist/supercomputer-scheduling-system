## DelftBlue Scheduling System

The DelftBlue Scheduling System is an application designed to manage and allocate resources for university employees using the DelftBlue supercomputer. The system consists of several subdomains, including employees, faculty accounts, sysadmins, jobs, clusters of nodes, and the scheduler.

# Features
- User authentication: Users must be authenticated before making resource requests.
- Resource allocation: The main function of the system is to allocate resources to users.
- Bounded Contexts: The system includes several bounded contexts, including Users & Authentication, Jobs, Clusters, and the Scheduler.

# UML Component Diagram
![image](https://user-images.githubusercontent.com/73136957/229944811-20b837f7-eb1f-422a-a731-772d32cc8c9d.png)


# Lab Template

This template contains two microservices:
- authentication-microservice
- example-microservice

The `authentication-microservice` is responsible for registering new users and authenticating current ones. After successful authentication, this microservice will provide a JWT token which can be used to bypass the security on the `example-microservice`. This token contains the *NetID* of the user that authenticated. If your scenario includes different roles, these will have to be added to the authentication-microservice and to the JWT token. To do this, you will have to:
- Add a concept of roles to the `AppUser`
- Add the roles to the `UserDetails` in `JwtUserDetailsService`
- Add the roles as claims to the JWT token in `JwtTokenGenerator`

The `example-microservice` is just an example and needs to be modified to suit the domain you are modeling based on your scenario.

The `domain` and `application` packages contain the code for the domain layer and application layer. The code for the framework layer is the root package as *Spring* has some limitations on were certain files are located in terms of autowiring.

## Running the microservices

You can run the two microservices individually by starting the Spring applications. Then, you can use *Postman* to perform the different requests:

Register:
![image](instructions/register.png)

Authenticate:
![image](instructions/authenticate.png)

Hello:
![image](instructions/hello.png)
