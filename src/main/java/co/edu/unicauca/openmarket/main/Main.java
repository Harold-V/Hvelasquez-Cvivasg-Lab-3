
package co.edu.unicauca.openmarket.main;

import co.edu.unicauca.openmarket.access.Factory;
import co.edu.unicauca.openmarket.access.ICategoryRepository;
import co.edu.unicauca.openmarket.access.IProductRepository;
import co.edu.unicauca.openmarket.domain.service.CategoryService;
import co.edu.unicauca.openmarket.domain.service.ProductService;
import co.edu.unicauca.openmarket.presentation.GUICategory;
import co.edu.unicauca.openmarket.presentation.GUIProducts;

/**
 *
 * @author Libardo Pantoja
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        IProductRepository repositoryProduct = Factory.getInstance().getProductRepository("default");
        ICategoryRepository repositoryCategory = Factory.getInstance().getCategoryRepository("default");
        
        ProductService productService = new ProductService(repositoryProduct);
        CategoryService categoryService = new CategoryService(repositoryCategory);
        
        GUIProducts instanceProduct = new GUIProducts(productService, categoryService);
        instanceProduct.setVisible(true);
    }
    
}
