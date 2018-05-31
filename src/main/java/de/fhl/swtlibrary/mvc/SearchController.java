package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.Book;
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

import java.util.List;

@Path("/")
public class SearchController {

  private Request req;
  private EntityStore<Persistable, Book> bookEntityStore;

  @Inject
  public SearchController(Request req, EntityStore<Persistable, Book> bookEntityStore) {
    this.req = req;
    this.bookEntityStore = bookEntityStore;
  }

  @GET
  public Result getBasicSearch() {
    return Results.html("pages/index");
  }

  @POST
  public Result postBasicSearch() {
    String query = req.param("query").value();

    if (!Validation.isNonEmptyString(query)) {
      return RenderUtil.error(req, Paths.BOOK_SEARCH,"ERROR_MISSING_FIELDS");
    } else if (query.trim().length() < 3) {
      return RenderUtil.error(req, Paths.BOOK_SEARCH, "ERROR_SHORT_QUERY");
    }

    List<Book> books = bookEntityStore.select(Book.class)
      .where(Book.TITLE.lower().like("%" + query.toLowerCase() + "%"))
      .get()
      .toList();

    if (books.isEmpty()) {
      return RenderUtil.error(req, Paths.BOOK_SEARCH, "ERROR_NO_RESULTS");
    }

    books.forEach(b -> bookEntityStore.refresh(b, Book.AUTHORS));
    books.forEach(b -> bookEntityStore.refresh(b, Book.COPIES));
    req.set("query", query);
    req.set("books", books);
    return Results.html("pages/search_results");
  }

}
