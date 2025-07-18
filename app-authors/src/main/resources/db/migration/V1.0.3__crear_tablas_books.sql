CREATE TABLE books
(
    isbn    VARCHAR(255) NOT NULL,
    title   VARCHAR(128),
    price   DECIMAL(12, 2),
    version INTEGER,
    CONSTRAINT pk_books PRIMARY KEY (isbn)
);

insert into books (isbn, title, price, version) values('1111', 'libro1', 10.00, 1);
insert into books (isbn, title, price, version) values('2222', 'libro2', 20.00, 1);
insert into books (isbn, title, price, version) values('3333', 'libro3', 30.00, 1);
insert into books (isbn, title, price, version) values('4444', 'libro4', 40.00, 1);

CREATE TABLE inventory
(
    isbn     VARCHAR(255) NOT NULL,
    sold     INTEGER,
    supplied INTEGER,
    CONSTRAINT pk_inventory PRIMARY KEY (isbn)
);

ALTER TABLE inventory
    ADD CONSTRAINT FK_INVENTORY_ON_ISBN FOREIGN KEY (isbn) REFERENCES books (isbn);

ALTER TABLE books_authors
    ADD CONSTRAINT FK_BOOKS_AUTHORS_ON_BOOKS FOREIGN KEY (books_isbn) REFERENCES books (isbn);