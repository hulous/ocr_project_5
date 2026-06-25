package com.openclassrooms.mddapi.configurations;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;

import static org.junit.jupiter.api.Assertions.assertSame;
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
}
