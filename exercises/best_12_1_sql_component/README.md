SQL Component
=============

Prerequisites: Exercise 06.1

Configure a SQL component to be used with a Postgres database

Steps
-----


Start a Postgres instance as docker container

```
docker run -d --rm -p 5432:5432 -e POSTGRES_DB=camel -e POSTGRES_USER=camel -e POSTGRES_PASSWORD=camel postgres
```

Create the tables

```
cd ./solutions/commons
mvn flyway:migrate
```

Insert dependencies for the SQL-Component and jdbc drivers:

```
	<dependency>
	    <groupId>org.apache.camel.quarkus</groupId>
	    <artifactId>camel-quarkus-sql</artifactId>
	</dependency>		
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-jdbc-postgresql</artifactId>
    </dependency>
	<dependency>
	    <groupId>org.postgresql</groupId>
	    <artifactId>postgresql</artifactId>
	</dependency>
```

Configure the datasource:

```
quarkus.datasource.my_db.db-kind=postgresql
quarkus.datasource.my_db.username=camel
quarkus.datasource.my_db.password=camel
quarkus.datasource.my_db.jdbc.url=jdbc:postgresql://localhost:5432/camel
quarkus.datasource.my_db.jdbc.max-size=11
```

Add `SqlComponentiT.java` to your project and make sure the test is green.

Optional
-------

Write a SqlComponent, such that the following URI cab be used:

```
"mypgsql://delete from order_to_producer"
```
