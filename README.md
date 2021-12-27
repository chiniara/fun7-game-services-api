# fun7 Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

## Pre requisites

- Java 11
- Maven
- MySQL 5.7
- Google Cloud SDK to deploy

## Assumptions

- You need to pass all the parameters for the check-services api.
- The user is really simple as most of the info required for the api to work would be sent by the application requesting
  it.

## What could be worked on in the future with more time

- Auth in Admin API
- Dockerfile to run locally
- Adding native tests for Quarkus Native
- Better test naming
- Better error handling
- Better validators
- Using DTOs for all the requests
- Better error messages responses
- Adding a cache layer for the APIs
- Use more asynchronous operations
- Adding a CI/CD Pipeline

## Running the application in dev mode

You will need a running mysql server. You can install one locally or start one using docker using:

```shell
docker run --name docker-mysql -e MYSQL_ROOT_PASSWORD=<password> -p 3306:3306 -d mysql:5.7
```

Create a .env file in the root folder with the following variables set:

```
_DEV_AD_API_URL=
_DEV_AP_API_USER=
_DEV_AP_API_PASS=
_DEV_DB_URL=
_DEV_DB_USER=
_DEV_DB_PASS=
```

Run tests with (You can check the coverage in /target/jacoco-report) :

```shell script
./mvnw verify
```

Then run your application in dev mode using:

```shell script
./mvnw compile quarkus:dev
```

The application will now be running on

```
http://localhost:8080
```

## Packaging and deploying the application to GCP App Engine

1. [Install and configure Cloud SDK](https://cloud.google.com/sdk/docs/install)

2. You will need a Google Cloud SQL MySQL instance running.

3. Then you'll need the following variables set in .env

    ```
    _PROD_AD_API_URL=
    _PROD_AP_API_USER=
    _PROD_AP_API_PASS=
    _PROD_DB_URL=
    _PROD_DB_USER=
    _PROD_DB_PASS=
    _PROD_DB_REGION_INSTANCE=
    ```

4. The application can then be deployed using:

    ```shell script
    gcloud app deploy --project <project_name>
    ```

# **The main endpoints**

## Check Services API

### **GET**

### **`/check-services?cc={cc}&userId={id}&timezone={timezone}`**

Example

### `/check-services?cc=US&userId=1&timezone=America%2FSao_Paulo`

### **Returns the status of the services**

```json
{
  "multiplayer": "disabled",
  "userSupport": "disabled",
  "ads": "enabled"
}
```

## Admin Api

### **GET**

### **`/admin/user`**

### **Returns all users**

- Response
    ```json
    [
      {
        "userId": 1,
        "timesPlayed": 1
      }
    ]
    ```

### **`/admin/user/{id}`**

### **Returns a detailed user**

- Response
    ```json
    {
      "userId": 1,
      "timesPlayed": 1
    }
    ```

### **POST**

### **`/admin/user`**

### **Creates a new user**

- Body:
    ```json
      {}
    ```
- Response
    ```json
    {
      "userId": 1,
      "timesPlayed": 1
    }
    ```

### **DELETE**

### **`/admin/user/{id}`**

### **Deletes a user**

- Response

```

```
