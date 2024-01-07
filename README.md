## How to start:
1. Run Gradle tasks clean build
2. Create an Application configuration in IntellijIdea with following details:
   - JDK -> your current JDK
   - -cp -> transaction-service.main
   - Main class -> com.transaction.example.ApplicationKt
   - Environment variables(optional) -> APPLICATION_NAME=transaction-service;LOGBACK_APPENDER=STDOUT
  
## Implementation:

This project was implemented using Micronaut, MySQL and tested using Mockk.
