# Joe-Fraser T-Mobile Assessment

Reference Materials for Project<br/>
My Existing Code from other Projects and Assessments<br/>
Google, lol

#SQLite(Do this first) 
Download the sqlite-tools-win-x64-3490200.zip from site: https://sqlite.org/index.html and install it<br/>
Then run sqlite3.exe va.db and create the va.db database as per the requirements.md document<br/>
The requirements say to make two tables Notifications and Templates joined by a foreign key<br/>
You should be able to use the file, in the project folder databaseScripts to help<br/>

Non-Docker directions ignore if using Docker<br/>

After the tables are made and va.db is persisted to the disk, copy the va.db to the project folder data/SQLite<br/>
Create the project folder if it does not exist<br/>
The application will now work, with and without Docker<br/>

An example run from the windows shell would be:  sqlite3 va.db<br/>
Example SQLite command line to list all tables<br/>
sqlite> .tables <br/>

Docker Directions<br/>
The true production way to implement the SQLite database is to put it in a DockerDesktop Volume<br/>
So again go to a running DockerDesktop and click on Volumes, then click on Create.<br/>
You must use the name joeFraserDataSQLite If you do not want to change docker-compose.yml to the name<br/>
you like , sorry about that. Now follow the directions in project file, copyLocalFileToDockerVolume.<br/>
If it worked you will see va.db in DockerDesktop. For Linux people just change the cp path to your va.db loacation<br/>
 

#Maven Compile in Eclipse(Do this second)
after the Project is loaded in Eclipse(File Import from Git)<br/>
right click on Project VA-assessment<br>
Hover on Run As<br/>
choose Maven build, not Maven build...<br>
The Maven build Run Configuration needs to have the goals set to: clean package<br/>


#Directions for VA Assessment Boot and Testing

The Design constraints are the App will build in Docker and can run in Docker<br/>
or can run without Docker. These constraints have been meet.<br/>
 
#Example Java Location
C:\Program Files\Java\jdk-17\bin<br/>


#Example Command Line Project Boot(Do this third)
open your fav Windows Shell Instance(Command Prompt Instance)<br/>
cd c:\work\java\eclipse-workspace2\VA-assessment<br/>
"C:\Program Files\Java\jdk-17\bin\java" -Dfile.encoding=UTF-8 -Dspring.profiles.active=dev -jar target/VA-assessment-0.0.1-SNAPSHOT.jar

#Docker Information for Project Boot
I put the files docker-compose.yml and Dockerfile into the project<br/>
It is now working, and tested. <br/>
Enter "docker compose up" from the command line, in the windows project folder.<br/>
The "docker compose up" only compiles and deploys code. No worries about multiple runs.<br/>
The va.db database in the DockerDesktop Volume is not touched.<br/>
The files now perform a compile inside Docker with Java version 17<br/>
You cannot use the command until DockerDesktop is running

#Swagger Testing(Version 3) after Boot
fav Browser url, I use google chrome<br/>
http://localhost:8080/swagger-ui/index.html

#Swagger check(Version 3) 
fav Browser url, I use google chrome<br/>
http://localhost:8080/v3/api-docs



