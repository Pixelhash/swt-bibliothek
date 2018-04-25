package de.swt.bibliothek.database;

import java.sql.SQLException;

public class MysqlDatabase extends AbstractDatabase {

    // Pattern is HOSTNAME -> PORT -> DATABASE -> USER -> PASSWORD
    private final static String MYSQL_JDBC_CONNECTION_STRING =
        "jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false&autoReconnect=true";

    public MysqlDatabase(String connectionString) throws SQLException {
        super(connectionString);
    }

    public static String createConnectionString(String hostname, int port, String databaseName, String user, String password) {
        return String.format(MYSQL_JDBC_CONNECTION_STRING,
            hostname, port, databaseName, user, password);
    }
}
