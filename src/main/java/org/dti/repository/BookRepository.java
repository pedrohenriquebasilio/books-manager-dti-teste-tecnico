package org.dti.repository;


import org.dti.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(int id);
    int insert(Book book);
    boolean update(Book book);
    boolean delete(int id);
}
