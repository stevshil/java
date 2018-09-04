# Log4j in Springboot

Must exclude spring boots in-built logger if you wish to use log4j in your applications.  Doing this though will require you to use the log4j.properties file and locate it in src/main/resources.  This will then require different profiles as the properties will be included into your application at compile time not run time.

Using the spring boot starter web logging you can add your logging requirements to your application.log file;

```
logging.level.root=ERROR
logging.file=logs/myapp.log
```
