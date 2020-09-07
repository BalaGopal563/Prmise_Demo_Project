package com.project.utill;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggingClass {
static final Logger logger = Logger.getLogger(LoggingClass.class);

public static void loadLogger() {
PropertyConfigurator.configure("log4j.properties");
}

public static Logger getLogger() {
if (logger != null)
return logger;
else
loadLogger();
return logger;
}
}