package co.edu.unicauca.openmarket.access;

import co.edu.unicauca.openmarket.domain.Category;
import java.util.List;

/**
 *
 * @author harve
 */
public interface ICategoryRepository extends IRepository<Category> {
    
    @Override
    boolean save(Category newategory);
    
    @Override
    boolean edit(Long id, Category category);
    
    @Override
    boolean delete(Long id);
    
    @Override
    List<Category> findById(Long id);
    
    @Override
    List<Category> findAll();
    
    @Override
    Category findOneEntityById(Long id);
}
