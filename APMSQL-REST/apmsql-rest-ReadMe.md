#ApmSqlRest
	A HTTP/REST wrapper service for APMSQL/JDBC.

	APMSQL provides ODBC & JDBC interfaces to CA APM. Tis service wraps the JDBC interface in the sense that
	this service implements REST end points that accepts JSON REST calls over HTTP that are forwarded via JDBC as SQL Select 	statements towards CA APM. Results gotten through JDBC are converted into JSON responses.

#Configuration 

	4, self.explanatory, configuration parameters must be set

	jdbcurl=jdbc:teiid:apm_base@mm://localhost:54321 -- URL to APMSQL teiid driver, host, and jdbc-port

	jdbcuser=DB -- Provide either EM user/password pair or a security-token for user and no password

	jdbcpass=DB

	httpport= 2511  -- Port on which this APMSQL-REST service will listen


#Query Endpoint: /query.  

	
	It expects a POST request:
	
		Header: Content-Type: application/json
		
		JSON Body:
			{
			    "query": "<APMSQL SELECT statement>";
			}

		JSON response array object:
			[
			  {
			    "field1": "value1",
			    "field2": "value2",
			    "field3": "value3",
			    "field4": value4,
			    "field5": value5
			  }
			  ...
			]
		
#Query Endpoint: /ApmSqlRest/Transactions/ResponseTimes
	
	Semantics: Metrics “Average Response Time”, “Total Defects Per Interval”, and “Total Transactions Per Interval” are fetched for 	the period of the last <1 or 30> full minutes. 
	
	It expects a POST request:
	
		Header: 
			Content-Type: application/json
		JSON Body
			{"minutes": "<value>" }
	
	  	JSON Response array object:
			[{
				"Type": "Live",
				"Transaction": "ViewInitialOrderLineCheck-Dispatch",
				"Max": 116,
				"Now": "2017-12-13 14:38:00.0",
				"Average": 100,
				"Metric": "Average Response Time (ms)",
				"Count": 8,
				"Granularity": 60,
				"TimeStamp_Minute": "2017-12-13 14:37",
				"Location": "Amsterdam"
			},{
			...
			}]

#Error result

	Any error will result in a JSON response body with 'success= false' and a message 	object describing the error:
	
	{"success":false,"message":"Error in JSON query: Minutes value must not exceed 30"}
	