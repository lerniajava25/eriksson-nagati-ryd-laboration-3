package warehouse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import warehouse.model.Product;
import warehouse.repository.ProductRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceSortingTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    private Product p1;
    private Product p2;
    private Product p3;

    @BeforeEach
    void setUp() {
        // Vi skapar några testprodukter med varierat pris och antal (kvantitet)
        // Antar konstruktör eller setters utifrån din service-kod
        p1 = new Product("1","Skruvmejsel", "Verktyg", 10.0, 100, LocalDate.now());  // Billigast, flest i lager
        p2 = new Product("2","Skiftnyckel", "Verktyg", 50.0, 50, LocalDate.now());   // Mellan
        p3 = new Product("3","Hammare", "Verktyg", 200.0, 5, LocalDate.now());  // Dyrast, lägst i lager
    }

    @Nested
    @DisplayName("Tester för sortering på pris")
    class PriceSortingTests {

        @Test
        @DisplayName("sortOutNMostExpensiveProducts ska returnera de dyraste produkterna i fallande ordning")
        void ReturnProductsSortedByPriceDescending() {
            when(productRepository.findAll()).thenReturn(List.of(p1, p2, p3));

            List<Product> result = warehouseService.sortOutNMostExpensiveProducts(2);

            assertThat(result)
                    .hasSize(2)
                    .containsExactly(p3, p2); // Hammare (200.0) sedan Mutter (50.0)
        }

        @Test
        @DisplayName("sortOutNLeastExpensiveProducts ska returnera de billigaste produkterna i stigande ordning")
        void ReturnProductsSortedByPriceAscending() {
            when(productRepository.findAll()).thenReturn(List.of(p1, p2, p3));

            List<Product> result = warehouseService.sortOutNLeastExpensiveProducts(2);

            assertThat(result)
                    .hasSize(2)
                    .containsExactly(p1, p2); // Skruv (10.0) sedan Mutter (50.0)
        }
    }

    @Nested
    @DisplayName("Tester för sortering på popularitet/lagerantal")
    class PopularitySortingTests {

        @Test
        @DisplayName("sortOutNMostPopularProducts ska returnera produkterna med högst antal i fallande ordning")
        void ReturnProductsSortedByQuantityDescending() {
            when(productRepository.findAll()).thenReturn(List.of(p1, p2, p3));

            List<Product> result = warehouseService.sortOutNMostPopularProducts(2);

            assertThat(result)
                    .hasSize(2)

                    // p1 har 100 st, p2 har 50 st
                    .containsExactly(p1, p2);
        }

        @Test
        @DisplayName("sortOutNLeastPopularProducts ska returnera produkterna med lägst antal i stigande ordning")
        void ReturnProductsSortedByQuantityAscending() {
            when(productRepository.findAll()).thenReturn(List.of(p1, p2, p3));

            List<Product> result = warehouseService.sortOutNLeastPopularProducts(2);

            assertThat(result)
                    .hasSize(2)

                    // p3 har 5 st, p2 har 50 st
                    .containsExactly(p3, p2);
        }
    }

    @Nested
    @DisplayName("Gränsfall och gränsvärdeshantering för sortering")
    class EdgeCasesTests {

        @Test
        @DisplayName("Om n är större än antalet produkter ska alla produkter returneras")
        void WhenNIsGreaterThanListSize_ShouldReturnAllProducts() {
            when(productRepository.findAll()).thenReturn(List.of(p1, p2));

            List<Product> result = warehouseService.sortOutNMostExpensiveProducts(10);

            assertThat(result)
                    .hasSize(2)
                    .containsExactly(p2, p1);
        }

        @Test
        @DisplayName("Om n är 0 ska en tom lista returneras")
        void WhenNIsZero_ShouldReturnEmptyList() {
            when(productRepository.findAll()).thenReturn(List.of(p1, p2, p3));

            List<Product> result = warehouseService.sortOutNMostExpensiveProducts(0);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Om produktlistan är tom ska en tom lista returneras")
        void WhenRepositoryIsEmpty_ShouldReturnEmptyList() {
            when(productRepository.findAll()).thenReturn(Collections.emptyList());

            List<Product> result = warehouseService.sortOutNLeastPopularProducts(5);

            assertThat(result).isEmpty();
        }
    }
}