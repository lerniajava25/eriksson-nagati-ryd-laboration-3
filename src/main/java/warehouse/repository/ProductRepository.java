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
     * Find all products.
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
     * Atomically update an existing product.
     *
     * @param id      the product id
     * @param product the replacement product
     * @return the updated product if the id exists
     */
    public Optional<Product> updateIfPresent(
            String id,
            Product product) {

        Product updatedProduct =
                products.computeIfPresent(
                        id,
                        (key, existingProduct) -> product);

        return Optional.ofNullable(updatedProduct);
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