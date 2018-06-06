package de.fhl.swtlibrary;

import de.fhl.swtlibrary.model.Models;
import io.requery.cache.EmptyEntityCache;
import io.requery.cache.EntityCacheBuilder;
import io.requery.meta.EntityModel;
import io.requery.sql.*;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;

public final class DatabaseUtil {

  private static EntityDataStore data;

  static void setUpTestDatabase() throws SQLException, FileNotFoundException {
    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    dataSource.setUser("sa");
    dataSource.setPassword("");
    EntityModel model = Models.DEFAULT;
    new SchemaModifier(dataSource, model).createTables(TableCreationMode.CREATE_NOT_EXISTS);
    Configuration configuration = new ConfigurationBuilder(dataSource, model)
      .setEntityCache(new EmptyEntityCache())
      .useDefaultLogging()
      .build();
    RunScript.execute(dataSource.getConnection(), new FileReader("src/test/resources/db-demo-data.sql"));
    data = new EntityDataStore<>(configuration);
  }

  public static EntityDataStore getData() {
    return data;
  }
}
