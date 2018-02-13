package com.wily.swat.apmsql.rest.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.wily.swat.apmsql.rest.resources.QueryResource;
import com.wily.swat.apmsql.rest.resources.TransactionResponseTimesResource;
import com.wily.swat.apmsql.rest.resources.TransactionsResponseTimesResource;

public class SQLApplication extends Application 
  {
	// This is where you would add new REST end points
	// There are only two requirements:
	// 1) Attach the relative URI of your new end point to the router below
	// 2) Implement the corresponding resource class and attach to the router
    @Override
    public synchronized Restlet createInboundRoot() 
      {
        Router router = new Router(getContext());

        // Add additional APMSQL REST endpoints to be aqdded here
        router.attach("/ApmSqlRest/Query", QueryResource.class);
        router.attach("/ApmSqlRest/Transactions/ResponseTimes", TransactionsResponseTimesResource.class);
        router.attach("/ApmSqlRest/Transaction/{transactionName}/ResponseTimes", TransactionResponseTimesResource.class);
       
        return router;
      }
  }
