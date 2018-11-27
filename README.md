# Overview

This application uses Spring Boot, Java 8, PostgresQL, and Maven to deliver a RESTful API for managing any number of dynamically defined food items.
It has an example test for the /foodUnits endpoint written with jUnit, and generates basic documentation for itself when the tests are run. 

Considerations:
- I chose PostgresQL because of its great support for user-modules, particularly its support of UUIDs and Spatialization functions.
- This app uses UUIDs for all primary keys, because this application as described should eventually scale up to handle many millions of entries across
multiple database instances. UUIDs greatly simplify the work required to reconcile records between disparate servers. (No auto-number ID conflicts)
- JWT tokens are used for API authentication, because of their fantastic ability to allow user authentication without touching the database. Simplifies
scaling by decoupling from the DB when possible.

Simplifications:
- Latitude and longitude are simply stored as double precision values, rather than a single geometric type
- Validation rules currently only check for presence of attributes when adding a foodUnit, but don't do any further validation on the data provided.
- I created a single robust test for the /foodUnits endpoint as a simple demonstration of my skills, rather than throw together a barebones basic reference for the whole thing.

Next Steps:
- Assess need for geometric data types. Dragging in spatialization might not be needed.
- Validation should at least allow dynamic rules in the form of simple boolean expressions to be evaluated at runtime (i.e. $val >= 0 && $val <=100)
- A web dashboard using react could be created to allow GUI-based navigation of data in the database (not just clicking through JSON manually, prettier.)
- A web form can be created to allow users to upload their foodUnits
- Logic can be written to ensure that only admins, and the user who added a food unit can modify it.



# Installation Instructions

Step 1. Download/Clone project repo (The project required Maven, and JDK 1.8+ -- It runs on an embedded Tomcat server, so you don't have to worry about installing/configuring that.

Step 2. Install PostgresQL 9.5 (newer may work, but I configured this explicitly for 9.5.)

Step 3a. Locate FoodTracker.backup in src/main/resources

Step 3b. Run database.backup to restore the postgres database (note: I installed the pg spatial system, but didn't use it for simplicity sake. I use pgAdmin III to manage the database)

Step 4. Create user FoodTracker and give it permissions for the new database. Password: f00dtrack

Step 5. Start server (in eclipse Run as Spring Boot Project, you could also maven clean install, then run the fat jar with java -jar nameOfJar.jar)

Step 6. Use Postman to hit API using:

```
Header: Authorization
Value: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZXRoIiwidXNlcklkIjoiOWYxMzlkMWYtZjI5Zi00ZWM1LWI3ZTktNjQ1NjdkOGYzZDdjIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eUlkIjpudWxsLCJhdXRob3JpdHkiOiJST0xFX0FVVEhFTlRJQ0FURUQifSx7ImF1dGhvcml0eUlkIjoiOWYxMzlkMWYtZjI5Zi00ZWM1LWI3ZTktNjQ1NjdkOGY0NDQ0IiwiYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiZW5hYmxlZCI6dHJ1ZSwianRpIjoiNjQ5MDU5MTgtOTAyMy00NjFkLTk0NjQtZjViOGU2NGRlYjk1IiwiaWF0IjoxNTQwMjM1NjE2fQ.DRE5NNUDPLxTZXl5omAcdwgRo_2f7mPKR5kNF9C0WDxew2h4OFLpfoTXybYnwEHeD9gP4XH18-227Za3Gmydsg
```

http://localhost:8080/api/profile will return an overview of the API endpoints

Try localhost:8080/api/users or localhost:8080/api/foodUnits

The API returns payloads with embedded links to query related entities from the DB.
If you wish to upload a new foodUnit entity, you simply POST a JSON object representing a FoodUnit to /api/foodUnits
For more information, run the test suite and take a look at the example documentation it generates.

# Note

You need to run the project as a Maven test to see the quick example of a robust testing suite I put together for this skills test. It not only tests the foodUnits endpoint, it also generates basic Markdown-formatted API documentation, which can easily be massaged into something like Slate with hand-written documentation to take it to the next level.

This application also has a very, VERY simple web front-end. If you have a header-modifying plugin, you can actually browse the API directly within the web browser! The links are fully clickable.

Its only purpose was to demonstrate Spring Security added into this project as a bonus, and to provide a quick place for me to add users to the system and see their JWT tokens. The backend encrypts passwords with bcrypt, but for convenience sake my authentication provider just checks to see if the username is present in the DB before allowing access.
