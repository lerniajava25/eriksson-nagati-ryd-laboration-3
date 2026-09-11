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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
     * Should return all products.
     */
    @Test
    void shouldReturnAllProducts() {

        Product laptop = new Product(
                "1",
                "Laptop",
                "Electronics",
                1000.0,
                2,
                LocalDate.now());

        Product bread = new Product(
                "2",
                "Bread",
                "Food",
                30.0,
                10,
                LocalDate.now());

        when(productRepository.findAll())
                .thenReturn(Arrays.asList(laptop, bread));

        List<Product> result =
                warehouseService.findAll();

        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals("Bread", result.get(1).getName());

        verify(productRepository).findAll();
    }

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

    /**
     * Should create a product.
     */
    @Test
    void shouldCreateProduct() {

        Product product = new Product(
                "10",
                "Keyboard",
                "Electronics",
                800.0,
                5,
                LocalDate.now());

        Product result =
                warehouseService.createProduct(product);

        assertEquals(product, result);

        verify(productRepository).save(product);
    }

    /**
     * Should throw exception when product id is empty.
     */
    @Test
    void shouldThrowExceptionWhenCreatingProductWithEmptyId() {

        Product product = new Product(
                "",
                "Keyboard",
                "Electronics",
                800.0,
                5,
                LocalDate.now());

        assertThrows(
                IllegalArgumentException.class,
                () -> warehouseService.createProduct(product)
        );
    }

    /**
     * Should find product by id.
     */
    @Test
    void shouldFindProductById() {

        Product product = new Product(
                "10",
                "Keyboard",
                "Electronics",
                800.0,
                5,
                LocalDate.now());

        when(productRepository.findById("10"))
                .thenReturn(Optional.of(product));

        Optional<Product> result =
                warehouseService.findById("10");

        assertTrue(result.isPresent());
        assertEquals(
                "Keyboard",
                result.get().getName());

        verify(productRepository).findById("10");
    }

    /**
     * Should return empty optional when product does not exist.
     */
    @Test
    void shouldReturnEmptyWhenProductIdDoesNotExist() {

        when(productRepository.findById("999"))
                .thenReturn(Optional.empty());

        Optional<Product> result =
                warehouseService.findById("999");

        assertTrue(result.isEmpty());

        verify(productRepository).findById("999");
    }

    /**
     * Should update existing product.
     */
    @Test
    void shouldUpdateExistingProduct() {

        Product oldProduct = new Product(
                "10",
                "Keyboard",
                "Electronics",
                800.0,
                5,
                LocalDate.now());

        Product updatedProduct = new Product(
                "different-id",
                "Gaming Keyboard",
                "Electronics",
                1200.0,
                8,
                LocalDate.now());

        when(productRepository.findById("10"))
                .thenReturn(Optional.of(oldProduct));

        Optional<Product> result =
                warehouseService.updateProduct(
                        "10",
                        updatedProduct);

        assertTrue(result.isPresent());

        assertEquals(
                "10",
                result.get().getId());

        assertEquals(
                "Gaming Keyboard",
                result.get().getName());

        verify(productRepository).save(updatedProduct);
    }

    /**
     * Should return empty when updating product that does not exist.
     */
    @Test
    void shouldReturnEmptyWhenUpdatingMissingProduct() {

        Product product = new Product(
                "999",
                "Unknown",
                "Electronics",
                100.0,
                1,
                LocalDate.now());

        when(productRepository.findById("999"))
                .thenReturn(Optional.empty());

        Optional<Product> result =
                warehouseService.updateProduct(
                        "999",
                        product);

        assertTrue(result.isEmpty());

        verify(productRepository).findById("999");
    }

    /**
     * Should delete existing product.
     */
    @Test
    void shouldDeleteExistingProduct() {

        when(productRepository.deleteById("10"))
                .thenReturn(true);

        boolean result =
                warehouseService.deleteProduct("10");

        assertTrue(result);

        verify(productRepository).deleteById("10");
    }

    /**
     * Should return false when deleting missing product.
     */
    @Test
    void shouldReturnFalseWhenDeletingMissingProduct() {

        when(productRepository.deleteById("999"))
                .thenReturn(false);

        boolean result =
                warehouseService.deleteProduct("999");

        assertFalse(result);

        verify(productRepository).deleteById("999");
    }
}