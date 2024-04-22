SQL Endpoints
=============

Prerequisites: Exercise 12.1

Insert a order into the table order_to_producer.

Steps
-----

You may connect to the database by: 

```
docker run -it --rm  -e PGPASSWORD=camel postgres psql --host localhost -U camel -d camel -p 5432

select * from order_to_producer;
```
Create a RouteBuilder in your application (not in a unit-test) with name `PastaOrderRouteBuilder` 
Create a direct endpoint as input that accepts `OrderToProducer` message bodies.
Forward messages to a SQL component that inserts the order into the table `order_to_producer`.

Hint: You can register a generator for the uuid by:

```
    @Named
    public IdGenerator uuidGen() {
    	return new IdGenerator();
    }
```

and reference it in simple-language by: `${bean:uuidGen.nextId}`

Write a test for the route and get it green.

Optional
-------

We might need the generated id later.
Therefore write the generated key first into a header field and then use this header in the insert (e.g: `${header.bestDbKey}`)
