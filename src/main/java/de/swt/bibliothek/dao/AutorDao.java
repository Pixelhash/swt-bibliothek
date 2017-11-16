package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import de.swt.bibliothek.model.Autor;

public class AutorDao extends AbstractDao<Autor, Integer> {

    public AutorDao(JdbcConnectionSource connectionSource, Class<Autor> typeClass) {
        super(connectionSource, typeClass);
    }

}
