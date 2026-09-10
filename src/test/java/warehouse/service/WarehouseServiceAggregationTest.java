package warehouse.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import warehouse.model.Product;
import warehouse.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceAggregationTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    void shouldCalculateTotalWarehouseValue() {

        Product laptop = new Product(
                "1", "Laptop", "Electronics",
                1000.0, 2, LocalDate.now());

        Product mouse = new Product(
                "2", "Mouse", "Electronics",
                100.0, 3, LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(List.of(laptop, mouse));

        double result =
                warehouseService.calculateTotalWarehouseValue();

        assertEquals(2300.0, result, 0.001);
    }

    @Test
    void shouldReturnZeroWarehouseValueWhenWarehouseIsEmpty() {

        when(productRepository.findAll())
                .thenReturn(List.of());

        double result =
                warehouseService.calculateTotalWarehouseValue();

        assertEquals(0.0, result, 0.001);
    }

    @Test
    void shouldCalculateAveragePricePerCategory() {

        Product laptop = new Product(
                "1", "Laptop", "Electronics",
                1000.0, 2, LocalDate.now());

        Product mouse = new Product(
                "2", "Mouse", "Electronics",
                500.0, 5, LocalDate.now());

        Product bread = new Product(
                "3", "Bread", "Food",
                20.0, 10, LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(List.of(laptop, mouse, bread));

        Map<String, Double> result =
                warehouseService.getAveragePricePerCategory();

        assertEquals(750.0,
                result.get("Electronics"), 0.001);

        assertEquals(20.0,
                result.get("Food"), 0.001);
    }

    @Test
    void shouldIgnoreProductsWithNullOrBlankCategory() {

        Product laptop = new Product(
                "1", "Laptop", "Electronics",
                1000.0, 2, LocalDate.now());

        Product unknown1 = new Product(
                "2", "Unknown", null,
                500.0, 1, LocalDate.now());

        Product unknown2 = new Product(
                "3", "Unknown", "   ",
                300.0, 1, LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(List.of(
                        laptop, unknown1, unknown2));

        Map<String, Double> result =
                warehouseService.getAveragePricePerCategory();

        assertEquals(1, result.size());
        assertTrue(result.containsKey("Electronics"));
        assertFalse(result.containsKey(""));
    }

    @Test
    void shouldReturnEmptyMapWhenWarehouseIsEmpty() {

        when(productRepository.findAll())
                .thenReturn(List.of());

        Map<String, Double> result =
                warehouseService.getAveragePricePerCategory();

        assertTrue(result.isEmpty());
    }
}