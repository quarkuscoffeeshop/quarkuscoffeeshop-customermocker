# quarkus-cafe-customermock project

This project uses Quarkus, the Supersonic Subatomic Java Framework!

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Environment variables

Quarkus' configuration can be environment specific: https://quarkus.io/guides/config

This service uses the following environment variables when running:
* REST_URL

This can be set for local development with:
```shell script
export REST_URL=http://localhost:8080/order
```

## Running in dev mode
```shell script
export REST_URL=http://localhost:8080
./mvnw clean compile quarkus:dev
```

## Compiling and running native binary

Obviously you need to swap your Docker (or other repository) id for <<DOCKER_HUB_ID>>
```shell script
export REST_URL=http://localhost:8080/order
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkuscoffeeshop-customermocker .
docker run -i --network="host" -e REST_URL=${REST_URL} <<DOCKER_HUB_ID>>quarkuscoffeeshop-customermocker:latest
```

## Pushing to a container registry

After building and running the container (see above) you can find image with grep, tag it, and push it with:
```shell script
docker images -a | grep customermocker
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkuscoffeeshop-customermocker:<<VERSION>>
docker push <<DOCKER_HUB_ID>>/quarkuscoffeeshop-customermocker:<<VERSION>>
```


## OpenShift Deployment 
**Deploy quarkus-cafe-customermock on OpenShift**
```
$ oc login https://api.ocp4.examaple.com:64443
$ oc project quarkus-cafe-demo
$ oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:20.0.0-java11~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-customermock --name=quarkus-cafe-customermock
```

**To delete quarkus-cafe-barista application**
```
$ oc delete all --selector app=quarkus-cafe-customermock
```

## Local deveplomnent steps 
* uncomment lines 
```
#quarkus.container-image.build=true
#quarkus.container-image.push=true
#quarkus.native.container-build=true
#quarkus.jib.base-native-image=quay.io/quarkus/ubi-quarkus-native-image:20.0.0-java11
#quarkus.container-image.group=jeremydavis
#quarkus.container-image.name=quarkus-cafe-customermocker
#quarkus.container-image.tag=0.3

# HTTP

%dev.quarkus.http.port=8084
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `quarkus-cafe-customermock-1.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/quarkus-cafe-customermock-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

```shell
export REST_URL=http://localhost:8080/order
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkuscoffeeshop-customermocker .
docker run -i --network="host" -e REST_URL=${REST_URL} <<DOCKER_ID>>quarkuscoffeeshop-customermocker:latest
docker images -a | grep customermocker
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkuscoffeeshop-customermocker:<<VERSION>>
```
