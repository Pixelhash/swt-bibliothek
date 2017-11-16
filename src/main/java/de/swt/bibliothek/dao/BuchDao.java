package de.swt.bibliothek.dao;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import de.swt.bibliothek.Application;
import de.swt.bibliothek.model.Autor;
import de.swt.bibliothek.model.Buch;
import de.swt.bibliothek.model.BuchAutor;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BuchDao extends AbstractDao<Buch, Integer> {

    public BuchDao(JdbcConnectionSource connectionSource, Class<Buch> typeClass) {
        super(connectionSource, typeClass);
    }

    @Override
    public List<Buch> getAll() {
        return this.addExemplare(this.addAuthors(super.getAll()));
    }

    public List<Buch> getSearchedBooks(String query) throws SQLException {
        QueryBuilder<Buch, Integer> queryBuilder = Application.buchDao.getQueryBuilder();
        queryBuilder.where().like("titel", "%" + query + "%");
        List<Buch> results = Application.buchDao.getRawDao().query(queryBuilder.prepare());
        return this.addExemplare(this.addAuthors(results));
    }

    private List<Buch> addAuthors(List<Buch> buecher) {
        buecher.forEach(b -> {
            QueryBuilder<BuchAutor, Void> queryBuilder = Application.buchAutorDao.getQueryBuilder();
            try {
                queryBuilder.where().eq("buch_id", b.getId());
                List<BuchAutor> buchAutoren = Application.buchAutorDao.getRawDao().query(queryBuilder.prepare());
                b.setAutoren(buchAutoren.stream().map(BuchAutor::getAutor).collect(Collectors.toList()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return buecher;
    }

    private List<Buch> addExemplare(List<Buch> buecher) {
        buecher.forEach(b -> {
            try {
                b.setExemplare(Application.buchExemplarDao.getAvailableBookAmount(b));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return buecher;
    }
}
