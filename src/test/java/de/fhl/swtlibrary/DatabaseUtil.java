package de.fhl.swtlibrary;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.*;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.EntityModel;
import io.requery.sql.*;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Date;

public final class DatabaseUtil {

  @Inject
  private static EntityStore<Persistable, Book> bookEntityStore;

  private static EntityDataStore data;

  public static void setUpTestDatabase() throws SQLException, FileNotFoundException {
    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    dataSource.setUser("sa");
    dataSource.setPassword("");
    EntityModel model = Models.DEFAULT;
    new SchemaModifier(dataSource, model).createTables(TableCreationMode.CREATE_NOT_EXISTS);
    Configuration configuration = new ConfigurationBuilder(dataSource, model)
      .useDefaultLogging()
      .build();
    RunScript.execute(dataSource.getConnection(), new FileReader("src/test/resources/db-demo-data.sql"));
    data = new EntityDataStore<>(configuration);
  }

  public static EntityDataStore getData() {
    return data;
  }

  public static void insertAuthors() {
    Author a1 = new Author();
    a1.setName("Hans Schillinger");

    Author a2 = new Author();
    a2.setName("Petra Wulfs");

    Author a3 = new Author();
    a3.setName("Michael-Hans Teseros");

    data.insert(a1);
    data.insert(a2);
    data.insert(a3);
  }

  public static void insertCategories() {
    Category c1 = new Category();
    c1.setName("Roman");

    Category c2 = new Category();
    c2.setName("Naturwissenschaften");

    Category c3 = new Category();
    c3.setName("Kinderbuch");

    data.insert(c1);
    data.insert(c2);
    data.insert(c3);
  }

  public static void insertPublishers() {
    Publisher p1 = new Publisher();
    p1.setName("Berliner Verlag");
    p1.setLocation("Berlin");

    Publisher p2 = new Publisher();
    p2.setName("Kölner Verlag");
    p2.setLocation("Köln");

    Publisher p3 = new Publisher();
    p3.setName("Hamburger Verlag");
    p3.setLocation("Hamburg");

    data.insert(p1);
    data.insert(p2);
    data.insert(p3);
  }

  public static void insertTestBooks() {
    EntityStore<Persistable, Category> categoryEntityStore = data;
    EntityStore<Persistable, Publisher> publisherEntityStore = data;

    Book b1 = new Book();
    b1.setTitle("Mathebuch");
    b1.setIsbn("1112223334445");
    b1.setReleaseYear((short) 2017);
    b1.setLocation("R12");
    b1.setCategory(categoryEntityStore.select(Category.class)
      .where(Category.NAME.eq("Roman"))
      .get()
      .first());
    b1.setPublisher(publisherEntityStore.select(Publisher.class)
      .where(Publisher.NAME.eq("Kölner Verlag"))
      .get()
      .first());

    data.insert(b1);
  }

  public static void insertBookAuthors() {
    EntityStore<Persistable, Book> bookEntityStore = data;
    EntityStore<Persistable, Author> authorEntityStore = data;

    BookAuthor ba1 = new BookAuthor();
    ba1.setAuthorId(authorEntityStore.select(Author.class)
      .where(Author.NAME.eq("Hans Schillinger"))
      .get()
      .first().getId());
    ba1.setBookId(bookEntityStore.select(Book.class)
      .where(Book.TITLE.eq("Mathebuch"))
      .get()
      .first().getId());

    data.insert(ba1);
  }
}
