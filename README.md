# Savings Application With A Gateway

In this basic project, we simply create 2 microservice instances, from 1 repository, and a gateway which proxies requests to the relevant service.


## Setup and Run
We need `mvn`, `docker` and `docker-compose` commands, components needs to be installed.

On project root directory, on folder location `savings-kata`, run the commands below on terminal.

1. pom.xml will build both modules:
 `mvn clean install`

2. Docker build for savings and gateway services 
   1. `docker build -t savings-docker.jar -f savings/Dockerfile .`
   2. `docker build -t gateway-docker.jar -f gateway/Dockerfile .`
3. run our images: 
   1. `docker-compose up -d`
4. to shut down:
   1. `docker-compose down`
   

## Architecture

We have 2 Java services:
* savings on port `8081` and `8082`
* gateway on port `8080`

and PostgreSQL DB running on `5432` and `5433`

`gateway` service is a spring boot cloud gateway project. It has 5 seconds of timeout for the client responses. 

`savings` service is a Spring Boot service, which has 2 endpoints
1. GET `/balance`
2. POST `/balance`

I created 2 different images from the same project. 
Both instances have their own Postgresql DB.
On docker-compse we provide 2 main parameters for service instances, as environment variables, in order them to work independently.
1. server port(`8081` and `8082`)
2. account (`a` and `b`)

Both accounts are initialized with zero amount, since we have hardcoded accounts
# Testing

You need an HTTP tool for the APIs. Like Postman.

After containers are up, for main test case you need to call 2 different endpoints:

http://localhost:8080/savings/a/balance
http://localhost:8080/savings/b/balance

with GET and POST methods, all requests are made to Gateway:

1. Getting balance of account A:

Request method: `GET` 

Endpoint: `http://localhost:8080/savings/a/balance`

Response:
`{"amount":300}`

Response Codes:  
200 OK  
404 Not found  
504 Gateway Timeout

2. Updating `amount` for account A, you will get an updated amount info:

Request method: `POST`  
Endpoint: `http://localhost:8080/savings/a/balance`
Request Body:
`{
"amount": "-400"
}`

Response:
`{"amount":###.##}`

Response Codes:  
200 OK  
400 Bad request  
404 Not found  
422 Unprocessable Entity  
504 Gateway Timeout  


1. Getting balance of account B:

Request method: `GET`  
Endpoint: `http://localhost:8080/savings/b/balance`  
Response:
`{"amount":300}`  

Response Codes:  
200 OK  
404 Not found  
504 Gateway Timeout  
2. Updating `amount` for account B, you will get an updated amount info:

Request method: `POST`  
Endpoint: `http://localhost:8080/savings/b/balance`  
Request Body:
`{
"amount": "-400"
}`

Response:
`{"amount":###.##}`

Response Codes:  
200 OK  
400 Bad request  
404 Not found  
422 Unprocessable Entity  
504 Gateway Timeout  
 
## Database
Database is PostgreSQL. We have an extra index on `account` column.
Schema is:
```CREATE SEQUENCE seq_balance
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


CREATE TABLE IF NOT EXISTS balance
(
id                      BIGINT    NOT NULL PRIMARY KEY,
account                 VARCHAR(255)   NOT NULL UNIQUE,
amount                  NUMERIC   NOT NULL,
created_on              TIMESTAMP DEFAULT now(),
modified_on             TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS balance_id_idx ON balance(id);
CREATE INDEX IF NOT EXISTS balance_account_idx on balance(account);
```

## Questions

### How you would scale your API gateway?
Probably there should be a very high TPS for this need :)
What I could be using an HAProxy like proxy app. So a client side load balancer would manage multiple instances of API Gateway. It would be another layer for maintenance, but could be a solution. 

### Monitoring uptime so you can sleep at night?

There are several tools available, ELK stack, grafana, actuator can be used.  
Alerts can be created with some criteria about errors, error frequency, system resource statuses. So that we can be alerted even if we are not available next to our laptop.
    