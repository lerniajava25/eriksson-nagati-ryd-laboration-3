package warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class Application {

    @GetMapping("/")
    String home() {
        return "Hello World!";
    }

    @PostMapping("/")

    @PutMapping("/")

    @DeleteMapping("/")

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
