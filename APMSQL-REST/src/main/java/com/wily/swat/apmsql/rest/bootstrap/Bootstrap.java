package com.wily.swat.apmsql.rest.bootstrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;

import com.wily.swat.apmsql.rest.application.SQLApplication;
import com.wily.swat.apmsql.rest.config.Config;
import com.wily.swat.apmsql.rest.logging.Logging;
import com.wily.swat.apmsql.rest.version.Version;

public class Bootstrap 
  {
	public static final String ApmSqlRest= "APMSQL-REST";
	static private Logger log;
	static
	  { 
		System.setProperty("log4j2.is.webapp", "false");
		
		Logging.doConfigure(); 
	  }
	
  public static void main(String args[]) 
    {    	
	  
	  log = LogManager.getLogger(Bootstrap.class);
	  logHello();
	  
  	  Component component = new Component();
      component.getDefaultHost().attach("", new SQLApplication());

      try
        {
      	  component.getServers().add(startHttpServer(component, Config.getIntProperty("httpport")));
      	  component.start();
        }
      catch (Exception e)
        { log.error("APMSQL-REST failed to start: " + e); }
    }

    private static Server startHttpServer(Component component, Integer port) throws Exception 
      { return new Server(new Context(), Protocol.HTTP, port, component); }
    
    private static void logHello()
      { log.info("{} extension started, version {}", ApmSqlRest, Version.version.toString()); }
}
