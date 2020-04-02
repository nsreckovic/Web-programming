# Web-programming-H5

Homework 5 for Web programming course.

## The assignment

You need to create an HTTP server using JSP pages.
The main purpose of your application is to show the user the entered data by scope levels.

The page should have two sections:

1. Input section

   You need to have one section on the page (div or something similar) dedicated to data entry. 
   It will contain: 
   * One drop-down menu to select the scope level.
   * One key field (String)
   * One value box (Also string)
   * Submit forms button.

   The menu will offer three options:
   * REQUEST
   * SESSION
   * APPLICATION

   After submit it is necessary to save the data in the appropriate scope and return the page which besides the entry section also has a display section (may be the same JSP).

2. Display section

   Below the input section, if there is data in any scope, there must be a display section. 
   The display section will be divided into three sections:
   * Subsection for request scope.
   * Session scope subsection.
   * Subsection for application scope.

   Stylize all sections so that the section of the data can be clearly identified.
   Each section will consist of a series of stored data presented in a key-value format.

   If the section does not have any data then that section shouldn't exist in html, ie. the server should not even generate it. The session scope section must also print somewhere a JSESSIONID that is packed in a cookie.

## License
[MIT](https://choosealicense.com/licenses/mit/)
