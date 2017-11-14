package de.swt.bibliothek.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

public abstract class AbstractDao<T, ID> {

    private Dao<T, ID> dao;

    public AbstractDao() {
        this.dao = DaoManager
    }

}
