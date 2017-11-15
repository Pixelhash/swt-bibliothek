package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import de.swt.bibliothek.model.Buch;

public class BuchDao extends AbstractDao<Buch, Integer> {

    public BuchDao(JdbcConnectionSource connectionSource, Class<Buch> typeClass) {
        super(connectionSource, typeClass);
    }
}
