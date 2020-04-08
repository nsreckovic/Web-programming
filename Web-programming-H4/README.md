# Web-programming-H4

Homework 4 for Web programming course.

## The assignment

You need to create an HTTP server using the HTTPServlet class. The main purpose of your server is to enable the student to rate the assistant.

1. The server will receive a POST HTTP request with an assistant name and rating.
The path and posrt of this call are arbitrary.

   After the server receives your request, it must pass this information in JSON format to the database via a socket connection.

   The database is a separate application that receives JSON requests and places them in memory. Assistants and ratings can be placed in as an array or list. Always store the names of the assistants with all capital letters.

2. The server will also receive a GET HTTP request that lists the names of the assistants and their average ratings.
It will determine these average ratings by getting all information from the database (also in JSON format).

   All assistants whose name is Aleksandar will receive a rating of 0 not taking into account user input. 

   Return the response to the user in HTML format.

## How to run program
First, start the database (AssistantDBMain class). Then, start the Tomcat server.

## License
[MIT](https://choosealicense.com/licenses/mit/)
