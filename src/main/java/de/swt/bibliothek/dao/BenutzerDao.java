package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import de.swt.bibliothek.model.Benutzer;

public class BenutzerDao extends AbstractDao<Benutzer, Integer> {

    public BenutzerDao(JdbcConnectionSource connectionSource, Class<Benutzer> typeClass) {
        super(connectionSource, typeClass);
    }

}
