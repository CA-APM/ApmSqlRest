package com.wily.swat.apmsql.rest.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;

import org.apache.logging.log4j.status.StatusLogger;

public class Logging 
  {
	private static final String logConfigFileName= "/log4j2.xml";
	static final private String org_apache_logging_log4j_jul_LogManager= "org.apache.logging.log4j.jul.LogManager";
	static
	  { 
		// Set all loggers asynchronous
		System.setProperty("log4j2.contextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
		
		// Suppress info messages until adapter is loaded and logging configured from configuration (from file or class path)
		StatusLogger.getLogger().setLevel(org.apache.logging.log4j.Level.WARN);
		
		// Load Log4J2 logging adapter for java.util.logging.LogManager to get uniform logging thru Log4J2
		System.setProperty("java.util.logging.manager", org_apache_logging_log4j_jul_LogManager); 
		
		// Install JulToLog4J2Bridge by setting a custom logging Handler and removing others - didn't get it to work before the above
	    //JulToLog4J2Handler.install(); 
	    
	    // Check that the java.util.logmanager has been replaced by the adapter class
	    if (!java.util.logging.LogManager.getLogManager().getClass().getName().equals(org_apache_logging_log4j_jul_LogManager))
	    	getLogger().error("Configuring unified logging for Jetty failed as Log4J2 LogManager adapter class was not loaded: " + org_apache_logging_log4j_jul_LogManager);
	  }
	
	private static String getCanonicalPath(File file)
	  {
		try 
		  { return file.getCanonicalPath(); } 
		catch (Exception e) 
		  { 
			getLogger().error("Could not resolve canonical path of configfile: ", e);
			return logConfigFileName; 
		  }
	  }
	
	public static void doConfigure()
      {
		System.setProperty("log4j.configurationFile", "." + logConfigFileName);
		
		File configFile= new File("." + logConfigFileName); // prepended "." needed for file system file
    	if (configFile.exists() && configFile.isFile())
    	  {
    		try 
    		  { 
    			InputStream configStream= new FileInputStream(configFile);
    			getLogger().info("Using logging configuration for uniform Log4J2 logging from file: {}", getCanonicalPath(configFile));
    			
    			doConfigure(configStream);    		
    			return;
    		  } 
    		catch (IOException ioe) 
    		  { getLogger().error("Failed to load Log4J logging configuration from file: {}", logConfigFileName, ioe); }
    	  }
    	
    	// If file configuration fails fall back to in-jar configuration
    	System.setProperty("log4j.configurationFile", logConfigFileName);
		try
		  {  
			InputStream configStream= Logging.class.getResourceAsStream(logConfigFileName);
			getLogger().info("Using logging configuration for uniform Log4J2 logging from jar: {}", getCanonicalPath(configFile));
			doConfigure(configStream);			
		  }
		catch (IOException ioe)	    
	      { getLogger().error("Failed to load Log4J logging configuration from jar: {}", logConfigFileName, ioe); }
		 
      }
        
	private static void doConfigure(InputStream configStream) throws IOException
	  {
		ConfigurationSource configSource = new ConfigurationSource(configStream);	        
        Configuration config = XmlConfigurationFactory.getInstance().getConfiguration(null, configSource);
        
        LoggerContext ctx = (LoggerContext) LogManager.getContext(true);

        ctx.stop();
        ctx.start(config);
        
        configStream.close(); 
        
        getLogger().info("Logging in {} mode", LogManager.getLogger(Logging.class).getLevel());       
	  }
	
    // Can't have a static Logger variable as it's initialization would be interfere with logging configuration
    private static Logger getLogger()
	  { return LogManager.getLogger(Logging.class); }
  }
