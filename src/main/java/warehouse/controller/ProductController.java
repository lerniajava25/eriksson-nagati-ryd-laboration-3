package warehouse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import warehouse.model.Product;
import warehouse.service.WarehouseService;
import java.util.Map;
import java.util.List;
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
     * @return all the products
     */
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return warehouseService.findAll();
    }

    /**
     * Gets products by category.
     *
     * @param category the category
     * @return the products by category
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
     * @return the low-stock products
     */
    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(
            @RequestParam int threshold) {

        return warehouseService.findLowStockProducts(threshold);
    }

    /**
     * Gets popular products.
     *
     * @param prodOrder     the prod order
     * @param numberOfItems the number of items
     * @return the popular products
     */
    @GetMapping("/popular")
    public List<Product> getPopularProducts(
            @RequestParam(name= "order", required = false) Optional<String> prodOrder,
            @RequestParam(name="no_items", required = false) Optional<Integer> numberOfItems
            ) {
        if (prodOrder.isPresent() && prodOrder.get().equals("l") && numberOfItems.isPresent()) {
            return warehouseService.sortOutNLeastPopularProducts(numberOfItems.get());
        }
        if  (numberOfItems.isEmpty()) {
            return warehouseService.sortOutNMostPopularProducts(MAX_VALUE);
        }
        return warehouseService.sortOutNMostPopularProducts(numberOfItems.get());
    }

    /**
     * Gets products by price.
     *
     * @param priceOrder    the price order
     * @param numberOfItems the number of items
     * @return the products by price
     */
    @GetMapping("/price")
    public List<Product> getProductsByPrice(
            @RequestParam(name="order", required = false) Optional<String> priceOrder,
            @RequestParam(name="no_items", required = false) Optional<Integer> numberOfItems
            ) {
        if (priceOrder.isPresent() && priceOrder.get().equals("asc") && numberOfItems.isPresent()) {
            return warehouseService.sortOutNLeastExpensiveProducts(numberOfItems.get());
        }
        if (numberOfItems.isEmpty()) {
            return warehouseService.sortOutNMostExpensiveProducts(MAX_VALUE);
        }
        return warehouseService.sortOutNMostExpensiveProducts(numberOfItems.get());
    }

    /**
     * Gets total warehouse value.
     *
     * @return the total warehouse value
     */
    @GetMapping("/analytics/total-value")
    public double getTotalWarehouseValue() {
        return warehouseService.calculateTotalWarehouseValue();
    }

    /**
     * Gets average price per category.
     *
     * @return the average price per category
     */
    @GetMapping("/analytics/average-price")
    public Map<String, Double> getAveragePricePerCategory() {
        return warehouseService.getAveragePricePerCategory();
    }
}