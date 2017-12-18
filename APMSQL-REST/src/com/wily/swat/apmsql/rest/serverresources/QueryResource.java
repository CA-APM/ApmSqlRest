package com.wily.swat.apmsql.rest.serverresources;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import java.io.IOException;

public class QueryResource extends ServerResource implements JSONTransformerIface
  {
	protected final static String QUERY= "query";
	
    @Get
    public String help() 
      { return "Use Post for your SQL queries to this endpoint"; }

    @Post
    public Representation processQuery(Representation entity) throws IOException 
      { return Statics.ProcessJSON(entity, (JSONTransformerIface)this); }
    
    public String jsonToSql(JSONObject jsonObject) throws JSONException
      {
    	if(!jsonObject.has(QUERY)) 
	        throw new JSONException("JSON query object not supplied");
	
	    return jsonObject.getString(QUERY);
      }
  }
