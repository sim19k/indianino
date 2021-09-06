
## Prerequisites

- Java
- Node
- Typescript
- Docker
- Visual Studio Code
- Maven

## Installation

1. Clone this repo using `git clone https://github.com/JamesBoadi/BaseProject.git`
2. Move to the appropriate directory: `cd BaseProject/src/client`
3. Install dependencies: `npm install`
4. Create a launch.json file `https://code.visualstudio.com/docs/java/java-debugging` Start the Java backend from the root of the directory: `cd BaseProject/src/main` or run from the command line
5. Move to the directory of the Angular app `cd BaseProject/src/client` and run the app: `npm start`

## Run

1. Each Microservice needs to be run seperately, first, start the eureka server by running Application.java 
`cd BaseProject/src/main/java/com/server`, then run each microservice (Gateway, Identity-Provider). Each microservice has a main class, so you can run each one from there, refer to step 4 of installation.

## Configuration

You can configure the database through the `.properties` file, for each microservice you can configure it to any database you like

To repackage and generate a jar go to the directory of the microservice and run the command `mvnw package`

The project is already configured as a multi module maven project, to rebuild, run `mvmw clean install` from the root of the project.

## Docker

You can also run this using Docker

## Build the Image

1. Fetch the code using git clone `https://github.com/JamesBoadi/BaseProject.git`

1. Build using `mvn package`

   This will create the jar: `mvn/wrapper/maven-wrapper.jar`

    * Use the OpenJDK 8 docker image (freely available at Docker hub) as a starting point. This image defines a minimal Linux system with OpenJDK 8 preinstalled.
    * Copy the jar into the container and rename it to `app.jar` to save typing later.  By default, `app.jar` will be copied into the root of the container file system.
    * Expose ports 8761, 8001, 8002

1. To build the container (**note** the `.` at the end, indicating to use the current directory as its working directory):

    ```sh
    docker build -t {your_github_acc}/BaseProject .
    ```

1. Check it worked. You should see `{your_github_acc}/BaseProject` listed.

    ```sh
    docker images
    ```

## Running the Application

We will run the container three times, each time running the Java application in a different mode.

![Example Microservices System](mini-system.jpg)

1. They need to talk to each other, so let's give them a network:

    ```sh
    docker network create identity-net
    ```

1. Now run the first container. This runs up the Eureka registration server, which will allow the other microservices to find each other:

    ```sh
    docker run --name reggo --hostname reggo --network identity-net -p 8761:8761 {your_github_acc}/BaseProject java -jar app.jar reg
    ```

    The `-d` (detach) flag is missing so all output will stream to the console so we can see what is happening.
    
    As soon as the application starts up, it displays its IP address. Remember this for later.

1. In your browser, go to http://localhost:8761 and you should see the Eureka dashboard. There are no instances registered.

1. _In a new CMD/Terminal window_, run a second container for the gateway microservice. This holds a database if 21 available accounts (stored using the H2 in-memory RDBMS database)

    ```sh
    docker run --name gateway --hostname gateway --network gateway-net -p 8081:8081 {your_github_acc}/BaseProject java -jar app.jar accounts  --gateway.server.hostname=<reg server ip addr>
    ```

    Replace `<eg server ip addr>` with the IP address you determined earlier.

1. Return to the Eureka Dashboard in your browser and refresh the screen.  You should see that `ACCOUNTS-SERVICE` is now registered.

1. _In a new CMD/Terminal window_, run a third container for the accounts identityProvider-service. This is a web-application for viewing account information by requesting account data from the identityProvider microservice.

    ```sh
    docker run --name identityProvider --hostname identityProvider --network accounts-net -p 8082:8082 {your_github_acc}/BaseProject java -jar app.jar identityProvider --identityProvider.server.hostname=<eg server ip addr>
    ```

    Replace `<eg server ip addr>` with the IP address you determined earlier.

1. Return to the Eureka Dashboard in your browser and refresh the screen.  You should see that `GATEWAY-SERVICE` and `IDENTITY-PROVIDER-SERVICE` are now registered.

