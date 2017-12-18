package com.wily.swat.apmsql.rest.serverresources;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONTransformerIface 
  { public String jsonToSql(JSONObject jsonObject) throws JSONException; }
