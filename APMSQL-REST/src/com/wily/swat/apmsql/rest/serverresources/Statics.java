package com.wily.swat.apmsql.rest.serverresources;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.springframework.jdbc.UncategorizedSQLException;

import com.wily.swat.apmsql.rest.jdbc.JDBC;

public class Statics 
  {
	//private final static String testQuery1= "{\"query\": \"SELECT metric_path, Min ( ts ) AS minimum, Max ( ts ) AS maximum FROM numerical_metric_data WHERE ts BETWEEN {ts '2017-01-01 00:00:00.0'} AND {ts '2018-01-01 00:00:00.0'}  AND metric_path LIKE '%GC Heap%' GROUP BY metric_path ORDER BY metric_path DESC\" }";
	
	private final static String testQuery2= "{\"query\": \"SELECT distinct agent_name, metric_path, agg_value FROM  numerical_metric_data WHERE ts BETWEEN {ts '2017-12-12 10:50:00.0'} AND {ts '2017-12-12 11:00:00.0'} AND metric_path LIKE '%GC%Bytes In Use' and agent_name not like '%(Virtual)%' ORDER BY metric_path ASC\" }";
	
	 public static Representation ProcessJSON(Representation entity, JSONTransformerIface transformer ) 
	    {
		  try
	        {
		      JsonRepresentation jsonRepresentation= new JsonRepresentation(entity);
		       if (jsonRepresentation.getAvailableSize()<0)
		    	 return Statics.errorRepresentation("No JSON supplied in request body"); 
	      
		      JSONObject jsonObject = jsonRepresentation.getJsonObject();   
		      
		      try
		        { 
		    	  List<JSONObject> list= Statics.executeQuery(transformer.jsonToSql(jsonObject)); 
		    	  JSONArray array = new JSONArray(list);
				  return new JsonRepresentation(array);
		    	}
		      catch (UncategorizedSQLException ucse)
		        { return Statics.errorRepresentation("SQL error: " + ucse.getMessage()); }
		      		      
	        }
	      catch (JSONException je)
	        { return Statics.errorRepresentation("Error in JSON query: " + je.getMessage()); }
	      catch (IOException ioe)
	        { return Statics.errorRepresentation("IOError in executing query: " + ioe.getMessage()); }
	    }
	
    protected static List<JSONObject> executeQuery(String sql) throws JSONException, IOException
      {
	  	List<JSONObject> list = JDBC.execute(sql, resultSet -> 
	  	  {
	        ArrayList<JSONObject> objList = new ArrayList<JSONObject>();
	        ResultSetMetaData metaData = resultSet.getMetaData();
	        while (resultSet.next()) 
	          {
	            JSONObject jsonObj = jsonForRow(resultSet, metaData);
	            objList.add(jsonObj);
	          }
	        return objList;
	      });
	  	
	  	return list;
      }
  
  protected static JSONObject jsonForRow(ResultSet resultSet, ResultSetMetaData metaData) throws SQLException 
    {
      JSONObject rowObject = new JSONObject();

      int columnCount = metaData.getColumnCount();
      for(int i = 1; i <= columnCount; i++) 
        try 
          { rowObject.put(metaData.getColumnName(i), resultSet.getObject(i)); } 
        catch (JSONException e) 
          { e.printStackTrace(); }

      return rowObject;
    }

  protected static JsonRepresentation errorRepresentation(String errMessage) 
    {
      JSONObject object = new JSONObject();
      try 
        {
          object.put("message", errMessage);
          object.put("success", false);
        } 
      catch (JSONException e) 
        { e.printStackTrace(); }

      return new JsonRepresentation(object);
    }
  
  public static void main(String args[]) 
    {
	  LogManager.getRootLogger().setLevel(Level.DEBUG);
	  Enumeration<Logger> loggers = LogManager.getCurrentLoggers();
	  while (loggers.hasMoreElements()) 
	    loggers.nextElement().setLevel(Level.DEBUG);
	
	  List<JSONObject> list;
	  try 
	  	{ 
	  	  list= executeQuery(new JSONObject(testQuery2).getString(QueryResource.QUERY)); 
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