package co.edu.unicauca.openmarket.access;

import co.edu.unicauca.openmarket.domain.Category;
import co.edu.unicauca.openmarket.domain.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Es una implementación que tiene libertad de hacer una implementación del
 * contrato. Lo puede hacer con Sqlite, postgres, mysql, u otra tecnología
 *
 * @author Libardo, Julio
 */
public class ProductRepository implements IProductRepository {

    private Connection conn;

    public ProductRepository() {
        initDatabase();
    }

    @Override
    public boolean save(Product newProduct) {
        try {
            System.out.println("save Product");
            //Validate product
            if (newProduct == null || newProduct.getName().isBlank()) {
                return false;
            }
            this.connect();

            String sql = "INSERT INTO products ( productName, description, categoryId ) "
                    + "VALUES ( ?, ?, ? );";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newProduct.getName());
            pstmt.setString(2, newProduct.getDescription());
            pstmt.setLong(3, newProduct.getCategory().getCategoryId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return false;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try {
            System.out.println("fin all product");
            String sql = "SELECT p.*, c.categoryName "
                    + "FROM products p "
                    + "INNER JOIN category c ON p.categoryId = c.categoryId;"; // Unir la tabla de productos con la tabla de categorías
            this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Product newProduct = new Product();
                newProduct.setProductId(rs.getLong("productId"));
                newProduct.setName(rs.getString("productName"));
                newProduct.setDescription(rs.getString("description"));

                // Crear una instancia de Category y establecer solo el nombre
                Category category = new Category();
                category.setName(rs.getString("categoryName"));

                // Establecer la categoría completa en el producto
                newProduct.setCategory(category);

                products.add(newProduct);
            }
            rs.close();
            //this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return products;
    }

    private void initDatabase() {
        System.out.println("initDatabase");
        // SQL statement for creating a new table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS products (\n"
                + "	productId integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	productName text NOT NULL,\n"
                + "	description text NULL, \n"
                + "     categoryId integer NOT NULL,\n"
                + "	FOREIGN KEY (categoryId) REFERENCES category(categoryId)\n"
                + ");";

        String dropTableSQL = "DROP TABLE IF EXISTS products;";
        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(dropTableSQL);
            stmt.execute(createTableSQL);
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
    }

    public void connect() {
        // SQLite connection string
        //String url = "jdbc:sqlite:./myDatabase.db"; //Para Linux/Mac
        String url = "jdbc:sqlite:myDatabase.db"; //Para Windows
        //String url = "jdbc:sqlite::memory:";

        try {
            conn = DriverManager.getConnection(url);

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public boolean edit(Long id, Product product) {
        try {
            System.out.println("edit product");
            //Validate product
            if (id <= 0 || product == null) {
                return false;
            }
            this.connect();

            String sql = "UPDATE products "
                    + "SET productName=?, description=?, categoryId=? "
                    + "WHERE productId=?;";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setLong(3, product.getCategory().getCategoryId());
            pstmt.setLong(4, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        try {
            System.out.println("delete product");
            //Validate product
            if (id <= 0) {
                return false;
            }
            this.connect();

            String sql = "DELETE FROM products "
                    + "WHERE productId = ?;";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return false;
    }

    @Override
    public List<Product> findById(Long id) {
        List<Product> products = new ArrayList<>();
        try {
            System.out.println("find by id product");
            String sql = "SELECT p.*, c.categoryName "
                    + "FROM products p "
                    + "INNER JOIN category c ON p.categoryId = c.categoryId "
                    + "WHERE productId = ?;";
            this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();
            while (res.next()) {
                Product newProduct = new Product();
                newProduct.setProductId(res.getLong("productId"));
                newProduct.setName(res.getString("productName"));
                newProduct.setDescription(res.getString("description"));

                // Crear una instancia de Category y establecer solo el nombre
                Category category = new Category();
                category.setName(res.getString("categoryName"));

                // Establecer la categoría completa en el producto
                newProduct.setCategory(category);

                products.add(newProduct);
            }
            //this.disconnect();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return products;
    }

    @Override
    public List<Product> findByName(String name) {
        List<Product> products = new ArrayList<>();
        try {
            System.out.println("find by name product");
            System.out.println("holi");
            String lowercaseName = name.toLowerCase();

            String sql = "SELECT p.*, c.categoryName "
                    + "FROM products p "
                    + "INNER JOIN category c ON p.categoryId = c.categoryId "
                    + "WHERE LOWER(productName) LIKE ?;";
            this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + lowercaseName + "%");

            ResultSet res = pstmt.executeQuery();
            while (res.next()) {
                Product newProduct = new Product();
                newProduct.setProductId(res.getLong("productId"));
                newProduct.setName(res.getString("productName"));
                newProduct.setDescription(res.getString("description"));

                Category category = new Category();
                category.setName(res.getString("categoryName"));

                newProduct.setCategory(category);

                products.add(newProduct);
            }
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return products;
    }

    @Override
    public List<Product> findByCategory(Long category) {
        List<Product> products = new ArrayList<>();
        try {
            System.out.println("find by category product");
            String sql = "SELECT p.*, c.categoryName "
                    + "FROM products p "
                    + "INNER JOIN category c ON p.categoryId = c.categoryId "
                    + "WHERE p.categoryId = ?;";
            this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, category);

            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                Product prod = new Product();
                prod.setProductId(res.getLong("productId"));
                prod.setName(res.getString("productName"));
                prod.setDescription(res.getString("description"));
                Category catego = new Category();
                catego.setName(res.getString("categoryName"));

                // Establecer la categoría completa en el producto
                prod.setCategory(catego);
                products.add(prod);
            }
            //this.disconnect();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return products;
    }

    @Override
    public Product findOneEntityById(Long id) {
        try {
            System.out.println("find One entity by id product");
            String sql = "SELECT p.*, c.categoryName "
                    + "FROM products p "
                    + "INNER JOIN category c ON p.categoryId = c.categoryId "
                    + "WHERE productId = ?;";
            this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();
            if (res.next()) {
                Product newProduct = new Product();
                newProduct.setProductId(res.getLong("productId"));
                newProduct.setName(res.getString("productName"));
                newProduct.setDescription(res.getString("description"));

                // Crear una instancia de Category y establecer solo el nombre
                Category category = new Category();
                category.setCategoryId(res.getLong("categoryId"));
                category.setName(res.getString("categoryName"));

                // Establecer la categoría completa en el producto
                newProduct.setCategory(category);
                res.close();
                return newProduct;
            } else {
                res.close();
                return null;
            }
            //this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return null;
    }

}
