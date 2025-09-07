package org.dti;

import org.dti.controller.ConsoleController;
import org.dti.infra.Database;
import org.dti.repository.BookRepository;
import org.dti.repository.SqliteBookRepository;
import org.dti.service.BookService;
import org.dti.validation.BookValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) throws Exception {
        logger.info("Iniciando aplicação Books Manager");
        
        String dbPath = "data/books.db";
        logger.info("Criando diretório de dados se não existir");
        Files.createDirectories(Path.of("data"));

        logger.info("Inicializando conexão com o banco de dados: {}", dbPath);
        Database db = new Database(dbPath);
        try (Connection conn = db.getConnection()) {
            logger.info("Executando script SQL para criação de tabelas");
            var sql = Files.readString(Path.of("sql/create_tables.sql"));
            conn.createStatement().execute(sql);

            logger.info("Inicializando repositório, serviço e controller");
            BookRepository repo = new SqliteBookRepository(conn);
            var service = new BookService(repo, new BookValidator());
            var controller = new ConsoleController(service);
            
            logger.info("Iniciando interface de console");
            controller.run();
            
            logger.info("Aplicação encerrada");
        } catch (Exception e) {
            logger.error("Erro durante a execução da aplicação", e);
            throw e;
        }
    }
}
