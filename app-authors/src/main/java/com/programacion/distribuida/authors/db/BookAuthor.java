package com.programacion.distribuida.authors.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="books_authors")
@Getter @Setter
public class BookAuthor {
    @EmbeddedId
    private BookAuthorId id;

    @ManyToOne // muchos registros de la entidad BookAuthor pueden estar asociados a un solo registro de la entidad Author
    @MapsId("authorId")
    @JoinColumn(name="authors_id", nullable = false)
    private Author author;
}
