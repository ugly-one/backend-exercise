# Studies & Me Backend Exercise
This repository comprises a small exercise in Scala and GraphQL meant for use during the interview process at Studies&Me. 
Any solution must be forked off of this repository.

The exercises revolves around a simple todo list.

The service presented here allows a user to list all current todo tasks as well as create new tasks.

# Requirements and Getting Started
In order to run the existing code you must have the following installed
* SBT (scala build tool)
* Docker and preferably docker-compose

In order to get started simply
* Fork this repository
* Run `sbt docker`
* Run `docker-compose up -d`

This will start a database and a server locally. 
The server is GraphQL based and will by default listen for queries at
`localhost:9103/graphql`

The exercise comes with some predefined data to test whether the service is working as expected.
You should be able to send a query to the above url with 
`query { tasks { id, description} }`
and receive a response containing 3 todo tasks.

Lastly there exists a graphiql interface at `localhost:9103/graphiql` which may prove helpful for understanding the domain.

# Running tests
Only a few tests exist out of the box.
They can be run with `sbt test`.

# The Exercise
The following is a list of abilities it would be nice of this service to have
* The ability to mark a todo task as completed
* The ability to delete todo tasks
* The ability update individual task descriptions

Implement what you feel like of the above.
They vary slightly in complexity, but the important part is how you attack working with a foreign code base.

Further more, it is not required to stick to GraphQL for this exercise. 
While we use GraphQL in Studies&Me, we also realise that Sangria is not necessarily easy to grog.
Please implement this REST-fully if that seems simpler for you.

It almost goes without saying, but no implementation is complete without some form of testing.

# Bonus Points
The following is not required, but would be nice to have a discussion around.
Actual implementation would be even cooler.
* End to end testing
* Authorization

# And finally
Remember to have fun :)

# Changelog
* v1.0
    * Initial version of this exercise based on Studies&Me backend