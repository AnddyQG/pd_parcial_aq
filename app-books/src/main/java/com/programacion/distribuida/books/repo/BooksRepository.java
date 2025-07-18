package com.programacion.distribuida.books.repo;

import com.programacion.distribuida.books.db.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class BooksRepository implements PanacheRepositoryBase<Book, String> {

    public Book findById(String isbn) {
        String query = "SELECT o FROM Book o WHERE o.isbn=?1";
        return this.find(query, isbn).firstResult();
    }

    public void update(String isbn, Book book) {
        this.findByIdOptional(isbn)
                .ifPresent(existingBook -> {
                    existingBook.setTitle(book.getTitle());
                    existingBook.setPrice(book.getPrice());
                    existingBook.setVersion(existingBook.getVersion()+1);
                });
    }
}
