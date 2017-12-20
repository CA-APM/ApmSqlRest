package com.wily.swat.apmsql.rest.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.wily.swat.apmsql.rest.serverresources.QueryResource;
import com.wily.swat.apmsql.rest.serverresources.TransactionsResponseTimesResource;

public class SQLApplication extends Application 
  {
    @Override
    public synchronized Restlet createInboundRoot() 
      {
        Router router = new Router(getContext());
//      CorsFilter corsFilter = new CorsFilter(getContext(), router);
//      HashSet<String> allowedOrigins = new HashSet<>();
//      allowedOrigins.add("*");
//      corsFilter.setAllowedOrigins(allowedOrigins);

        // Add additional APMSQL REST endpoints to be aqdded here
        router.attach("/ApmSqlRest/Query", QueryResource.class);
        router.attach("/ApmSqlRest/Transactions/ResponseTimes", TransactionsResponseTimesResource.class);

        return router;
      }
  }
