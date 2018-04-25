package de.swt.bibliothek.database;

import java.sql.SQLException;

public class SqliteDatabase extends AbstractDatabase {

    // Pattern is DATABASE_FILE_PATH
    private static final String SQLITE_JDBC_CONNECTION_STRING = "jdbc:sqlite:%s";

    public SqliteDatabase(String connectionString) throws SQLException {
        super(connectionString);
    }

    public static String createConnectionString(String databaseFilePath) {
        return String.format(SQLITE_JDBC_CONNECTION_STRING, databaseFilePath);
    }

}
