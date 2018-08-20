# Micro-arch-poc
## Run micro-arch-config with docker compose:
1. ~~~bash
    docker-compose up --build zookeeper
   ~~~
1. ~~~bash
       docker-compose up --build kafka
   ~~~
1. ~~~bash
       docker-compose up --build config-server
   ~~~
1. ~~~bash
       docker-compose up --build vault #(optional)
   ~~~
1. ~~~bash
       docker-compose up --build account-registration-service 
   ~~~
1. ~~~bash
       docker-compose up --build gateway-service 
   ~~~
   
## Config-server

## Vault
See [Documentation](https://www.vaultproject.io/docs/index.html)
### To Test with vault 0.9.0 
1. ~~~bash
       docker-compose up --build vault
   ~~~
1. ~~~bash
       docker exec -it vault /bin/sh
   ~~~
1. ~~~bash
       vault auth myroot
   ~~~
1. ~~~bash
       vault write secret/account-registration-service store.properties.foo=Property-value-loaded-from-VAULT-Server
   ~~~