package com.wily.swat.apmsql.rest.resources;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.wily.swat.apmsql.rest.jdbc.JDBC;

public abstract class JsonRestResource extends ServerResource implements JSONTransformerIface
  {
	@Get
    public String helpForGet() 
      { return "Use 'Post' for your SQL queries to this endpoint"; }
    
    @Post
    public String helpForPost() 
      { return "Please Post Content-Type 'application/json' for this endpoint"; }
    
    @Post ("json")
    public Representation processQuery(Representation entity) throws IOException 
      { return JDBC.ProcessJSON(entity, (JSONTransformerIface)this); }
    
    public abstract String jsonToSql(JSONObject jsonObject) throws JSONException;
  }
