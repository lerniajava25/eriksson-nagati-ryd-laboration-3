package warehouse.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import warehouse.model.Product;
import warehouse.repository.ProductRepository;

import java.time.LocalDate;


/**
 * The type Data initializer.
 */
@Configuration
public class DataInitializer {

    /**
     * Init repository command line runner.
     * -
     * Repository is initialized with some dummy data
     * to show in the browser during development
     *
     * @param productRepository the product repository
     * @return the command line runner
     */
    @Bean
    CommandLineRunner initRepository(ProductRepository productRepository) {
        return args -> {
            productRepository.save(new Product(
                    "1",
                    "Skruvmejsel",
                    "Tools",
                    19.99,
                    10,
                    LocalDate.now()));
            productRepository.save(new Product(
                    "2",
                    "Skiftnyckel",
                    "Tools",
                    29.99,
                    5,
                    LocalDate.now()));
            productRepository.save(new Product(
                    "3",
                    "Hammare",
                    "Tools",
                    9.99,
                    7,
                    LocalDate.now()));
            productRepository.save(new Product(
                    "4",
                    "Laptop",
                    "Electronics",
                    14999.00,
                    3,
                    LocalDate.now()));
            productRepository.save(new Product(
                    "5",
                    "Mjölk",
                    "Food",
                    23.00,
                    26,
                    LocalDate.now()));
        };
    }
}
