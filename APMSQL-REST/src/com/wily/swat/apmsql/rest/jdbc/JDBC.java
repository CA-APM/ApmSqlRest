package com.wily.swat.apmsql.rest.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.teiid.jdbc.TeiidDriver;

import com.wily.swat.apmsql.rest.config.Config;
import com.wily.swat.apmsql.rest.resources.JSONTransformerIface;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class JDBC 
  {
	private final static Logger LOGGER = Logger.getLogger(JDBC.class.getName());
	
	private static JdbcTemplate jdbcTemplate;

    public static <T> T execute(String sql, ResultSetExtractor<T> extractor) throws IOException 
      {
        if(jdbcTemplate == null) 
          jdbcTemplate = init();

        return jdbcTemplate.query(sql, extractor);
      } 

    private static JdbcTemplate init() throws IOException 
     { return new JdbcTemplate(getDs()); }

    private static DataSource getDs() throws IOException 
      {
        String url = Config.getProperty("jdbcurl");
        String user = Config.getProperty("jdbcuser");
        String pass = Config.getProperty("jdbcpass");
        
        LOGGER.info("Teiid JDBC connection: User: '" + user + "', url: '" + url + "'");

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(pass);
        //ds.setDriverClassName(TeiidDriver.class.getName());
        ds.setDriver(TeiidDriver.getInstance());

        return ds;
      }
    
     public static Representation ProcessJSON(Representation entity, JSONTransformerIface transformer ) 
  	  {
  		try
          {
	        JsonRepresentation jsonRepresentation= new JsonRepresentation(entity);
	         if (jsonRepresentation.getAvailableSize()<0)
	    	   return errorRepresentation("No JSON supplied in request body"); 
      
	        JSONObject jsonObject = jsonRepresentation.getJsonObject();   
	      
	        try
	          { 
	    	    List<JSONObject> list= executeQuery(transformer.jsonToSql(jsonObject)); 
	    	    JSONArray array = new JSONArray(list);
			    return new JsonRepresentation(array);
	    	  }
	        catch (UncategorizedSQLException ucse)
	          { return errorRepresentation("SQL error: " + ucse.getMessage()); }      
            }
          catch (JSONException je)
            { return errorRepresentation("Error in JSON query: " + je.getMessage()); }
          catch (IOException ioe)
            { return errorRepresentation("IOError in executing query: " + ioe.getMessage()); }
  	    }
  	
      public static List<JSONObject> executeQuery(String sql) throws JSONException, IOException
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
  }
