package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.*;
import de.fhl.swtlibrary.util.*;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

import java.util.List;


@Path("/book/add")
public class AddBookController {
  private Request req;
  private EntityStore<Persistable, Book> bookEntityStore;
  private EntityStore<Persistable, Category> categoryEntityStore;
  private EntityStore<Persistable, Publisher> publisherEntityStore;

  @Inject
  public AddBookController(Request req,
                                EntityStore<Persistable, Book> bookEntityStore,
                                EntityStore<Persistable, Category> categoryEntityStore,
                                EntityStore<Persistable, Publisher> publisherEntityStore) {
    this.req = req;
    this.bookEntityStore = bookEntityStore;
    this.categoryEntityStore = categoryEntityStore;
    this.publisherEntityStore = publisherEntityStore;
  }

  @GET
  public Result getAddBook() {
    final List<Category> cList = categoryEntityStore
      .select(Category.class)
      .orderBy(Category.NAME)
      .get()
      .toList();
    final List<Publisher> pList = publisherEntityStore
      .select(Publisher.class)
      .orderBy(Publisher.NAME)
      .get()
      .toList();

    req.set("cList", cList);
    req.set("pList", pList);

    return Results.html("pages/bookadd");
  }

  @POST
  public Result postAddBook() {
    String title = req.param("title").value();
    String isbn = req.param("isbn").value();
    String releaseYearString = req.param("release_year").value();
    String location = req.param("location").value();
    String category = req.param("category").value();
    String publisher = req.param("publisher").value();

    Tuple<Boolean, Integer> validYear = Validation.isValidInt(releaseYearString);

    // Check if all passed parameters are valid
    if (!Validation.isNonEmptyString(title)
      || !Validation.isNonEmptyString(isbn)
      || !Validation.isNonEmptyString(location)
      || !Validation.isNonEmptyString(category)
      || !Validation.isNonEmptyString(publisher)
      || !validYear.getFirstValue()) {
      return RenderUtil.error(req, Paths.BOOK_ADD, "ERROR_MISSING_FIELDS");
    }

    final short releaseYear = validYear.getSecondValue().shortValue();

    //Get Category and Publisher by given ID
    final Category cat = categoryEntityStore.select(Category.class)
      .where(Category.ID.equal(Integer.parseInt(category)))
      .get()
      .first();
    final Publisher pub = publisherEntityStore.select(Publisher.class)
      .where(Publisher.ID.equal(Integer.parseInt(publisher)))
      .get()
      .first();

    //Create Book and fill it
    Book book = new Book();
    book.setTitle(title);
    book.setIsbn(isbn);
    book.setReleaseYear(releaseYear);
    book.setLocation(location);
    book.setCategory(cat);
    book.setPublisher(pub);

    // Insert Book into Database
    bookEntityStore.insert(book);

    return RenderUtil.success(req, Paths.BOOK_ADD, "BOOK_ADD_SUCCESS");
  }
}
