# Web-programming-H3

Homework 3 for Web programming course.

## The assignment

You need to create an HTTP server using socket.
The main purpose of the server is to return the quote of the day.

The server will receive a GET HTTP request that will return the quote of the day. The path and port of this call are arbitrary.

In order to return the quote of the day to the browser (client), the quote must be retrieved from an external service. You can find the documentation here: https://quotes.rest/.
Your server upon request for a client's request must send a GET request to https://quotes.rest/qod.
What you need to do is add an Accept parameter to the header to ask the service to return JSON to you.

The server response should be a redirect to the url on your server that has the quote of the day.

## License
[MIT](https://choosealicense.com/licenses/mit/)
