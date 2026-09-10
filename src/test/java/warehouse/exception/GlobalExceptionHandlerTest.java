package warehouse.exception;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    @Test
    void shouldReturnBadRequestForIllegalArgumentException() {

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler();

        IllegalArgumentException exception =
                new IllegalArgumentException(
                        "Threshold cannot be negative");

        ResponseEntity<Map<String, String>> response =
                handler.handleIllegalArgumentException(exception);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        assertEquals(
                "Threshold cannot be negative",
                response.getBody().get("error"));
    }
}