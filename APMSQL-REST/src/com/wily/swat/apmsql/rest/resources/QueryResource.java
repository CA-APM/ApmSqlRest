package com.wily.swat.apmsql.rest.resources;

import org.json.JSONException;
import org.json.JSONObject;

public class QueryResource extends JsonRestResource 
  {
	public final static String QUERY= "Query";
	
    @Override
	public String jsonToSql(JSONObject jsonObject) throws JSONException
      {
    	if(!jsonObject.has(QUERY)) 
	        throw new JSONException("JSON 'Query' object not supplied");
	
	    return jsonObject.getString(QUERY);
      }
    
    public static String getQuesry(String jsonString) throws JSONException
      { return (new JSONObject(jsonString)).getString(QueryResource.QUERY);  }
   }
