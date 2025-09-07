package org.dti.validation;

import org.dti.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BookValidator {
    private static final Logger logger = LoggerFactory.getLogger(BookValidator.class);

    public BookValidator() {
        logger.info("BookValidator inicializado");
    }

    public List<String> validateForCreate(Book book) {
        logger.info("Validando livro para criação: {}", book);
        List<String> errors = validateCommon(book);
        if (errors.isEmpty()) {
            logger.info("Validação para criação concluída sem erros");
        } else {
            logger.warn("Validação para criação falhou com {} erros: {}", errors.size(), errors);
        }
        return errors;
    }

    public List<String> validateForUpdate(Book book) {
        logger.info("Validando livro para atualização: {}", book);
        List<String> errors = new ArrayList<>();
        if (book.getId() == null) {
            logger.warn("Validação falhou: ID é obrigatório para update");
            errors.add("id é obrigatório para update");
        }
        errors.addAll(validateCommon(book));
        if (errors.isEmpty()) {
            logger.info("Validação para atualização concluída sem erros");
        } else {
            logger.warn("Validação para atualização falhou com {} erros: {}", errors.size(), errors);
        }
        return errors;
    }

    private List<String> validateCommon(Book book) {
        logger.debug("Executando validação comum para: {}", book);
        List<String> errors = new ArrayList<>();
        
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            logger.debug("Validação falhou: título é obrigatório");
            errors.add("title é obrigatório");
        }
        
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            logger.debug("Validação falhou: autor é obrigatório");
            errors.add("author é obrigatório");
        }
        
        if (book.getPages() == null) {
            logger.debug("Validação falhou: páginas é obrigatório");
            errors.add("pages é obrigatório");
        } else if (book.getPages() <= 0) {
            logger.debug("Validação falhou: páginas deve ser maior que zero");
            errors.add("pages deve ser maior que zero");
        }
        
        if (book.getPrice() != null && book.getPrice() < 0) {
            logger.debug("Validação falhou: preço não pode ser negativo");
            errors.add("price não pode ser negativo");
        }
        
        return errors;
    }
}
