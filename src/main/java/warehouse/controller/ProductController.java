package warehouse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import warehouse.model.Product;
import warehouse.service.WarehouseService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Integer.MAX_VALUE;

/**
 * The type Product controller.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final WarehouseService warehouseService;

    /**
     * Instantiates a new Product controller.
     *
     * @param warehouseService the warehouse service
     */
    public ProductController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * Gets all products.
     *
     * @return all products
     */
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return warehouseService.findAll();
    }

    /**
     * Gets one product by id.
     *
     * @param id the product id
     * @return the product or 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable String id) {

        return warehouseService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    /**
     * Creates a new product.
     *
     * @param product the product
     * @return created product
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product) {

        Product createdProduct =
                warehouseService.createProduct(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    /**
     * Updates an existing product.
     *
     * @param id      the product id
     * @param product the updated product
     * @return updated product or 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable String id,
            @RequestBody Product product) {

        return warehouseService
                .updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    /**
     * Deletes a product.
     *
     * @param id the product id
     * @return 204 if deleted or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable String id) {

        if (warehouseService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Gets products by category.
     *
     * @param category the category
     * @return products by category
     */
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(
            @PathVariable String category) {

        return warehouseService.findByCategory(category);
    }

    /**
     * Gets low-stock products.
     *
     * @param threshold the threshold
     * @return low-stock products
     */
    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(
            @RequestParam int threshold) {

        return warehouseService
                .findLowStockProducts(threshold);
    }

    /**
     * Gets popular products.
     *
     * @param prodOrder     the product order
     * @param numberOfItems the number of items
     * @return products
     */
    @GetMapping("/popular")
    public List<Product> getPopularProducts(
            @RequestParam(
                    name = "order",
                    required = false)
            Optional<String> prodOrder,

            @RequestParam(
                    name = "no_items",
                    required = false)
            Optional<Integer> numberOfItems) {

        if (prodOrder.isPresent()
                && prodOrder.get().equals("l")
                && numberOfItems.isPresent()) {

            return warehouseService
                    .sortOutNLeastPopularProducts(
                            numberOfItems.get());
        }

        if (numberOfItems.isEmpty()) {
            return warehouseService
                    .sortOutNMostPopularProducts(
                            MAX_VALUE);
        }

        return warehouseService
                .sortOutNMostPopularProducts(
                        numberOfItems.get());
    }

    /**
     * Gets products by price.
     *
     * @param priceOrder    the price order
     * @param numberOfItems the number of items
     * @return products
     */
    @GetMapping("/price")
    public List<Product> getProductsByPrice(
            @RequestParam(
                    name = "order",
                    required = false)
            Optional<String> priceOrder,

            @RequestParam(
                    name = "no_items",
                    required = false)
            Optional<Integer> numberOfItems) {

        if (priceOrder.isPresent()
                && priceOrder.get().equals("asc")
                && numberOfItems.isPresent()) {

            return warehouseService
                    .sortOutNLeastExpensiveProducts(
                            numberOfItems.get());
        }

        if (numberOfItems.isEmpty()) {
            return warehouseService
                    .sortOutNMostExpensiveProducts(
                            MAX_VALUE);
        }

        return warehouseService
                .sortOutNMostExpensiveProducts(
                        numberOfItems.get());
    }

    /**
     * Gets total warehouse value.
     *
     * @return total warehouse value
     */
    @GetMapping("/analytics/total-value")
    public double getTotalWarehouseValue() {
        return warehouseService
                .calculateTotalWarehouseValue();
    }

    /**
     * Gets average price per category.
     *
     * @return average price per category
     */
    @GetMapping("/analytics/average-price")
    public Map<String, Double>
            getAveragePricePerCategory() {

        return warehouseService
                .getAveragePricePerCategory();
    }
}