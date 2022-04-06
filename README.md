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
### A, include it into a maven project
Donwload the jar file from the project ```Jar/Dialogue_System.jar```
The main usage of this project is to provide a tool to perform dialogue, use approach below to import this 
project into your project and make use of the project:


#### step 1, generate new maven project using command:
If you are importing this project as library into you maven project, this step can be omitted.
```
mvn -B archetype:generate -DgroupId=com.example -DartifactId=example -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.0
```
#### step 2, create new dictionary at the root path of you project(where ```pom.xml``` located) named libs and put ```Dialogue_System.jar``` in to this dictionary.
#### step 3, add follow dependency to the pom file:
```
<dependency>
      <groupId>org.multiAgent</groupId>
      <artifactId>multiAProj</artifactId>
      <version>1.3.1</version>
      <scope>system</scope>
      <systemPath>${project.basedir}/libs/Dialogue_System.jar</systemPath>
</dependency>
```
set language level if needed:
```
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
```
set compiler version if needed:
```
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
            </plugin>
        </plugins>
    </pluginManagement>
</build>
```
#### step 4, update maven settings:
```
mvn install
```
#### step 5, the package has imported into your project, you can import the dialogue component into you program and make use of it, examples of performing dialogue are shown in ```src/main/java/org/multiAgent/example/```

You can run, import, or test the project simply using an Integrated Development Environment.
Examples of how to construct and run the system are provided in: 
```src/main/java/org/multiAgent/example/```.

A template in ```src/main/java/org/multiAgent/Template.java``` show how to perform dialogue without csv file.

### B, running directly from jar file:
Donwload the jar file from the project ```Jar/Dialogue_System.jar```.
Use command below to run a dialogue and save dialogue result and dialogue log in seperate csv files.
An example csv file is in path: ```src/main/java/org/multiAgent/example/exampleCsvFile.csv```

```
java -cp %PATH_TO_Dialogue_system.jar% org.multiAgent.dialogueFromCsv [path to input csv file] [the goal of the dialogue] [path to csv file to save result] [path to csv file to save log]
```
Use command below could perform a simulation of a random generated dialogue:
```
java -cp %PATH_TO_Dialogue_system.jar% org.multiAgent.randomDialogue [number of agents] [number of actions] [number of values] [number of arguments] 
```
### C, running the project via maven:

Download and unzip the project from github, run below command via command line in the root path of the project(where the ```pom.xml``` located):
```
mvn install
```
to download all dependencies, run through test cases and compile the program. Then run a main class via:
```
mvn exec:java -Dexec.mainClass=org.multiAgent.%PACKAGE_TO_MAIN_CLASS%
```

## Structure of CSV file
Here show the structure of csv file, csv file constructed data with format below could be input to the dialogue system to run:

![exampleCsvFile](https://user-images.githubusercontent.com/59595500/162023565-caa222e8-b896-494a-a92a-0ff72fb34874.png)
