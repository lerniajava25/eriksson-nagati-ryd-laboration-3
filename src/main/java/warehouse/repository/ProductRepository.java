package warehouse.repository;

import org.springframework.stereotype.Repository;
import warehouse.model.Product;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {

    private final Map<String, Product> products =
            new ConcurrentHashMap<>();

    public Collection<Product> findAll() {
        return products.values();
    }

    public Product save(Product product) {
        products.put(product.getId(), product);
        return product;
    }
}