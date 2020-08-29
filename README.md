# code-with-quarkus project

The base project was generated using: https://code.quarkus.io/ .

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Launching the database

The project uses a PostGreSQL database, that can be started in a docker container with the script `/db/launch.sh`.

The application will drop and create the table upon startup.

## Launching the test-suite

The API was tested in its dev and packaged versions using Postman, which can be downloaded from: https://www.postman.com/ .

From Postman, you can `Import` the file `/postman/testsuite.postman_collection.json`.

The tests in the collection must be run in order, and the 'todos' table must be empty.

## API endpoints

The available endpoints are listed in the file `/src/main/resources/META-INF/resources/rest-paths.json`.

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
