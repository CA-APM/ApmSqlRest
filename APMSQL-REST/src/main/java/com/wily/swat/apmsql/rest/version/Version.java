package com.wily.swat.apmsql.rest.version;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wily.swat.apmsql.rest.bootstrap.Bootstrap;

public class Version 
  {
	public static final Version version= new Version();
	
	private Version() {}
	
	private Logger getLogger()
	  { return LogManager.getLogger(Version.class); }
	
    public String toString() 
      {
        Enumeration<URL> urlEnumeration;
        try 
          {
            urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (urlEnumeration.hasMoreElements()) 
              {
                try 
                  {
                	URL url= (URL)urlEnumeration.nextElement();
                	getLogger().debug("Jar URL: {}", url); 
                    InputStream is= url.openStream();
                    if (is != null) 
                      {
                        Manifest manifest= new Manifest(is);
                        Attributes mainAttributes= manifest.getMainAttributes();
                        String name= mainAttributes.getValue("Extension-Name");
                        if (name!=null && name.equals(Bootstrap.ApmSqlRest))
                          {
                        	String version= mainAttributes.getValue("Implementation-Version");
                          
                        	if(version != null) 
                        	  return version;
                          }
                      }
                  }               
                catch (Exception e) 
                  { getLogger().error("Exception: ",  e); }
              }
          } 
        catch (IOException ioe) 
          { getLogger().error("IOException",  ioe); }
        
        return "<version information not gotten from manifest>"; 
      }
  }