# Joe-Fraser T-Mobile Assessment

Reference Materials for Project<br/>
My Existing Code from other Projects and Assessments<br/>
Google, lol

#H2(Do this first) 
H2 is an in memory Database. It will be wiped out on every new Run.<br/>
The only work required would be to modify PersistenceJpaConfig.java to load entities contained in folder<br/>
springboot.entities into H2 automatically. I am using value "update" which does perform table creation. <br/>
The hibernate property to change is: hibernate.ddl-auto<br/>

#Maven Compile in Eclipse(Do this second)
after the Project is loaded in Eclipse(File Import from Git)<br/>
right click on Project TmobileAssessment<br>
Hover on Run As<br/>
choose Maven build, not Maven build...<br>
The Maven build Run Configuration needs to have the goals set to: clean package<br/>


#Directions for TmobileAssessment Boot and Testing

The Design constraints are the App will build in Docker and can run in Docker<br/>
or can run without Docker. These constraints have been meet.<br/>
 
#Example Java Location
C:\Program Files\Java\jdk-21\bin<br/>

#Example Command Line Project Boot(Do this third)
open your fav Windows Shell Instance(Command Prompt Instance)<br/>
cd c:\work\java\eclipse-workspace2\TmobileAssessment<br/>
"C:\Program Files\Java\jdk-21\bin\java" -Dfile.encoding=UTF-8 -Dspring.profiles.active=dev -jar target/TmobileAssessment-0.0.1-SNAPSHOT.jar

#Docker Information for Project Boot
I put the files docker-compose.yml and Dockerfile into the project<br/>
It is now working, and tested. <br/>
Enter "docker compose up" from the command line, in the windows project folder.<br/>
The "docker compose up" only compiles and deploys code. No worries about multiple runs.<br/>
Remember H2 is in memory and destroyed on every run.<br/>
The files now perform a compile inside Docker with Java version 21<br/>
You cannot use the command until DockerDesktop is running

#Swagger Testing(Version 3) after Boot
fav Browser url, I use google chrome<br/>
http://localhost:8080/swagger-ui/index.html

#Swagger check(Version 3) 
fav Browser url, I use google chrome<br/>
http://localhost:8080/v3/api-docs<br/>

#H2 Console
fav Browser url, I use google chrome<br/>
http://localhost:8080/h2-console<br/>
database Url is: jdbc:h2:mem:testdb<br/>




