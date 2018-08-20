# Micro-arch-poc

## Config-server

## Vault


### To Test with vault 0.9.0 
VAULT 0.9.0

#### Vault docker start Terminal 1
1. docker-compose up --build vault
#### Vault inside container Terminal 2
1. docker exec -it vault /bin/sh
1. export VAULT_ADDR=http://localhost:8200
1. vault auth myroot
1. vault write secret/account-registration-service store.properties.foo=demo-user-swagger