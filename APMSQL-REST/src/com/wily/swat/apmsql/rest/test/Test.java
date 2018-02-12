package com.wily.swat.apmsql.rest.test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.wily.swat.apmsql.rest.jdbc.JDBC;
import com.wily.swat.apmsql.rest.resources.QueryResource;

public class Test 
  {
    //private final static String testQuery1= "{\"query\": \"SELECT metric_path, Min ( ts ) AS minimum, Max ( ts ) AS maximum FROM numerical_metric_data WHERE ts BETWEEN {ts '2017-01-01 00:00:00.0'} AND {ts '2018-01-01 00:00:00.0'}  AND metric_path LIKE '%GC Heap%' GROUP BY metric_path ORDER BY metric_path DESC\" }";
	
  	private final static String testQuery2= "{\"query\": \"SELECT distinct agent_name, metric_path, agg_value FROM  numerical_metric_data WHERE ts BETWEEN {ts '2017-12-12 10:50:00.0'} AND {ts '2017-12-12 11:00:00.0'} AND metric_path LIKE '%GC%Bytes In Use' and agent_name not like '%(Virtual)%' ORDER BY metric_path ASC\" }";
  	
    @SuppressWarnings("unchecked")
    private static void setLogging(Level logLevel) 
      {
  	  LogManager.getRootLogger().setLevel(logLevel);
  	  Enumeration<Logger> loggers = LogManager.getCurrentLoggers();
  	  while (loggers.hasMoreElements()) 
  	    loggers.nextElement().setLevel(logLevel);
      }
    
	public static void main(String args[]) 
    {
	  setLogging(Level.DEBUG);
	
	  List<JSONObject> list;
	  try 
	  	{ 
	  	  list= JDBC.executeQuery(new JSONObject(testQuery2).getString(QueryResource.QUERY)); 
	  	  System.out.println(list.toString());
	  	} 
	  catch (JSONException e) 
	  	{
		  e.printStackTrace();
		}
	  catch (IOException ioe)
	    { ioe.printStackTrace(); }
    }
  }