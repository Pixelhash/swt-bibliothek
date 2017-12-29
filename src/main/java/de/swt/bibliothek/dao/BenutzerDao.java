package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import de.swt.bibliothek.model.Benutzer;

import java.sql.SQLException;
import java.util.List;

public class BenutzerDao extends AbstractDao<Benutzer, Integer> {

    public BenutzerDao(JdbcConnectionSource connectionSource, Class<Benutzer> typeClass) {
        super(connectionSource, typeClass);
    }

    /**
     * Looks up the user to the given user id.
     *
     * @param id the id of a user.
     * @return the user if found or null if not found or on error.
     */
    public Benutzer getBenutzer(int id) {
        Benutzer benutzer = null;
        QueryBuilder<Benutzer, Integer> queryBuilder = this.getQueryBuilder();
        try {
            queryBuilder.where().eq("id", id);
            List<Benutzer> benutzerList = this.getRawDao().query(queryBuilder.prepare());
            if (benutzerList.size() == 1) {
                benutzer = benutzerList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return benutzer;
    }

}
