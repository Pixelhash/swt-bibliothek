package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import de.swt.bibliothek.model.Verlag;

public class VerlagDao extends AbstractDao<Verlag, Integer> {

    public VerlagDao(JdbcConnectionSource connectionSource, Class<Verlag> typeClass) {
        super(connectionSource, typeClass);
    }

}
