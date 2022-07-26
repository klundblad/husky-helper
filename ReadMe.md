## Husky Helper 

This is a HTML/CSS based web app that uses the Yelp API and RatemyProfessor API in order to find some of the best rated courses and locations to eat on campus.


## Final Project for CSE 143 AU20
Husky Helper: Marlena Preigh, Andrew Zhang, Katharine Lundblad

VIDEO LINK (Demonstrating functionality of product): https://youtu.be/2s83SHibO7I

---------------------------------------------------
---------------------------------------------------
Front End Components

    - PathOptimizer.java:

    - Rating.java:
        Represents a single user rating, and contains their rating as a double between 0-5 as well as their comments

    - RatingSystem.java:
        Represents a single restaurant. Includes a set of all ratings for the certain restaurant. For the SearchEngine, 
        RatingSystem has a getName function that returns the name of the place, so the search function can check to see 
        if the user has searched for the certain restaurant.

    - SearchEngine.java:
        Contains organized data about restuarants, professors, and ratings for both. Requires a Set<RatingSystem>
        and Set<Professor> in the constructor. The searchProf method returns a set of professor names who match or contain 
        the given search query.The searchRest method returns a set of restaurant names who either match or cointain the query, or
        that belong to categories that match or contain the query.

    - YelpData.java:

Web App

            
    - Server.java:
        Enables the server to build the web app by defining an endpoint, communicating with HTTP and performing
        the commands of the search engine. Run the app by opening the terminal in the top right portion of the
        screen labelled ">_", typing in the command "javac Server.java && java Server". Then, either click the 
        globe icon to view the app with all style formatting implemented or click on the wifi icon to load the page
        in the browser. Currently, hitting the URL in order to contact ratemyprofessor.com and yelp can create a server
        overload, so we chose to implement Thread.sleep() function to the server build. This means the page will take an
        upmost 120 seconds to build due to the delays. This is due to multiple Thread.sleep() functions being called for 1000
        milliseconds or 1 second.             
    - index.html:
        Implemented two basic text search boxes using amp-mustache templates. One textbox may be typed into to find
        restaurants and their overall ratings from the Yelp API and one textbox may be used to search a teacher or class.
        Additionally, the page displays and image of Husky Stadium. 

Files and Documentation

    - RestaurantIds:

---------------------------------------------------
---------------------------------------------------
Additional Comments: 

Video Link (Same Link as the above): https://youtu.be/2s83SHibO7I

run command: javac Main.java && java Main


