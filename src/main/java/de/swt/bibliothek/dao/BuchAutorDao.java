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

    public boolean delete(int bookId) {
        QueryBuilder<BuchAutor, Void> queryBuilder = this.getQueryBuilder();
        try {
            queryBuilder.where().eq("buch_id", bookId);
            List<BuchAutor> buchAutorList = this.getRawDao().query(queryBuilder.prepare());
            for (BuchAutor buchAutor : buchAutorList) {
                this.getRawDao().delete(buchAutor);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
