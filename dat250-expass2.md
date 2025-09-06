DAT250: Software Technology Experiment Assignment 2

## Installation
- I used [Spring initializr](https://start.spring.io/) to start the project.
- Dependencies: ``Spring Web``

## Project set up
### _Zone.Identifier
- After I setup the project, i got a lot of ``_Zone.Identifier`` files, which i didn't like at all.
- I found out that If you download a file from the internet on Windows, the OS adds ``ADS``
- Which basically stores extra metadata and stuff. These are for i security purposes.
- I tried to remove them for good, but failed.
- Since they dont affect my project, I just ignored it in .gitignore.
- Otherwise, I am happy with how the project was set up.   


### Unresolved reference build.gradle.kts
- After I indexed the project, everything in the file ``build.gradle.kts`` got unresolved. 
- I checked the version of gradle -v matched with distributionUrl.
- I ``Sync All gradle projects``. 
- I ìnvalidates caches.. and restarted. 
- I ran simple gradle prompts: 
- ./gradlew test
-  ./gradlew bootRun # try visiting http://localhost:8080 in your browser
-  ./gradlew bootJar

- I knew for a fact that gradle worked, but the editor IntelliJ struggled to recognize gradle. 

  