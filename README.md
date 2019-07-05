# `springboot-react-https`

The goal of this project is to implement a REST API, called `movies-api`. This API will have its endpoints accessible over `HTTPS`. A Java client and a Frontend ReactJS application will be implement to use the `movies-api`.

## Project diagram

## Microservices

### movies-api

### movies-client

### movies-ui

## Create PKCS12 ertificate

In order to create a PKCS12 certificate, run the following command
```
keytool -genkeypair -alias localhost \
  -keyalg RSA -keysize 2048 -storetype PKCS12 \
  -keystore keystore.p12 -validity 3650 \
  -dname "CN=localhost, OU=MyCompany, O=MyCompany, L=Berlin, ST=Berlin, C=DE"
```

Set a password. In this project, we will use `secret`
```
Enter keystore password: secret
Re-enter new password: secret
```

To list the certificates `keystore.p12` run the command below. The password will be requested.
```
keytool -list -v -keystore keystore.p12
```

## Start microservices

### movies-api

Open a terminal and inside `springboot-react-https/movies-api` folder run
```
./mvnw clean spring-boot:run
```

### movies-client

Open another terminal and inside `springboot-react-https/movies-client` folder run
```
./mvnw clean spring-boot:run
```

### movies-ui

Open another terminal and inside `springboot-react-https/movies-ui` folder run
```
npm start
```

## References