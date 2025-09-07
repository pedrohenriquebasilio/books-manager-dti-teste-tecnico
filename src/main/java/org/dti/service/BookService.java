package org.dti.service;

import org.dti.model.Book;
import org.dti.repository.BookRepository;
import org.dti.validation.BookValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository repo;
    private final BookValidator validator;

    public BookService(BookRepository repo, BookValidator validator) {
        this.repo = repo;
        this.validator = validator;
        logger.info("BookService inicializado");
    }

    public List<String> create(Book book) {
        logger.info("Tentativa de criar livro: {}", book);
        var errs = validator.validateForCreate(book);
        if (!errs.isEmpty()) {
            logger.warn("Falha na validação para criação do livro: {}", errs);
            return errs;
        }
        int id = repo.insert(book);
        book.setId(id);
        logger.info("Livro criado com sucesso. ID: {}", id);
        return List.of();
    }

    public List<Book> listAll() {
        logger.info("Listando todos os livros");
        List<Book> books = repo.findAll();
        logger.info("Total de livros encontrados: {}", books.size());
        return books;
    }

    public Optional<Book> findById(int id) {
        logger.info("Buscando livro por ID: {}", id);
        Optional<Book> book = repo.findById(id);
        if (book.isPresent()) {
            logger.info("Livro encontrado: {}", book.get());
        } else {
            logger.info("Livro não encontrado para o ID: {}", id);
        }
        return book;
    }

    public List<String> update(Book book) {
        logger.info("Tentativa de atualizar livro: {}", book);
        var errs = validator.validateForUpdate(book);
        if (!errs.isEmpty()) {
            logger.warn("Falha na validação para atualização do livro: {}", errs);
            return errs;
        }
        boolean ok = repo.update(book);
        if (!ok) {
            logger.warn("Recurso não encontrado para update. ID: {}", book.getId());
            return List.of("Recurso não encontrado para update");
        }
        logger.info("Livro atualizado com sucesso. ID: {}", book.getId());
        return List.of();
    }

    public boolean delete(int id) {
        logger.info("Tentativa de deletar livro. ID: {}", id);
        boolean deleted = repo.delete(id);
        if (deleted) {
            logger.info("Livro deletado com sucesso. ID: {}", id);
        } else {
            logger.warn("Falha ao deletar livro. ID não encontrado: {}", id);
        }
        return deleted;
    }
}
