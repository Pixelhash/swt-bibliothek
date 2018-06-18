package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.Book;
import de.fhl.swtlibrary.model.Category;
import de.fhl.swtlibrary.util.RenderUtil;
import de.fhl.swtlibrary.util.Paths;
import de.fhl.swtlibrary.util.Validation;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/")
public class SearchController {

  private Request req;
  private EntityStore<Persistable, Book> bookEntityStore;
  private EntityStore<Persistable, Category> categoryEntityStore;

  @Inject
  public SearchController(Request req,
                          EntityStore<Persistable, Book> bookEntityStore,
                          EntityStore<Persistable, Category> categoryEntityStore) {
    this.req = req;
    this.bookEntityStore = bookEntityStore;
    this.categoryEntityStore = categoryEntityStore;
  }

  @GET
  public Result getBasicSearch() {
    List<Category> categories = categoryEntityStore.select(Category.class)
      .get()
      .toList();

    req.set("categories", categories);

    return Results.html("pages/index");
  }

  @GET
  @Path(Paths.BOOK_SEARCH)
  public Result getBasicSearchResults() {

    if (!req.param("query").isSet()) {
      return RenderUtil.error(req, Paths.BOOK_INDEX,"ERROR_MISSING_FIELDS");
    }

    String query = req.param("query").value();
    String category;

    if (!Validation.isNonEmptyString(query)) {
      return RenderUtil.error(req, Paths.BOOK_INDEX,"ERROR_MISSING_FIELDS");
    } else if (query.trim().length() < 3) {
      return RenderUtil.error(req, Paths.BOOK_INDEX, "ERROR_SHORT_QUERY");
    }

    if (!req.param("category").isSet()) {
      category = "all";
    } else {
      category = req.param("category").value();
    }

    List<Book> books;

    if (category.equals("all")) {
      books = bookEntityStore.select(Book.class)
        .where(Book.TITLE.lower().like("%" + query.toLowerCase() + "%"))
        .get()
        .toList();
    } else {
      books = bookEntityStore.select(Book.class)
        .where(Book.TITLE.lower().like("%" + query.toLowerCase() + "%"))
        .get()
        .toList();
      books = new ArrayList<>(books).stream().filter(book -> book.getCategory().getName().toLowerCase().equals(category)).collect(Collectors.toList());
    }

    if (books.isEmpty()) {
      return RenderUtil.error(req, Paths.BOOK_INDEX, "ERROR_NO_RESULTS");
    }

    books.forEach(b -> bookEntityStore.refresh(b, Book.AUTHORS));
    books.forEach(b -> bookEntityStore.refresh(b, Book.COPIES));
    req.set("query", query);
    req.set("books", books);
    return Results.html("pages/search_results");
  }

}
