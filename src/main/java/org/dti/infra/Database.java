package org.dti.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private final String url;

    public Database(String pathToDbFile) {
        this.url = "jdbc:sqlite:" + pathToDbFile;
        logger.info("Database inicializado com URL: {}", url);
    }

    public Connection getConnection() throws SQLException {
        logger.info("Obtendo conexão com o banco de dados");
        try {
            Connection connection = DriverManager.getConnection(url);
            logger.info("Conexão com o banco de dados estabelecida com sucesso");
            return connection;
        } catch (SQLException e) {
            logger.error("Erro ao obter conexão com o banco de dados", e);
            throw e;
        }
    }
}
