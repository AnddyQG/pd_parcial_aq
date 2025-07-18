package com.programacion.distribuida.authors.repo;

import com.programacion.distribuida.authors.db.Author;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class AuthorRepo implements PanacheRepositoryBase<Author, Integer> {

    public List<Author> findByBook(String isbn) {
        String query = "SELECT o.author FROM BookAuthor o WHERE o.id.bookIsbn=?1";
        return this.list(query, isbn);
    }


    /*
    Devuelve un objeto Optional<Author> que contiene el autor actualizado
    si la operación es exitosa, o un Optional.empty() si no se encuentra
    el autor
     */
    public Optional<Author> update(Integer id, Author author) {
        var obj = this.findByIdOptional(id);
        if (obj.isEmpty()) {
            return Optional.empty();
        }

        var authorObj = obj.get();
        authorObj.setName(author.getName());
        authorObj.setVersion(author.getVersion());
        return Optional.of(authorObj);
    }

}
