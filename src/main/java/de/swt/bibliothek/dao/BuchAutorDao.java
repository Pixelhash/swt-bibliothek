package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import de.swt.bibliothek.model.BuchAutor;

public class BuchAutorDao extends AbstractDao<BuchAutor, Void> {

    public BuchAutorDao(JdbcConnectionSource connectionSource, Class<BuchAutor> typeClass) {
        super(connectionSource, typeClass);
    }

}
