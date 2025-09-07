package org.dti.service;

import org.dti.model.Book;
import org.dti.repository.BookRepository;
import org.dti.validation.BookValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class BookServiceTest {
    @Test
    void createValidBookShouldSucceed() {
        BookRepository repo = mock(BookRepository.class);
        when(repo.insert(any())).thenReturn(1);
        BookService service = new BookService(repo, new BookValidator());

        Book b = new Book(null, "Title", "Author", 100, 19.9, LocalDate.now(), null);
        var errs = service.create(b);
        assertTrue(errs.isEmpty());
        assertNotNull(b.getId());
    }

    @Test
    void createInvalidBookShouldReturnErrors() {
        BookRepository repo = mock(BookRepository.class);
        BookService service = new BookService(repo, new BookValidator());

        Book b = new Book(null, "", "", 0, -5.0, null, null);
        var errs = service.create(b);
        assertFalse(errs.isEmpty());
    }
}
