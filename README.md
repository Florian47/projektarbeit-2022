## Sample of a spring boot backend

Use a JDK17 in your IDE *and* in gradle started from the IDE.

Add a user:

    curl -X POST -H "Content-Type: application/json" -d '{"firstName":"Martin", "lastName":"Hilbert"}' http://localhost:8080/api/person

List users:

    curl http://localhost:8080/api/person/list
    curl http://localhost:8080/api/person/entities

Use a browser on http://localhost:8080/h2-console to open database H2 Console.
Enter jdbc:h2:mem:testdb in URL and connect.
