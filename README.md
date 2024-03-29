# FileUploadProcess

This project used to process 100k records in a csv file less than 30 seconds.

# Project Structure

![Project Structure](https://github.com/dhivya94m/FileUploadProcess/blob/master/src/main/resources/screens/1.png)

  ## Module Details SRC> JAVA
     1. com.org.bchio.config - contains the configurations of java class. ApplicationEventMulticaster and LayoutDialect - Thymeleaf               configurations are available in this module.
     2. com.org.bchio.constants - Application constant value declarations goes here.
     3. com.org.bchio.controller - FileUploadController class holds the below URL paths to access the applications.
        1. / - Redirects to index page
        2. /getPagedData/{page} - Used to get the uploaded files with pagination
        3. /getSearchData - Used to search the uploaded files
        4. /uploadFile - used to upload the files
     4. com.org.bchio.dto - Contains the DTO classes.
     5. com.org.bchio.exceptions - Contains the custome exception classes.
     6. com.org.bchio.model - Contains the model classes. Since ORM approach followed HIBERNATE framework used as a middleware between             MySQL Database layer and application layer.
        1. FileDetails - Contains the structure of uploaded file details.
        2. ValidTransactions - Contains the valid transactions and have many to one relationship with FileDetails.
        3. InvalidTransactions - Contains the invalid transactions and have many to one relationship with FileDetails.
        4. TransactionsSummary - Contains the transactions summary based on ordering currency and have many to one relationship with                  FileDetails.
     7. com.org.bchio.properties - Contains the FileStorageProperties class which will bind with the application.properties file to get           the custome properties which are starts with "file".
     8. com.org.bchio.repository - Contains the repository classes.
     9. com.org.bchio.service.component - Contains the component classes which are used to support services. 
        1. DataMapperComponent - While using JAVA 8 stream and map functions this class used to contains the map operational logics.
        2. FileDetailsSpecification - Used to hold repository specifications.
        3. IDGenerator - Used to generate unique ids using AUTOMIC INTEGER to improve the performence.
     10. com.org.bchio.service.decl - Contains the service interface declarations.
     11. com.org.bchio.service.def - Contains the service interface implementations.
          1. Responsible for upload files using NIO.
          2. Save uploaded file meta data into Mysql using JPA repository.
          3. Publish an event which initiate the file processing.
     12. com.org.bchio.service.event - Contains the file process classes.
         1. FileUploadEvent - Custome event to process the uploaded file asynchronously via background thread.
         2. FileUploadEventListner - Listen for the FileUploadEvent and initiate file processing.
         3. FileUploadEventService - This contains the actual file processing logic and contains 3 methods.
            1. loadDataWithRepository - This method used to insert data using seperate repository classes to insert into database. Via                  seperate repositories couldnot obtain the optimal performence so this is not used.
            2. loadDataWithEntityManager - Instead of seperate reposotories entityManager from PersistenceContext is used to insert the                data. Since HIBERNATE BATCH process enabled so we can get optimal performence.
            3. loadDataWithEntityManagerNativeQuery - We can get better performence than entity manager, since native query execution is                not allowed this method not used.
      13. src/test/java - Contains service classes tests.
        
  ## Module Details RESOURCES      
      1. base.html - Holds the teplate structure of the application. This enables the dynamic routing of the pages.
      2. index.html - Holds the File upload, Paginated table with file summary info and status.
    
# Technologies Used
  1. Spring boot 2.1.8
  2. Hibernate
  3. Thymeleaf
  4. MySQL

# Setup - Manual
  1. Clone the repository using 
    git clone https://github.com/dhivya94m/FileUploadProcess.git
  2. Using maven or some ide download and build the project.
  3. In application.properties configure the below properties
      1. file.upload-dir - Configure the location where the uploaded files needs to be stored. 
          1.eg: file.upload-dir=D:/uploads/
      2. spring.datasource.url=jdbc:mysql://localhost:3306/bchio
      3. spring.datasource.username=root
      4. spring.datasource.password=root
  4. In logback.xml configure the log file location
      1. property name="LOGS" value="D:/logs"
  5. Sample input data of 100k records available in resources>static>sample>sample.zip.
 
# Setup - Docker
	1. Docker must be installed on the host machine.
	2. Clone the project from github: git clone https://github.com/dhivya94m/FileUploadProcess.git
	3. Navigate to the project directory: cd BCHIO
	4. Execute docker command to up the project: docker-compose up or docker-compose up --build
	5. Ensure bchio and app-mysql-server containers are up using: docker ps -a
	6. If container is down, go to step 4.
      
# Setup - Docker Manual
	1. docker pull mysql
	2. docker run --name app-mysql-server -p 3333:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bchio -e MYSQL_PASSWORD=root -d mysql:5.7 
	3. docker ps -a
	4. git clone https://github.com/dhivya94m/FileUploadProcess.git
	5. cd BCHIO
	6. docker build . -t bchio
	7. docker run -p 8086:8086 --name bchio --link app-mysql-server:mysql -d bchio
	8. docker ps -a
	9. docker exec -it bchio bash
      
# Access Application
	Open the browser of your choice and enter: http://localhost:8086
      
# Screenshots
![Screen 2](https://github.com/dhivya94m/FileUploadProcess/blob/master/src/main/resources/screens/2.PNG)
![Screen 3](https://github.com/dhivya94m/FileUploadProcess/blob/master/src/main/resources/screens/3.PNG)
![Screen 4](https://github.com/dhivya94m/FileUploadProcess/blob/master/src/main/resources/screens/4.PNG)
![Screen 5](https://github.com/dhivya94m/FileUploadProcess/blob/master/src/main/resources/screens/5.PNG)
![Screen 6](https://github.com/dhivya94m/FileUploadProcess/blob/master/src/main/resources/screens/6.PNG)
![Screen 7](https://github.com/dhivya94m/FileUploadProcess/blob/master/src/main/resources/screens/7.PNG)

# End
Thanks.    
