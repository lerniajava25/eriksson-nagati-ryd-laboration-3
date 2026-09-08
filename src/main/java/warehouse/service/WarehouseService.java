package warehouse.service;

import org.springframework.stereotype.Service;
import warehouse.model.Product;
import warehouse.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    private final ProductRepository productRepository;

    public WarehouseService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findByCategory(String category) {

        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Category must not be empty");
        }

        return productRepository.findAll()
                .stream()
                .filter(product -> product.getCategory() != null)
                .filter(product ->
                        product.getCategory()
                                .equalsIgnoreCase(category.trim()))
                .collect(Collectors.toList());
    }

    public List<Product> findLowStockProducts(int threshold) {

        if (threshold < 0) {
            throw new IllegalArgumentException(
                    "Threshold cannot be negative");
        }

        return productRepository.findAll()
                .stream()
                .filter(product ->
                        product.getQuantity() < threshold)
                .collect(Collectors.toList());
    }
}