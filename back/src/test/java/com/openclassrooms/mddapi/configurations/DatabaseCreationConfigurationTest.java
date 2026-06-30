package com.openclassrooms.mddapi.configurations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DatabaseCreationConfigurationTest {

  @Test
  void dataSourceBeanBuildsDataSourceForPostgresSystemDatabase() {
    DataSourceProperties properties = Mockito.mock(DataSourceProperties.class);
    @SuppressWarnings({"unchecked", "rawtypes"})
    DataSourceBuilder<?> builder = Mockito.mock(DataSourceBuilder.class);
    DataSource expectedDataSource = Mockito.mock(DataSource.class);

    when(properties.determineUrl()).thenReturn("jdbc:postgresql://localhost/postgres");
    when(properties.determineUsername()).thenReturn("user");
    when(properties.determinePassword()).thenReturn("pass");
    when(properties.initializeDataSourceBuilder()).thenReturn((DataSourceBuilder) builder);
    when(((DataSourceBuilder<DataSource>) builder).build()).thenReturn(expectedDataSource);

    DatabaseCreationConfiguration configuration = new DatabaseCreationConfiguration();

    DataSource dataSource = configuration.dataSource(properties);

    assertSame(expectedDataSource, dataSource);
  }

  @Test
  void dataSourceBeanCreatesPostgresDatabaseWhenMissing() throws Exception {
    DataSourceProperties properties = Mockito.mock(DataSourceProperties.class);
    @SuppressWarnings({"unchecked", "rawtypes"})
    DataSourceBuilder<?> builder = Mockito.mock(DataSourceBuilder.class);
    DataSource expectedDataSource = Mockito.mock(DataSource.class);
    Connection adminConnection = Mockito.mock(Connection.class);
    PreparedStatement selectStatement = Mockito.mock(PreparedStatement.class);
    PreparedStatement createStatement = Mockito.mock(PreparedStatement.class);
    ResultSet resultSet = Mockito.mock(ResultSet.class);

    when(properties.determineUrl()).thenReturn("jdbc:postgresql://localhost/mydb");
    when(properties.determineUsername()).thenReturn("user");
    when(properties.determinePassword()).thenReturn("pass");
    when(properties.initializeDataSourceBuilder()).thenReturn((DataSourceBuilder) builder);
    when(((DataSourceBuilder<DataSource>) builder).build()).thenReturn(expectedDataSource);

    when(adminConnection.prepareStatement("SELECT 1 FROM pg_database WHERE datname = ?")).thenReturn(selectStatement);
    when(selectStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);
    when(adminConnection.prepareStatement("CREATE DATABASE \"mydb\"" )).thenReturn(createStatement);

    try (MockedStatic<DriverManager> driverManager = Mockito.mockStatic(DriverManager.class)) {
      driverManager.when(() -> DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "user", "pass")).thenReturn(adminConnection);

      DatabaseCreationConfiguration configuration = new DatabaseCreationConfiguration();
      DataSource dataSource = configuration.dataSource(properties);

      assertSame(expectedDataSource, dataSource);
      verify(selectStatement).setString(1, "mydb");
      verify(selectStatement).executeQuery();
      verify(createStatement).execute();
    }
  }

  @Test
  void dataSourceBeanDoesNotCreateDatabaseIfItAlreadyExists() throws Exception {
    DataSourceProperties properties = Mockito.mock(DataSourceProperties.class);
    @SuppressWarnings({"unchecked", "rawtypes"})
    DataSourceBuilder<?> builder = Mockito.mock(DataSourceBuilder.class);
    DataSource expectedDataSource = Mockito.mock(DataSource.class);
    Connection adminConnection = Mockito.mock(Connection.class);
    PreparedStatement selectStatement = Mockito.mock(PreparedStatement.class);
    PreparedStatement createStatement = Mockito.mock(PreparedStatement.class);
    ResultSet resultSet = Mockito.mock(ResultSet.class);

    when(properties.determineUrl()).thenReturn("jdbc:postgresql://localhost/mydb");
    when(properties.determineUsername()).thenReturn("user");
    when(properties.determinePassword()).thenReturn("pass");
    when(properties.initializeDataSourceBuilder()).thenReturn((DataSourceBuilder) builder);
    when(((DataSourceBuilder<DataSource>) builder).build()).thenReturn(expectedDataSource);

    when(adminConnection.prepareStatement("SELECT 1 FROM pg_database WHERE datname = ?")).thenReturn(selectStatement);
    when(selectStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(adminConnection.prepareStatement("CREATE DATABASE \"mydb\"" )).thenReturn(createStatement);

    try (MockedStatic<DriverManager> driverManager = Mockito.mockStatic(DriverManager.class)) {
      driverManager.when(() -> DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "user", "pass")).thenReturn(adminConnection);

      DatabaseCreationConfiguration configuration = new DatabaseCreationConfiguration();
      DataSource dataSource = configuration.dataSource(properties);

      assertSame(expectedDataSource, dataSource);
      verify(selectStatement).setString(1, "mydb");
      verify(selectStatement).executeQuery();
      verify(createStatement, never()).execute();
    }
  }

  @Test
  void dataSourceBeanSkipsDatabaseCreationForNonPostgresUrl() {
    DataSourceProperties properties = Mockito.mock(DataSourceProperties.class);
    @SuppressWarnings({"unchecked", "rawtypes"})
    DataSourceBuilder<?> builder = Mockito.mock(DataSourceBuilder.class);
    DataSource expectedDataSource = Mockito.mock(DataSource.class);

    when(properties.determineUrl()).thenReturn("jdbc:h2:mem:testdb");
    when(properties.determineUsername()).thenReturn("user");
    when(properties.determinePassword()).thenReturn("pass");
    when(properties.initializeDataSourceBuilder()).thenReturn((DataSourceBuilder) builder);
    when(((DataSourceBuilder<DataSource>) builder).build()).thenReturn(expectedDataSource);

    try (MockedStatic<DriverManager> driverManager = Mockito.mockStatic(DriverManager.class)) {
      DatabaseCreationConfiguration configuration = new DatabaseCreationConfiguration();
      DataSource dataSource = configuration.dataSource(properties);

      assertSame(expectedDataSource, dataSource);
      driverManager.verifyNoInteractions();
    }
  }
}
