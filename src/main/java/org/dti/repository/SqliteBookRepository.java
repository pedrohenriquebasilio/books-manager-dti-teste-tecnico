package org.dti.repository;

import org.dti.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteBookRepository implements BookRepository {
    private static final Logger logger = LoggerFactory.getLogger(SqliteBookRepository.class);
    private final Connection conn;

    public SqliteBookRepository(Connection conn) {
        this.conn = conn;
        logger.info("SqliteBookRepository inicializado");
    }

    @Override
    public List<Book> findAll() {
        logger.info("Executando findAll()");
        List<Book> list = new ArrayList<>();
        String sql = "SELECT id, title, author, pages, price, published_date, description FROM books ORDER BY id";
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            logger.debug("SQL executado: {}", sql);
            while (rs.next()) {
                list.add(map(rs));
            }
            logger.info("findAll() retornou {} registros", list.size());
        } catch (SQLException e) {
            logger.error("Erro ao executar findAll()", e);
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Book> findById(int id) {
        logger.info("Executando findById({})", id);
        String sql = "SELECT id, title, author, pages, price, published_date, description FROM books WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            logger.debug("SQL executado: {} com parâmetro id={}", sql, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Book book = map(rs);
                    logger.info("findById({}) encontrou: {}", id, book);
                    return Optional.of(book);
                }
                logger.info("findById({}) não encontrou resultados", id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Erro ao executar findById({})", id, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int insert(Book book) {
        logger.info("Executando insert({})", book);
        String sql = "INSERT INTO books(title, author, pages, price, published_date, description) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(st, book);
            logger.debug("SQL executado: {}", sql);
            st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    logger.info("insert() gerou ID: {}", id);
                    return id;
                }
                logger.warn("insert() não retornou ID gerado");
                return -1;
            }
        } catch (SQLException e) {
            logger.error("Erro ao executar insert({})", book, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Book book) {
        logger.info("Executando update({})", book);
        String sql = "UPDATE books SET title=?, author=?, pages=?, price=?, published_date=?, description=? WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            bind(st, book);
            st.setInt(7, book.getId());
            logger.debug("SQL executado: {} com ID={}", sql, book.getId());
            int rowsAffected = st.executeUpdate();
            boolean success = rowsAffected > 0;
            logger.info("update() afetou {} linhas. Sucesso: {}", rowsAffected, success);
            return success;
        } catch (SQLException e) {
            logger.error("Erro ao executar update({})", book, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        logger.info("Executando delete({})", id);
        String sql = "DELETE FROM books WHERE id=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            logger.debug("SQL executado: {} com ID={}", sql, id);
            int rowsAffected = st.executeUpdate();
            boolean success = rowsAffected > 0;
            logger.info("delete() afetou {} linhas. Sucesso: {}", rowsAffected, success);
            return success;
        } catch (SQLException e) {
            logger.error("Erro ao executar delete({})", id, e);
            throw new RuntimeException(e);
        }
    }

    private Book map(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        Integer pages = rs.getInt("pages");
        Double price = rs.getObject("price") == null ? null : rs.getDouble("price");
        String pd = rs.getString("published_date");
        LocalDate publishedDate = pd == null ? null : LocalDate.parse(pd);
        String desc = rs.getString("description");
        logger.debug("Mapeando ResultSet para Book: id={}, title={}, author={}", id, title, author);
        return new Book(id, title, author, pages, price, publishedDate, desc);
    }

    private void bind(PreparedStatement st, Book book) throws SQLException {
        logger.debug("Vinculando Book aos parâmetros do PreparedStatement: {}", book);
        st.setString(1, book.getTitle());
        st.setString(2, book.getAuthor());
        st.setInt(3, book.getPages());
        if (book.getPrice() == null) st.setNull(4, Types.REAL);
        else st.setDouble(4, book.getPrice());
        if (book.getPublishedDate() == null) st.setNull(5, Types.VARCHAR);
        else st.setString(5, book.getPublishedDate().toString());
        if (book.getDescription() == null) st.setNull(6, Types.VARCHAR);
        else st.setString(6, book.getDescription());
    }
}
