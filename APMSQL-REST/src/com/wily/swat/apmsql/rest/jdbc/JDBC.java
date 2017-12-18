package com.wily.swat.apmsql.rest.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.wily.swat.apmsql.rest.config.Config;

import javax.sql.DataSource;

public class JDBC 
  {
    private final static String TeiidDriver= "org.teiid.jdbc.TeiidDriver";
	
	private static JdbcTemplate jdbcTemplate;

    public static <T> T execute(String sql, ResultSetExtractor<T> extractor) 
      {
        if(jdbcTemplate == null) 
          jdbcTemplate = init();

        return jdbcTemplate.query(sql, extractor);
      } 

    private static JdbcTemplate init() 
     { return new JdbcTemplate(getDs()); }

    private static DataSource getDs()
      {
        String url = Config.getProperty("jdbcurl");
        String user = Config.getProperty("jdbcuser");
        String pass = Config.getProperty("jdbcpass");
        
        System.out.println("Read peoperties, user: '" + user + "', url: '" + url + "'");

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(pass);
        ds.setDriverClassName(TeiidDriver);

        return ds;
      }
  }
