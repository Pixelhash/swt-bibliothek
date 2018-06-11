package de.fhl.swtlibrary.mvc;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.Book;
import de.fhl.swtlibrary.model.Category;
import de.fhl.swtlibrary.util.Paths;
import de.fhl.swtlibrary.util.RenderUtil;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;

import java.util.ArrayList;
import java.util.List;

@Path("/categories")
public class CategoriesController {

  private Request req;
  private EntityStore<Persistable, Book> bookEntityStore;
  private EntityStore<Persistable, Category> categoryEntityStore;

  @Inject
  public CategoriesController(Request req, EntityStore<Persistable, Book> bookEntityStore, EntityStore<Persistable, Category> categoryEntityStore) {
    this.req = req;
    this.bookEntityStore = bookEntityStore;
    this.categoryEntityStore = categoryEntityStore;
  }

  @GET
  public Result getBasicSearch() {
    List<Book> books = bookEntityStore.select(Book.class)
      .get()
      .toList();

    List<Category> categories = categoryEntityStore.select(Category.class)
      .get()
      .toList();

    // Copy lists to be able to sort it, because the above lists are immutable
    List<Book> booksCopy = new ArrayList<>(books);
    List<Category> categoriesCopy = new ArrayList<>(categories);

    booksCopy.sort((b1, b2) -> {
      if (b1.getTitle() == null || b2.getTitle() == null) {
        return 0;
      }
      return b1.getTitle().compareTo(b2.getTitle());
    });

    categoriesCopy.sort((c1, c2) -> {
      if (c1.getName() == null || c2.getName() == null) {
        return 0;
      }
      return c1.getName().compareTo(c2.getName());
    });

    if (books.isEmpty() || categories.isEmpty()) {
      return RenderUtil.error(req, Paths.BOOK_CATEGORIES, "ERROR_NO_RESULTS");
    }

    books.forEach(b -> bookEntityStore.refresh(b, Book.AUTHORS));

    req.set("categories", categoriesCopy);
    req.set("books", booksCopy);
    return Results.html("pages/categories");
  }
}
