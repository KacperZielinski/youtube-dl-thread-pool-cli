## What it's doing?
If you 
* have a file with list of Youtube urls
* want to download them automatically using youtube-dl
* want use multiple instances in the background

##Input: 
    [#instances] Number of instances
    [#filepath]  File with Youtube urls
    
##Scenario:
    Given:
        You have a file which contains Youtube urls.
        You have youtube-dl available in environment path. 
    When:
        You want to download all files with multiple instances in background automatically.
    Then:
        All files are downloaded to default path.
        
        
##Features
* Displays name and download progress of file
* Removes duplicated urls from file

##Requirements
* youtube-dl
* Java 8
* Maven

##Build
    mvn clean package
##Run
    java -jar target/youtube-dl-thread-pool-cli-1.0-SNAPSHOT.jar

##Future
* Ability to pass input as a args[]
* Provide simple way to choose video|mp3

##License
Apache 2.0

 
