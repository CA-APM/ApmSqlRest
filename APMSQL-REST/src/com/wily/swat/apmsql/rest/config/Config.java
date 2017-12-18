package com.wily.swat.apmsql.rest.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    
	private final static String ConfigFile= "apmsql-rest.config";
    
	private static Properties properties = null;
    

    //Load static properties from the properties file
    private static void load() {
        try 
          {
        	File file = new File(ConfigFile);
        	InputStream inputStream;
        	
        	if(file.exists() && !file.isDirectory()) 
        	  inputStream = new FileInputStream(file);
        	else
        	  // Use config inside the jar-file.
        	  inputStream = Config.class.getResourceAsStream("/apmsql-rest.config"); 

            properties = new Properties();
            properties.load(inputStream);

            inputStream.close();
          } 
        catch(IOException ioe) 
          { ioe.printStackTrace(); }
    }

    //Go get the property we want
    public static String getProperty(String property) 
      {
        if(properties == null)
          load();

        return(properties.getProperty(property));
    }
    
    public static int getIntProperty(String property) 
    {
      if(properties == null)
        load();

      return(Integer.parseInt(properties.getProperty(property)));
  }
}
