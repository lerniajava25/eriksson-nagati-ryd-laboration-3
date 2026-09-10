package warehouse.service;

import org.springframework.stereotype.Service;
import warehouse.model.Product;
import warehouse.repository.ProductRepository;

import java.util.Comparator;
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

    public List<Product> sortOutNMostExpensiveProducts(int n) {
        return productRepository.findAll()
                .stream()
                .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Product> sortOutNLeastExpensiveProducts(int n) {
        return productRepository.findAll()
                .stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Product> sortOutNMostPopularProducts(int n) {
        return productRepository.findAll()
                .stream()
                .sorted((p1, p2) -> Integer.compare(p2.getQuantity(), p1.getQuantity()))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Product> sortOutNLeastPopularProducts(int n) {
        return productRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Product::getQuantity))
                .limit(n)
                .collect(Collectors.toList());
    }
}