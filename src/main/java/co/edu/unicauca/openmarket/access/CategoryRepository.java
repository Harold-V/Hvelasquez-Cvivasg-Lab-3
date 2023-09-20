package co.edu.unicauca.openmarket.access;

import co.edu.unicauca.openmarket.domain.Category;
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
//import org.apache.commons.text.similarity.LevenshteinDistance;

/**
 *
 * @author harve
 */
public class CategoryRepository implements ICategoryRepository {

    private Connection conn;

    public CategoryRepository() {
        initDatabase();
    }

    @Override
    public boolean save(Category newCategory) {
        try {
            System.out.println("save category");
            //Validate product
            if (newCategory == null || newCategory.getName().isBlank()) {
                return false;
            }
            this.connect();

            String sql = "INSERT INTO category (categoryName) "
                    + "VALUES (?);";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newCategory.getName());
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CategoryRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return false;
    }

    @Override
    public boolean edit(Long id, Category category) {
        try {
            System.out.println("edit category");
            //Validate product
            if (id <= 0 || category == null) {
                return false;
            }
            this.connect();
            String sql = "UPDATE category "
                    + "   SET categoryName=? "
                    + "   WHERE categoryId=?;";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category.getName());
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CategoryRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        try {
            System.out.println("delete category");
            //Validate product
            if (id <= 0) {
                return false;
            }
            this.connect();

            String sql = "DELETE FROM category "
                    + "WHERE categoryId = ?;";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CategoryRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return false;
    }

    @Override
    public List<Category> findById(Long id) {
        List<Category> categories = new ArrayList<>();
        try {
            System.out.println("find by id category");
            String sql = "SELECT * FROM category "
                    + "WHERE categoryId = ?;";
            this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                Category newCategory = new Category();
                newCategory.setCategoryId(res.getLong("categoryId"));
                newCategory.setName(res.getString("categoryName"));
                categories.add(newCategory);
            }
            //this.disconnect();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return categories;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        try {
            System.out.println("find all category");
            String sql = "SELECT * FROM category;";
            this.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Category newCategory = new Category();
                newCategory.setCategoryId(rs.getLong("categoryId"));
                newCategory.setName(rs.getString("categoryName"));
                categories.add(newCategory);
            }
            //this.disconnect();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return categories;
    }

    private void initDatabase() {
        // SQL statement for creating a new table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS category (\n"
                + "	categoryId integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	categoryName text NOT NULL\n"
                + ");";
        String dropTableSQL = "DROP TABLE IF EXISTS category;";
        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(dropTableSQL);
            stmt.execute(createTableSQL);
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
    }

    public void connect() {
        // SQLite connection string
        //String url = "jdbc:sqlite:./myDatabase.db"; //Para Linux/Mac
        //String url = "jdbc:sqlite:C:/sqlite/db/myDatabase.db"; //Para Windows
        //String url = "jdbc:sqlite::memory:";
        String url = "jdbc:sqlite:myDatabase.db"; //Para Windows

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
    public List<Category> findByName(String name) {
//        List<Category> categories = new ArrayList<>();
//        try {
//
//            String sql = "SELECT * FROM category "
//                    + "WHERE categoryName = ?;";
//
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, name);
//
//            ResultSet res = pstmt.executeQuery();
//
//            while (res.next()) {
//                Category newCategory = new Category();
//                newCategory.setCategoryId(res.getLong("categoryId"));
//                newCategory.setName(res.getString("categoryName"));
//                categories.add(newCategory);
//            }
//            //this.disconnect();
//
//        } catch (SQLException ex) {
//            Logger.getLogger(CategoryRepository.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return categories;

        List<Category> categories = new ArrayList<>();
        try {
            System.out.println("find by name category");
            String lowercaseName = name.toLowerCase();

            String sql = "SELECT * FROM category;";
            this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                Category existingCategory = new Category();
                existingCategory.setCategoryId(res.getLong("categoryId"));
                existingCategory.setName(res.getString("categoryName"));

                String lowercaseExistingName = existingCategory.getName().toLowerCase();

                if (lowercaseExistingName.contains(lowercaseName) || lowercaseName.contains(lowercaseExistingName)) {
                    categories.add(existingCategory);
                }
            }
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return categories;
    }

    @Override
    public Category findOneEntityById(Long id) {
        try {
            System.out.println("find by entity by id category");
            String sql = "SELECT * FROM category "
                    + "WHERE categoryId = ?;";
            this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                Category newCategory = new Category();
                newCategory.setCategoryId(res.getLong("categoryId"));
                newCategory.setName(res.getString("categoryName"));
                res.close();
                return newCategory;
            } else {
                res.close();
                return null;
            }
            //this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
        return null;
    }

}
