package de.swt.bibliothek.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao<T, ID> {

    protected Dao<T, ID> dao;

    public AbstractDao(JdbcConnectionSource connectionSource, Class<T> typeClass) {
        try {
            this.dao = DaoManager.createDao(connectionSource, typeClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<T, ID> getRawDao() {
        return dao;
    }

    public QueryBuilder<T, ID> getQueryBuilder() {
        return this.dao.queryBuilder();
    }

    public List<T> getAll() {
        try {
            return this.dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
