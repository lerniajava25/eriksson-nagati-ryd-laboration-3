package warehouse.service;

import org.springframework.stereotype.Service;
import warehouse.model.Product;
import warehouse.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Warehouse service.
 */
@Service
public class WarehouseService {

    private final ProductRepository productRepository;

    /**
     * Instantiates a new Warehouse service.
     *
     * @param productRepository the product repository
     */
    public WarehouseService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Find all products.
     *
     * @return the list
     */
    public List<Product> findAll() {
        return new ArrayList<>(productRepository.findAll());
    }

    /**
     * Find product by id.
     *
     * @param id the product id
     * @return optional product
     */
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    /**
     * Create a new product.
     *
     * @param product the product
     * @return the created product
     */
    public Product createProduct(Product product) {
        if (product.getId() == null
                || product.getId().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Product id must not be empty");
        }

        productRepository.save(product);
        return product;
    }

    /**
     * Update an existing product.
     *
     * @param id the product id
     * @param product the updated product
     * @return optional updated product
     */
    public Optional<Product> updateProduct(
            String id,
            Product product) {

        if (productRepository.findById(id).isEmpty()) {
            return Optional.empty();
        }

        product.setId(id);
        productRepository.save(product);

        return Optional.of(product);
    }

    /**
     * Delete product.
     *
     * @param id the product id
     * @return true if deleted
     */
    public boolean deleteProduct(String id) {
        return productRepository.deleteById(id);
    }

    /**
     * Find by category list.
     *
     * @param category the category
     * @return the list
     */
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
                .toList();
    }

    /**
     * Find low-stock products list.
     *
     * @param threshold the threshold
     * @return the list
     */
    public List<Product> findLowStockProducts(int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException(
                    "Threshold cannot be negative");
        }

        return productRepository.findAll()
                .stream()
                .filter(product ->
                        product.getQuantity() < threshold)
                .toList();
    }

    /**
     * Sort out n most expensive products list.
     *
     * @param n the n
     * @return the list
     */
    public List<Product> sortOutNMostExpensiveProducts(int n) {
        return productRepository.findAll()
                .stream()
                .sorted((p1, p2) ->
                        Double.compare(
                                p2.getPrice(),
                                p1.getPrice()))
                .limit(n)
                .toList();
    }

    /**
     * Sort out n least expensive products list.
     *
     * @param n the number of items
     * @return the list
     */
    public List<Product> sortOutNLeastExpensiveProducts(int n) {
        return productRepository.findAll()
                .stream()
                .sorted(Comparator.comparingDouble(
                        Product::getPrice))
                .limit(n)
                .toList();
    }

    /**
     * Sort out n most popular products list.
     *
     * @param n the n
     * @return the list
     */
    public List<Product> sortOutNMostPopularProducts(int n) {
        return productRepository.findAll()
                .stream()
                .sorted((p1, p2) ->
                        Integer.compare(
                                p2.getQuantity(),
                                p1.getQuantity()))
                .limit(n)
                .toList();
    }

    /**
     * Sort out n least popular products list.
     *
     * @param n the n
     * @return the list
     */
    public List<Product> sortOutNLeastPopularProducts(int n) {
        return productRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(
                        Product::getQuantity))
                .limit(n)
                .toList();
    }

    /**
     * Calculate total warehouse value.
     *
     * @return total warehouse value
     */
    public double calculateTotalWarehouseValue() {
        return productRepository.findAll()
                .stream()
                .mapToDouble(product ->
                        product.getQuantity()
                                * product.getPrice())
                .sum();
    }

    /**
     * Get average price per category.
     *
     * @return average price per category
     */
    public Map<String, Double> getAveragePricePerCategory() {
        return productRepository.findAll()
                .stream()
                .filter(product ->
                        product.getCategory() != null
                                && !product.getCategory()
                                .trim()
                                .isEmpty())
                .collect(Collectors.groupingBy(
                        product ->
                                product.getCategory().trim(),
                        Collectors.averagingDouble(
                                Product::getPrice)
                ));
    }
}