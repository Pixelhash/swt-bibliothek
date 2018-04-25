package de.swt.bibliothek.database;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import lombok.Getter;

import java.io.IOException;
import java.sql.SQLException;

public class AbstractDatabase {

    @Getter
    private JdbcPooledConnectionSource connectionSource;

    public AbstractDatabase(String connectionString) throws SQLException {
        this.connectionSource = new JdbcPooledConnectionSource(connectionString);
    }

    public void close() throws IOException {
        this.connectionSource.close();
    }

}
