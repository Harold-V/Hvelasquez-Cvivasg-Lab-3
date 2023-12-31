package co.edu.unicauca.openmarket.domain.service;

import co.edu.unicauca.openmarket.access.IProductRepository;
import co.edu.unicauca.openmarket.domain.Category;
import co.edu.unicauca.openmarket.domain.Product;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Libardo, Julio
 */
public class ProductService {

    // Ahora hay una dependencia de una abstracción, no es algo concreto,
    // no sabe cómo está implementado.
    private IProductRepository repository;

    /**
     * Inyección de dependencias en el constructor. Ya no conviene que el mismo
     * servicio cree un repositorio concreto
     *
     * @param repository una clase hija de IProductRepository
     */
    public ProductService(IProductRepository repository) {
        this.repository = repository;
    }

    public boolean saveProduct(String name, String description, Category category) {

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setCategory(category);

        //Validate product
        if (newProduct.getName().isBlank()) {
            return false;
        }

        return repository.save(newProduct);

    }

    public List<Product> findAllProducts() {
        List<Product> products = new ArrayList<>();
        products = repository.findAll();;

        return products;
    }

    public List<Product> findProductById(Long id) {
        return repository.findById(id);
    }
    
    public Product findOneProductById(Long id){
        return repository.findOneEntityById(id);
    }

    public List<Product> findProductByName(String name) {
        return repository.findByName(name);
    }
    
    public List<Product> findProductByCategory(Long categoryId){
        return repository.findByCategory(categoryId);
    }

    public boolean deleteProduct(Long id) {
        return repository.delete(id);
    }

    public boolean editProduct(Long productId, Product prod, Category category) {

        //Validate product
        if (prod == null || prod.getName().isBlank()) {
            return false;
        }
        prod.setCategory(category);
        return repository.edit(productId, prod);
    }

}
