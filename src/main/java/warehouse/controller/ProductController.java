package warehouse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import warehouse.model.Product;
import warehouse.service.WarehouseService;

import java.util.List;

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
}