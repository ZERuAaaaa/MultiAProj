# MultiAProj
MultiAProj is a multi-agent dialogue system to solve multi-parties decision making problem. 
Users could include this project as java library or run this program stand alone.


## environment setup:

This project is programmed with Java 17 make sure you install Java 17 and had all environment variables setted.
type this command in cmd to check java version
```
java -version
```
Maven version 3.8.1 is used to construct the project, ensure available version of maven is installed, use command  to check maven version.

```
mvn -v
```

## getting started:
The project provide three ways to form dialogue, depend on the usage:
### 1, include it into a maven project
Donwload the jar file from the project ```Jar/Dialogue_System.jar```
The main usage of this project is to provide a tool to perform dialogue, use command below to import this 
project into your project and make use of the project:

```
mvn install:install-file -Dfile=%PATH_TO_Dialogue_System.jar% -DgroupId=com.multiAProj
-DartifactId=multiAProj -Dversion=1.3.1 -Dpackaging=jar
```

Examples of how to construct and run the system are provided in: 
```src/main/java/org/multiAgent/example/```

### 2, running directly from jar file:
Donwload the jar file from the project ```Jar/Dialogue_System.jar```
Use command below to run a dialogue and save dialogue result and dialogue log in seperate csv files.
An example csv file is in path: ```src/main/java/org/multiAgent/example/exampleCsvFile.csv```

```
java -cp %PATH_TO_Dialogue_system.jar% org.multiAgent.dialogueFromCsv [path to input csv file] [the goal of the dialogue]
[path of csv file to save result] [path of csv file to save log]
```

### 3, running the project as maven project:

Download and unzip the project from github, run below command via command line in the root path of the project(where the ```pom.xml``` located):

```
mvn install
```

to download all dependencies, run through test cases and compile the program.
## Structure of CSV file
Here show the structure of csv file, csv file constructed data with format below could be input to the dialogue system to run:

![exampleCsvFile](https://user-images.githubusercontent.com/59595500/162023565-caa222e8-b896-494a-a92a-0ff72fb34874.png)
