package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import de.swt.bibliothek.model.Adresse;

public class AdresseDao extends AbstractDao<Adresse, Integer> {

    public AdresseDao(JdbcConnectionSource connectionSource, Class<Adresse> typeClass) {
        super(connectionSource, typeClass);
    }

}
