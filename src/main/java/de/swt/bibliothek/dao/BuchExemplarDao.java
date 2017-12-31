package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;
import de.swt.bibliothek.Application;
import de.swt.bibliothek.model.Benutzer;
import de.swt.bibliothek.model.Buch;
import de.swt.bibliothek.model.BuchExemplar;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

    /**
     * Looks up the book exemplar to the given book exemplar id.
     *
     * @param id the id of a book exemplar.
     * @return the book exemplar if found or null if not found or on error.
     */
    public BuchExemplar getBuchExemplar(int id) {
        BuchExemplar buchExemplar = null;
        QueryBuilder<BuchExemplar, Integer> queryBuilder = this.getQueryBuilder();
        try {
            queryBuilder.where().eq("id", id);
            List<BuchExemplar> buchExemplarList = this.getRawDao().query(queryBuilder.prepare());
            if (buchExemplarList.size() == 1) {
                buchExemplar = buchExemplarList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buchExemplar;
    }

    /**
     * Borrows a book exemplar for a given user.
     *
     * @param benutzer the user.
     * @param buchExemplar the book exemplar to borrow.
     * @return true if successfully borrowed, false if already borrowed or on error.
     */
    public boolean borrow(Benutzer benutzer, BuchExemplar buchExemplar) {
        if (buchExemplar.getBenutzer() != null) return false;

        buchExemplar.setBenutzer(benutzer);
        buchExemplar.setAusleihdatum(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 30);
        buchExemplar.setRueckgabedatum(calendar.getTime());
        try {
            Application.buchExemplarDao.getRawDao().update(buchExemplar);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Returns a book exemplar from a given user.
     *
     * @param benutzer the user.
     * @param buchExemplar the book exemplar to return.
     * @return true if successfully returned, false if not borrowed or on error.
     */
    public boolean returnBook(Benutzer benutzer, BuchExemplar buchExemplar) {
        if (buchExemplar.getBenutzer() == null) return false;

        buchExemplar.setBenutzer(null);
        buchExemplar.setAusleihdatum(null);
        buchExemplar.setRueckgabedatum(null);
        try {
            Application.buchExemplarDao.getRawDao().update(buchExemplar);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<BuchExemplar> getOfBook(int bookId) {
        QueryBuilder<BuchExemplar, Integer> queryBuilder = this.getQueryBuilder();
        List<BuchExemplar> ret = null;
        try {
            queryBuilder.where().eq("buch_id", bookId);
            ret = this.getRawDao().query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
