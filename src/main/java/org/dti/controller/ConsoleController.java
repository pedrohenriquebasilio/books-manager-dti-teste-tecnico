package org.dti.controller;

import org.dti.model.Book;
import org.dti.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleController {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleController.class);
    private final BookService service;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleController(BookService service) {
        this.service = service;
        logger.info("ConsoleController inicializado");
    }

    public void run() {
        logger.info("Iniciando aplicação ConsoleController");
        printHeader();
        while (true) {
            showMenu();
            String cmd = scanner.nextLine().trim();
            logger.info("Comando recebido: {}", cmd);
            switch (cmd) {
                case "1" -> listAll();
                case "2" -> getById();
                case "3" -> create();
                case "4" -> update();
                case "5" -> delete();
                case "0" -> {
                    logger.info("Encerrando aplicação");
                    System.out.println("Saindo..."); 
                    return;
                }
                default -> {
                    logger.warn("Opção inválida recebida: {}", cmd);
                    System.out.println("Opção inválida");
                }
            }
        }
    }

    private void printHeader() { 
        logger.debug("Exibindo cabeçalho");
        System.out.println("=== Book Manager (console) ==="); 
    }

    private void showMenu() {
        logger.debug("Exibindo menu");
        System.out.println("\nMenu:");
        System.out.println("1 - Listar todos");
        System.out.println("2 - Buscar por ID");
        System.out.println("3 - Cadastrar livro");
        System.out.println("4 - Atualizar livro");
        System.out.println("5 - Deletar livro");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private void listAll() {
        logger.info("Executando listAll()");
        List<Book> list = service.listAll();
        if (list.isEmpty()) {
            logger.info("Nenhum registro encontrado");
            System.out.println("Nenhum registro.");
        }
        else {
            logger.info("Exibindo {} registros", list.size());
            list.forEach(b -> System.out.println(b));
        }
    }

    private void getById() {
        System.out.print("ID: ");
        String idInput = scanner.nextLine();
        logger.info("Executando getById() com entrada: {}", idInput);
        try {
            int id = Integer.parseInt(idInput);
            Optional<Book> ob = service.findById(id);
            if (ob.isPresent()) {
                logger.info("Livro encontrado: {}", ob.get());
                System.out.println(ob.get());
            } else {
                logger.info("Livro não encontrado para ID: {}", id);
                System.out.println("Não encontrado");
            }
        } catch (NumberFormatException e) {
            logger.error("Erro ao converter ID para inteiro: {}", idInput, e);
            System.out.println("ID inválido");
        }
    }

    private void create() {
        logger.info("Iniciando processo de criação de livro");
        Book b = readBookFromInput(false);
        logger.info("Dados do livro coletados: {}", b);
        var errs = service.create(b);
        if (errs.isEmpty()) {
            logger.info("Livro criado com sucesso. ID: {}", b.getId());
            System.out.println("Criado com id=" + b.getId());
        }
        else {
            logger.warn("Erros na criação do livro: {}", errs);
            errs.forEach(e -> System.out.println("Erro: " + e));
        }
    }

    private void update() {
        System.out.print("ID do livro a atualizar: ");
        String idInput = scanner.nextLine();
        logger.info("Iniciando processo de atualização para ID: {}", idInput);
        try {
            int id = Integer.parseInt(idInput);
            Book b = readBookFromInput(true);
            b.setId(id);
            logger.info("Dados do livro coletados para atualização: {}", b);
            var errs = service.update(b);
            if (errs.isEmpty()) {
                logger.info("Livro atualizado com sucesso. ID: {}", id);
                System.out.println("Atualizado");
            }
            else {
                logger.warn("Erros na atualização do livro: {}", errs);
                errs.forEach(e -> System.out.println("Erro: " + e));
            }
        } catch (NumberFormatException e) {
            logger.error("Erro ao converter ID para inteiro: {}", idInput, e);
            System.out.println("ID inválido");
        }
    }

    private void delete() {
        System.out.print("ID para deletar: ");
        String idInput = scanner.nextLine();
        logger.info("Iniciando processo de exclusão para ID: {}", idInput);
        try {
            int id = Integer.parseInt(idInput);
            boolean success = service.delete(id);
            if (success) {
                logger.info("Livro deletado com sucesso. ID: {}", id);
                System.out.println("Deletado");
            }
            else {
                logger.warn("Livro não encontrado para exclusão. ID: {}", id);
                System.out.println("Não encontrado");
            }
        } catch (NumberFormatException e) {
            logger.error("Erro ao converter ID para inteiro: {}", idInput, e);
            System.out.println("ID inválido");
        }
    }

    private Book readBookFromInput(boolean forUpdate) {
        logger.debug("Lendo dados do livro do input. forUpdate={}", forUpdate);
        System.out.print("Título: ");
        String title = scanner.nextLine().trim();
        System.out.print("Autor: ");
        String author = scanner.nextLine().trim();
        System.out.print("Páginas: ");
        String pagesInput = scanner.nextLine().trim();
        Integer pages = null;
        try {
            pages = Integer.parseInt(pagesInput);
        } catch (NumberFormatException e) {
            logger.warn("Erro ao converter páginas para inteiro: {}", pagesInput);
        }
        
        System.out.print("Preço (ou vazio): ");
        String priceStr = scanner.nextLine().trim();
        Double price = null;
        if (!priceStr.isBlank()) {
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                logger.warn("Erro ao converter preço para double: {}", priceStr);
            }
        }
        
        System.out.print("Data publicação (yyyy-MM-dd) (ou vazio): ");
        String pd = scanner.nextLine().trim();
        LocalDate publishedDate = null;
        if (!pd.isBlank()) {
            try {
                publishedDate = LocalDate.parse(pd);
            } catch (Exception e) {
                logger.warn("Erro ao converter data de publicação: {}", pd);
            }
        }
        
        System.out.print("Descrição (opcional): ");
        String desc = scanner.nextLine().trim();
        if (desc.isBlank()) desc = null;
        
        Book book = new Book(null, title, author, pages, price, publishedDate, desc);
        logger.debug("Livro criado a partir do input: {}", book);
        return book;
    }
}
