package com.devaffeine.auth2;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {
    public static DataSource createDataSource(String url, String user, String password, boolean readOnly) {
        HikariConfig masterConfig = new HikariConfig();
        masterConfig.setJdbcUrl( url );
        masterConfig.setUsername( user );
        masterConfig.setPassword( password );
        masterConfig.addDataSourceProperty( "cachePrepStmts" , "true" );
        masterConfig.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        masterConfig.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        masterConfig.setReadOnly(readOnly);
        return new HikariDataSource( masterConfig );
    }
}
