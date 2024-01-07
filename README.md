## How to start:
1. Run Gradle tasks clean build
2. Create an Application configuration in IntellijIdea with following details:
<img src="./documentation/RUN_CONFIG.png" width="700" alt="Run config">
   - JDK -> your current JDK
   - -cp -> transaction-service.main
   - Main class -> com.transaction.example.ApplicationKt
   - Environment variables(optional) -> APPLICATION_NAME=transaction-service;LOGBACK_APPENDER=STDOUT 
