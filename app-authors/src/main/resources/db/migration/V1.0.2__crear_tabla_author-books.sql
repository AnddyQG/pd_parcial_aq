CREATE TABLE books_authors
(
    books_isbn VARCHAR(255) NOT NULL,
    authors_id INTEGER      NOT NULL,
    CONSTRAINT pk_books_authors PRIMARY KEY (books_isbn, authors_id)
);

ALTER TABLE books_authors
    ADD CONSTRAINT FK_BOOKS_AUTHORS_ON_AUTHORS FOREIGN KEY (authors_id) REFERENCES authors (id);