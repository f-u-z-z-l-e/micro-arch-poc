# micro-arch-poc
A proof of concept for a microservice architecture with spring cloud.

## Building the projects

first build the whole project with the following command:
* ```./gradlew clean build prepareDocker```

## Start 3rd party dependencies
start zookeeper (used by kafka and for service discovery in spring), then kafka and HashiCorp vault.
* ```docker-compose up -d --build zookeeper kafka vault```

## Vault
Vault needs to be initialized and properties need to be added manually.
* ```docker exec -ti vault /bin/sh```
* ```vault login``` will prompt for a password, type: ```myroot```
* ```vault kv put /secret/account-registration-service store.properties.foo="value from vault"```

this adds one record for all ```account-registration-service``` profiles, to add another key/value to the same record, use:
* ```vault kv patch /secret/account-registration-service foo=bar```

to be able to add a record for a specific profile, the config-server has to be configured to use ``` `/` ``` as ```profile-separator```
and you would issue the following command:
* ```vault kv put /secret/account-registration-service/docker foo=bar-docker ```

HashiCorp Vault documentation can be found [here](https://www.vaultproject.io/docs/index.html). <br>
Spring Cloud Vault documentation can be found [here](http://cloud.spring.io/spring-cloud-static/spring-cloud-vault/2.0.1.RELEASE/).

## Spring Cloud Config Server
* ```docker-compose up -d --build config-server```

check if you are able to fetch the configuration from the vault through the config-server:
* ```curl -X GET http://172.20.1.40:8888/account-registration-service/docker -H "X-Config-Token: myroot"```

you should see the following in the response:
```
"propertySources": [
 {
   "name": "vault:account-registration-service/docker",
   "source": {
     "foo": "bar-docker"
   }
 },
 {
   "name": "vault:account-registration-service",
   "source": {
     "store.properties.foo": "value from vault",
     "foo": "bar"
   }
 }
 ...
 ```

Spring Cloud Config documentation can be found [here](http://cloud.spring.io/spring-cloud-static/spring-cloud-config/2.0.1.RELEASE/).
## Start spring boot/cloud services

* ```docker-compose up -d --build account-registration-service account-balance-service gateway-service```

## Postman
In the projects ```/postman``` folder a postman collection with all necessary ReST calls is available.
Feel free to import it and the preconfigured environments into you local postman installation.

## Cleaning up
You can use docker-compose to remove everything this project started/created on your local docker installation by issuing
the following command:
* ```docker-compose down --rmi all```