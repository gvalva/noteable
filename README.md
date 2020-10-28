# Noteable Take Home

This application was built using Sclatra with integration with Slick. Please ensure you have the following requirements installed on your machine.

  - sbt version 1.3.3
  - Scala version 2.13.3

# Starting the Server
The internal application server is can be started using the following sbt commands.

```sh
$ cd noteable
$ sbt
$ ~;jetty:stop;jetty:start
```


# Creating the DB and load preset values.
In order to create the database and load the data, you must invoke the following 2 REST APIs in the following order.
```sh
$ curl -X GET '127.0.0.1:8080/db/create-tables' --verbose
$ curl -X GET '127.0.0.1:8080/db/load-data' --verbose
```

# Running the application
There is currently an existing bug which fails to parse date parameters.

- Get all doctors 
`curl -X GET '127.0.0.1:8080/doctor' --verbose`
- Get all appointments by doctor id (currently bug exists)
`curl -X GET '127.0.0.1:8080/doctor/1/10-10-2020' --verbose`
- Delete existing appointment (curretly bug exists)
`curl -X DELETE '127.0.0.1:8080/doctor/1/10-10-2020' --verbose`
- Create a new appointment 
`curl -X POST '127.0.0.1:8080/doctor' \
  -H 'Content-Type: application/json' \
  -d '{"doctor":{"id":1,"firstName":"Chris","lastName":"Mas"},"appointment":{"id":7,"startTime":"2020-10-25 15:00:00.000","endTime":"2020-10-25 15:45:00.000","appointmentType":"new","doctorId":"2"}}' \
  --verbose
`
