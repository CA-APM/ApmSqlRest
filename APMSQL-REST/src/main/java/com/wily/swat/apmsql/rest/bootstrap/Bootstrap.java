package com.wily.swat.apmsql.rest.bootstrap;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;

import com.wily.swat.apmsql.rest.application.SQLApplication;
import com.wily.swat.apmsql.rest.config.Config;

public class Bootstrap 
  {
    public static void main(String args[]) 
      {
    	// Jetty default logging
	    System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.JavaUtilLog");
	    	
    	Component component = new Component();
        component.getDefaultHost().attach("", new SQLApplication());

        try
          {
        	component.getServers().add(startHttpServer(component, Config.getIntProperty("httpport")));
        	component.start();
          }
        catch (Exception e)
          { 
        	System.err.println("APMSQL-REST failed to start: " + e.getMessage()); 
        	e.printStackTrace();
          }
      }

    private static Server startHttpServer(Component component, Integer port) throws Exception 
      { return new Server(new Context(), Protocol.HTTP, port, component); }
}
