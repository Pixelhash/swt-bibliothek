package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import de.swt.bibliothek.model.Kategorie;

public class KategorieDao extends AbstractDao<Kategorie, Integer> {

    public KategorieDao(JdbcConnectionSource connectionSource, Class<Kategorie> typeClass) {
        super(connectionSource, typeClass);
    }

}
