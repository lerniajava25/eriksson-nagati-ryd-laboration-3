package warehouse.repository;

import org.springframework.stereotype.Repository;
import warehouse.model.Product;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Product repository.
 */
@Repository
public class ProductRepository {

    private final Map<String, Product> products =
            new ConcurrentHashMap<>();

    /**
     * Find all collection.
     *
     * @return the collection
     */
    public Collection<Product> findAll() {
        return products.values();
    }

    /**
     * Find product by id.
     *
     * @param id the product id
     * @return optional product
     */
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    /**
     * Save product.
     *
     * @param product the product
     */
    public void save(Product product) {
        products.put(product.getId(), product);
    }

    /**
     * Delete product by id.
     *
     * @param id the product id
     * @return true if a product was deleted
     */
    public boolean deleteById(String id) {
        return products.remove(id) != null;
    }
}