package co.edu.unicauca.openmarket.access;

import java.util.List;

/**
 *
 * @author Carlos Mario
 */
public interface IRepository<T> {
    boolean save(T newEntity);

    boolean edit(Long id, T newEntity);

    boolean delete(Long id);

    List<T> findById(Long id);
    
    T findOneEntityById(Long id);
    
    List<T> findByName(String name);

    List<T> findAll();
}
