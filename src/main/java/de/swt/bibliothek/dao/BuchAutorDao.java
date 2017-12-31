package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import de.swt.bibliothek.model.BuchAutor;

import java.sql.SQLException;
import java.util.List;

public class BuchAutorDao extends AbstractDao<BuchAutor, Void> {

    public BuchAutorDao(JdbcConnectionSource connectionSource, Class<BuchAutor> typeClass) {
        super(connectionSource, typeClass);
    }

}
