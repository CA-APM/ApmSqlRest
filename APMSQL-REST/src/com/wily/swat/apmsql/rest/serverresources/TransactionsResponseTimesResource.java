package com.wily.swat.apmsql.rest.serverresources;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import java.io.IOException;

public class TransactionsResponseTimesResource extends ServerResource implements JSONTransformerIface
  {
	private final static String MINUTES= "Minutes";
	private final static String MinutesMarker= "{" + MINUTES + "}";
	private final static String NowMarker = "{Now}";
		
	private final static String SqlTemplate=
	  "SELECT " + 
	  "FROM_UNIXTIME(" + NowMarker + ") as Now, " +
	  "'Live' as Type, " +
	  "REPLACE(REPLACE(Replace(Substring(metric_path, LOCATE('|Business Transactions|', metric_path)+23), ':Average Response Time (ms)', ''), ':Total Transactions Per Interval', ''), ':Total Defects Per Interval', '') as Transaction, " +
	  "'Amsterdam' as Location, " +
	  "FORMATTIMESTAMP(ts, 'YYYY-MM-dd HH:mm') AS TimeStamp_Minute, " +
	  "60 as Granularity, " +
	  "metric_attribute||'' as Metric, " + 
	  "ROUND(AVG(agg_value), 0)+0 as Average, " + 
	  "MAX(max_value)+0 as Max, " + 
	  "SUM(value_count)+0 as Count " +
	  "FROM numerical_metric_data " +
	  "WHERE metric_path LIKE '%|Business Service|%|Business Transactions|%' " +
	  "AND TIMESTAMPADD(SQL_TSI_MINUTE , -" + MinutesMarker + ", FROM_UNIXTIME(" + NowMarker + ")) <= ts AND ts < FROM_UNIXTIME(" + NowMarker + ") " +
	  "AND metric_attribute IN ('Average Response Time (ms)', 'Total Transactions Per Interval', 'Total Defects Per Interval') " +
	  "GROUP BY REPLACE(REPLACE(Replace(Substring(metric_path, LOCATE('|Business Transactions|', metric_path)+23), ':Average Response Time (ms)', ''), ':Total Transactions Per Interval', ''), ':Total Defects Per Interval', ''), metric_attribute, FORMATTIMESTAMP(ts, 'YYYY-MM-dd HH:mm') " + 
	  "ORDER BY Type, Transaction, Location, Metric, TimeStamp_Minute ";
	
    @Get
    public String help() 
      { return "Use Post for your queries to this endpoint"; }

    @Post
    public Representation processQuery(Representation entity) throws IOException 
      { return Statics.ProcessJSON(entity, (JSONTransformerIface)this); }
    
    public String jsonToSql(JSONObject jsonObject) throws JSONException
      {
    	if(!jsonObject.has(MINUTES)) 
	      throw new JSONException("Minutes object not supplied in JSON");
    	
    	String now= Long.toString((System.currentTimeMillis()/60000L)*60L-60L);
    	
    	int minutes= jsonObject.getInt(MINUTES);
    	
    	if (minutes>30)
    	  throw new JSONException("Minutes value must not exceed 30");
 
	    return SqlTemplate.replace(MinutesMarker, (new Integer(minutes)).toString()).replace(NowMarker, now);
      }
}
