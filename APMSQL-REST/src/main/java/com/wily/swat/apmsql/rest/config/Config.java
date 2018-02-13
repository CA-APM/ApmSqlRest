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
    private static void load() throws IOException {
        try 
          {
        	File file = new File(ConfigFile);
        	InputStream inputStream;
        	
        	if(file.exists() && !file.isDirectory()) 
        	  {        		
        	    inputStream = new FileInputStream(file);
        	

        	    properties = new Properties();
        	    properties.load(inputStream);

        	    inputStream.close();
        	  }
        	else
        	  throw new IOException(ConfigFile + " not found in current directory");
          } 
        catch(IOException ioe) 
          { 
        	throw ioe;
          }
    }

    //Go get the property we want
    public static String getProperty(String property) throws IOException 
      {
        if(properties == null)
          load();

        return(properties.getProperty(property));
    }
    
    public static int getIntProperty(String property) throws IOException 
    {
      if(properties == null)
        load();

      return(Integer.parseInt(properties.getProperty(property)));
  }
}
