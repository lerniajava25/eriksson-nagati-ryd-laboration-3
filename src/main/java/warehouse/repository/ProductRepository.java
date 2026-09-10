package warehouse.repository;

import org.springframework.stereotype.Repository;
import warehouse.model.Product;

import java.util.Collection;
import java.util.Map;
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
     * Save.
     *
     * @param product the product
     */
    public void save(Product product) {
        products.put(product.getId(), product);
    }
}