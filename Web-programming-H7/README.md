# Web-programming-H7

Homework 7 for Web programming course.

## The assignment

Create a web application for selling airline tickets. The application should contain 10+ predefined tickets and at least two predefined airlines. Keep tickets and companies in a file or database.

If you are working with a database, it is necessary to implement an Optimistic lock mechanism to protect against concurrent modification. So, add another column to each table, with version or timestamp. Before each entry, check if the version is the same as when we downloaded it from the database. If not, someone else has already changed that record and should cancel the change and report the error.

Students who choose to save to a file do not have to worry about this problem, but they do need to provide concurrent file access.


### Entities


#### Airline:
* ID: Integer - must be unique, not displayed on the frontend
* Name: String - must be unique
* Tickets: List - a series of tickets of that company

#### Plane ticket:
* ID: Integer - must be unique, not displayed on the frontend
* One-way: Boolean — whether the ticket is one-way or round-trip
* From: String - the city from which it takes off
* To: String - the city it lands in
* Depart: Date - departure date
* Return: Date - return date (round-trip tickets only)


### Main page


Form for creating a new map with all the necessary fields.
Some way for the user to choose whether to be shown only one-way, round-trip, or all tickets.


#### Map table

It is located on the main page and contains the fields:
* One-way - whether the ticket is one-way
* From - the city from which it takes off
* To - the city it lands in
* Depart - departure date
* Return - return date
* Company - the name of the airline that is a link to the airline's website


Error handling and validation are required and each function must report an error if the input parameters or internal state do not allow the operation to be performed. Inform the user when he made a mistake.

All forms must receive parameters that make sense.
For example: place of departure and destination cannot be the same place; departure time cannot be after return time and vice versa; etc.
