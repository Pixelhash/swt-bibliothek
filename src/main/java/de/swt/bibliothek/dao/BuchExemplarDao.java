package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;
import de.swt.bibliothek.model.Benutzer;
import de.swt.bibliothek.model.Buch;
import de.swt.bibliothek.model.BuchExemplar;

import java.sql.SQLException;
import java.util.List;

public class BuchExemplarDao extends AbstractDao<BuchExemplar, Integer> {

    public BuchExemplarDao(JdbcConnectionSource connectionSource, Class<BuchExemplar> typeClass) {
        super(connectionSource, typeClass);
    }

    public int getAvailableBookAmount(Buch buch) throws SQLException {
        QueryBuilder<BuchExemplar, Integer> queryBuilder = this.getQueryBuilder();
        queryBuilder.where().eq("buch_id", buch.getId()).and().isNull("benutzer_id");
        List<BuchExemplar> exemplarList = this.getRawDao().query(queryBuilder.prepare());
        return exemplarList.size();
    }

    public List<BuchExemplar> getAusgelieheneBuecher(Benutzer benutzer) throws SQLException {
        QueryBuilder<BuchExemplar, Integer> queryBuilder = this.getQueryBuilder();
        queryBuilder.where().eq("benutzer_id", benutzer.getId());
        return this.getRawDao().query(queryBuilder.prepare());
    }
}
