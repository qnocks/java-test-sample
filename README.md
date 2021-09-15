![infotecs-logo](https://upload.wikimedia.org/wikipedia/commons/f/f6/Infotex.png)
# Infotecs's Technical test

## About

Develop a simple key-value service

[More](./Java.docx)

## Build
- By maven plugin

`$ mvnw spring-boot:run`

- Manually

`$ mvn install`<br>
`$ java -jar target/KeyValue-0.0.1-SNAPSHOT.jar`

## Usage

### API

**POST** `/api/v1/{key}/{value}?ttl={ttl}`

>Sets *{value}* with associated *{key}*

- ttl (time to live) - optional parameter, if it isn't present will set the default value  
- if storing is successful returns *KeyValue* 
```
{
    "key": "key1",
    "value": "val1",
    "ttl": 77777
}
```
- otherwise
```
{
    "message": "Cannot add the value. Internal error",
    "httpStatus": "INTERNAL_SERVER_ERROR",
    "timestamp": "2021-09-14T15:01:03.1639797Z"
}
```

**GET** `/api/v1/{key}`

>Retrieves value by *{key}*

- returns the *KeyValue* or if an error occurs
```
{
    "message": "No data to retrieve",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2021-09-14T21:27:05.5493405Z"
}
```

**DELETE** `/api/v1/{key}`

>Deletes value by *{key}* 

- returns *KeyValue* if the value is present otherwise the error message
```
{
    "message": "No data to delete",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2021-09-14T21:30:10.9949748Z"
}
```

**GET** `/api/v1/dump`

>Saves a current state of the repository and returns it as a downloadable file

**GET** `/api/v1/load`

>Loads a state of the repository from a file created by the *dump* operation

**GET** `/api/v1/`

> Returns list of key-values stored in the repository
 
### Properties

It's possible to change the ttl default value, the time after which values going to update, and the log frequency for updating values.

*application.properties*
```
ttl.default=180000
schedule.check=60000
schedule.log-frequency=5
```