package warehouse.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import warehouse.model.Product;
import warehouse.repository.ProductRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Warehouse service test.
 */
@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    /**
     * Should return products from selected category.
     */
    @Test
    void shouldReturnProductsFromSelectedCategory() {

        Product laptop = new Product(
                "1",
                "Laptop",
                "Electronics",
                10000.0,
                4,
                LocalDate.now());

        Product milk = new Product(
                "2",
                "Milk",
                "Food",
                25.0,
                10,
                LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(Arrays.asList(laptop, milk));

        List<Product> result =
                warehouseService.findByCategory("Electronics");

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());

        verify(productRepository).findAll();
    }

    /**
     * Should ignore upper and lower case when searching category.
     */
    @Test
    void shouldIgnoreUpperAndLowerCaseWhenSearchingCategory() {

        Product laptop = new Product(
                "1",
                "Laptop",
                "Electronics",
                10000.0,
                4,
                LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(Collections.singletonList(laptop));

        List<Product> result =
                warehouseService.findByCategory("electronics");

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    /**
     * Should return empty list when category does not exist.
     */
    @Test
    void shouldReturnEmptyListWhenCategoryDoesNotExist() {

        Product laptop = new Product(
                "1",
                "Laptop",
                "Electronics",
                10000.0,
                4,
                LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(Collections.singletonList(laptop));

        List<Product> result =
                warehouseService.findByCategory("Food");

        assertTrue(result.isEmpty());
    }

    /**
     * Should throw exception when category is empty.
     */
    @Test
    void shouldThrowExceptionWhenCategoryIsEmpty() {

        assertThrows(
                IllegalArgumentException.class,
                () -> warehouseService.findByCategory("")
        );
    }

    /**
     * Should return products below stock threshold.
     */
    @Test
    void shouldReturnProductsBelowStockThreshold() {

        Product laptop = new Product(
                "1",
                "Laptop",
                "Electronics",
                10000.0,
                2,
                LocalDate.now());

        Product mouse = new Product(
                "2",
                "Mouse",
                "Electronics",
                500.0,
                20,
                LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(Arrays.asList(laptop, mouse));

        List<Product> result =
                warehouseService.findLowStockProducts(5);

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());

        verify(productRepository).findAll();
    }

    /**
     * Should return empty list when no products have low stock.
     */
    @Test
    void shouldReturnEmptyListWhenNoProductsHaveLowStock() {

        Product mouse = new Product(
                "1",
                "Mouse",
                "Electronics",
                500.0,
                20,
                LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(Collections.singletonList(mouse));

        List<Product> result =
                warehouseService.findLowStockProducts(5);

        assertTrue(result.isEmpty());
    }

    /**
     * Should throw exception when threshold is negative.
     */
    @Test
    void shouldThrowExceptionWhenThresholdIsNegative() {

        assertThrows(
                IllegalArgumentException.class,
                () -> warehouseService.findLowStockProducts(-1)
        );
    }
}