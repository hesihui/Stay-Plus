# Stay+

#### A full-stack online marketplace for vacation homes and condo rentals used **Spring Boot** and **React.JS** with Antd.

## FrontEnd:

log-in 

<img src="https://raw.githubusercontent.com/hesihui/Stay-Rental-Application/main/demo_pic/log_in.jpg"/>

<img src="https://raw.githubusercontent.com/hesihui/Stay-Rental-Application/main/demo_pic/upload.png"/>

## Backend APIs:

check postman: https://www.getpostman.com/collections/5a3e78aea3506db7b221

#### some example:

Add a stay: POST http://localhost:8080/reservations

```json
{
    "checkin_date": "2021-09-28",
    "checkout_date": "2021-10-01",
    "stay": {
        "id": "stay_id"
    }
}
```

Get all the rentals: GET http://localhost:8080/reservations

## Configuration :

#### For uploading image to google buckets: 

backend folder: src/main/resources/credential.json 

```json
{
  "type": "service_account",
  "project_id": "staybooking-328915",
  "private_key_id": ""
  "private_key": "PRIVATE KEY",
  "client_email": "staybooking-service@staybooking-328915.iam.gserviceaccount.com",
  "client_id": "",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "":  
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": ""
}

```

/src/main/resources/application.properties

```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql = true
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect
spring.datasource.url=<any mysql db url>
spring.datasource.username=<username>
spring.datasource.password=<password>
cret=secret
gcs.bucket=<gcs bucket name>
elasticsearch.address=<elastic search address>
elasticsearch.username=test
elasticsearch.password=12345678
geocoding_apikey=<api key>
```

## Resouces

antd: https://ant.design/docs/spec/introduce

Elastic Search on GCP: https://www.elastic.co/elasticsearch/features
