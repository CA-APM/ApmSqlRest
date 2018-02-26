# APMSQLREST

# Description
APMSQLREST is a HTTP/REST-JSON wrapper service for CA APMSQL/JDBC.

If you want to extract metric data from [CA Application Performance Management (CA APM)](https://www.ca.com/us/products/ca-application-performance-management.html) using a REST service with a JSON result this extension is for you.

You can submit HTTP REST-JSON requests in JSON format for SQL queries to the APMSQLREST end point you configure when installing APMSQLREST.

APMSQLREST acts as a SQL over REST service that wraps the APMSQL JDBC interface provided by CA APM. It implements REST end points that accept JSON REST calls over HTTP that are forwarded to CA APM as SQL select statements via JDBC. The JDBC query results are converted into JSON responses. For more information on CA APMSQL see the [CA APMSQL Cookbook](https://docops.ca.com/ca-apm/10-7/en/integrating/ca-apmsql-cookbook).


## Short Description
APMSQLREST is a HTTP/REST-JSON wrapper service for CA APMSQL/JDBC.

## APM version
CA APM 10.5.2 SP2

## Supported third party versions
N/A

## Limitations
None

## License
[Apache License, Version 2.0](LICENSE)

# Installation Instructions

## Prerequisites
* Java 1.8+ must be installed on the target computer
* APMSQL's JDBC server and port must be reachable from the target computer. See [Connect to CA APMSQL](https://docops.ca.com/ca-apm/10-5/en/integrating/ca-apmsql-cookbook/connect-to-ca-apmsql). CA APMSQL is not an extension but rather a self-contained service that connects to CA APM, preferably a *Manager of Managers* (MoM) in a clustered environment or a stand alone CA APM Enterprise Manager (EM) for consistent results.

## Dependencies
APM 10.5.2 SP2

## Installation
Extract the jar-file and the configuration file `apmsql-rest.json` to any folder on the target computer where you want to run APMSQLREST.

## Configuration
Four rather self-explanatory configuration parameters must be set in the configuration file `apmsql-rest.json`, e.g.:
* jdbcurl=jdbc:teiid:apm_base@mm://localhost:54321 -- URL to APMSQL teiid driver, host, and jdbc-port
* jdbcuser=DB: Provide either a CA APM user/password pair or a security-token for user and no password
* jdbcpass=DB
* httpport=2511: port on which this APMSQL-REST service will listen for HTTP REST/JSON requests.

# Usage Instructions
Simply run the self-executing jar-file: `java -jar APMSQLREST-<version>.jar`.

This will start the REST service listening for HTTP REST-JSON requests on the port configured.

Out-of-the-box two service end points are configured as described in the next two sections.

For more information on CA APMSQL syntax see the [CA APMSQL Cookbook](https://docops.ca.com/ca-apm/10-7/en/integrating/ca-apmsql-cookbook).

## Query Endpoint: /query.  

This endpoint expects a POST request:
```
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
	}, {
		...
	}
]
```

## Query Endpoint: /ApmSqlRest/Transactions/ResponseTimes
This endpoint is an example of a custom end point where the client only has to supply one parameter to fetch metrics as needed by the client.

Semantics: Metrics *Average Response Time*, *Total Defects Per Interval*, and *Total Transactions Per Interval* are fetched for the period of the last 1 or 30 full minutes.

It expects a POST request:

```
Header: Content-Type: application/json

JSON Body
{ "minutes": "<value>" }

JSON Response array object:
[
	{
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
	}, {
		...
	}
]
```

## Error result
Any error will result in a JSON response body with `success=false` and a message object describing the error, e.g.:
```
{
	"success": false,
	"message": "Error in JSON query: Minutes value must not exceed 30"
}
```

## Debugging and Troubleshooting
Download the entire eclipse project from [github](https://github.com/CA-APM/ApmSqlRest) and run in debug-mode in Eclipse to resolve any issues or adapt APMSQLREST for your own use case.


## Support
This document and associated tools are made available from CA Technologies as examples and provided at no charge as a courtesy to the CA APM Community at large. This resource may require modification for use in your environment. However, please note that this resource is not supported by CA Technologies, and inclusion in this site should not be construed to be an endorsement or recommendation by CA Technologies. These utilities are not covered by the CA Technologies software license agreement and there is no explicit or implied warranty from CA Technologies. They can be used and distributed freely amongst the CA APM Community, but not sold. As such, they are unsupported software, provided as is without warranty of any kind, express or implied, including but not limited to warranties of merchantability and fitness for a particular purpose. CA Technologies does not warrant that this resource will meet your requirements or that the operation of the resource will be uninterrupted or error free or that any defects will be corrected. The use of this resource implies that you understand and agree to the terms listed herein.

Although these utilities are unsupported, please let us know if you have any problems or questions by adding a comment to the CA APM Community Site area where the resource is located, so that the Author(s) may attempt to address the issue or question.

Unless explicitly stated otherwise this extension is only supported on the same platforms as the APM core agent. See [APM Compatibility Guide](http://www.ca.com/us/support/ca-support-online/product-content/status/compatibility-matrix/application-performance-management-compatibility-guide.aspx).

### Support URL
https://github.com/CA-APM/ApmSqlRest/issues

# Contributing
The [CA APM Community](https://communities.ca.com/community/ca-apm) is the primary means of interfacing with other users and with the CA APM product team.  The [developer subcommunity](https://communities.ca.com/community/ca-apm/ca-developer-apm) is where you can learn more about building APM-based assets, find code examples, and ask questions of other developers and the CA APM product team.

If you wish to contribute to this or any other project, please refer to [easy instructions](https://communities.ca.com/docs/DOC-231150910) available on the CA APM Developer Community.

## Categories
Reporting Integration

# Change log
Changes for each version of the extension.

Version	| Author				| Comment
--------|-----------------------|--------
1.0.1	| Henrik Nissen Ravn 	| First published version of the extension.
1.0.2	| Henrik Nissen Ravn 	| Expanded a bit.
1.0.3	| Henrik Nissen Ravn 	| Simplified a bit. Public repository.
1.0.4	| Henrik Nissen Ravn	| Included Commons Logging in unified logging.
		|						| Added async appender and LMAX based queuing.
		|						| Added internal property setting Log4J2.IsWebApp=false to suppress
		|						| web-app messages from Jetty.
