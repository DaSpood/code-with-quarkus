# code-with-quarkus project

The base project was generated using: https://code.quarkus.io/ .

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Launching the Keycloak server

The project uses a Keycloak server for OIDC, that can be started in a docker container with the script `src/main/resources/oidc/launch.sh`.

To configure the server, login to localhost:8180/auth using the credentials "admin;admin", then import the realm `src/main/resources/oidc/quarkus-realm.json`.

## Launching the database

The project uses a PostGreSQL database, that can be started in a docker container with the script `src/main/resources/db/launch.sh`.

The application will drop and create the table upon startup.

## Launching the test-suite

The postman test-suite has not been updated to support the new OIDC requirements, thus cannot be used on this branch.

## API endpoints

The available endpoints are listed in the file `/src/main/resources/doc/rest-paths.json`.

## Running the application in dev mode

You can run the application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `code-with-quarkus-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/code-with-quarkus-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.
