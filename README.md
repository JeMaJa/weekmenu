# weekmenu

This is an application to schedule the (weekly) dinner and help with the groceries.
The application is not finished yet, but I do invite everyone to contribute!.

# Back-end
the Application is writen in Java using the SpringBoot framework. 
Key dependencies used: Spring Web, JPA, Project Lombok.
In the applications.properties in this repo an H2 test database is configured.
In the production system I intend to run it using a MariaDB database.

# Front end
As you probably will notice, this is not yet my strength.
I'm using the following for the front-end: Thymeleaf, Bootstrap (www.getbootstrap.com), and some JavaScript including jQuery. (most of this will be copy/paste from other sources)

# Classes
## Model
Recipe: holds recipes
DayRecipe: Holds the combination of a day and Recipe.
Both of these classes have DTO's and Mapper Classes.

##Controller
RecipeWebController: Recipe Related controller methods intended for WebBrowsers
RecipeRestController: The REST API controller 
MenuController: the main controller listing the menu and related things

# Future work and backlog
Tracked in Github project: https://github.com/users/JeMaJa/projects/1/views/1

#License
See: https://github.com/JeMaJa/weekmenu/blob/main/LICENSE.md

#Contact
Through github.

