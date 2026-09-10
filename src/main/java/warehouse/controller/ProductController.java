package warehouse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import warehouse.model.Product;
import warehouse.service.WarehouseService;

import java.util.List;
import java.util.Optional;

import static java.lang.Integer.MAX_VALUE;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final WarehouseService warehouseService;

    public ProductController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(
            @PathVariable String category) {

        return warehouseService.findByCategory(category);
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(
            @RequestParam int threshold) {

        return warehouseService.findLowStockProducts(threshold);
    }
    @GetMapping("/popular")
    public List<Product> getPopularProducts(
            @RequestParam(name="mOrL", required = false) String mOrL,
            @RequestParam(name="no_items", required = false) Optional<Integer> numberOfItems
            ) {
        if (mOrL != null && mOrL.equals("l") && numberOfItems.isPresent()) {
            return warehouseService.sortOutNLeastPopularProducts(numberOfItems.get());
        }
        if  (numberOfItems.isEmpty()) {
            return warehouseService.sortOutNMostPopularProducts(MAX_VALUE);
        }
        return warehouseService.sortOutNMostPopularProducts(numberOfItems.get());
    }

    @GetMapping("/price")
    public List<Product> getProductsByPrice(
            @RequestParam(name="order", required = false) String priceOrder,
            @RequestParam(name="no_items", required = false) Optional<Integer> numberOfItems
            ) {
        if (priceOrder != null && priceOrder.equals("asc") && numberOfItems.isPresent()) {
            return warehouseService.sortOutNLeastExpensiveProducts(numberOfItems.get());
        }
        if (numberOfItems.isEmpty()) {
            return warehouseService.sortOutNMostExpensiveProducts(MAX_VALUE);
        }
        return warehouseService.sortOutNMostExpensiveProducts(numberOfItems.get());
    }
}