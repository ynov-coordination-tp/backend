package hellenicRides.backend.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(FormulaNotAllowedException.class)
  public ResponseEntity<Map<String, Object>> handleFormulaNotAllowed(
      FormulaNotAllowedException ex) {

    log.warn("Formula validation failed: {}", ex.getMessage());

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Formule non autorisée");
    body.put("message", ex.getMessage());
    body.put("formulaName", ex.getFormulaName());
    body.put("tourName", ex.getTourName());
    body.put("allowedFormulas", ex.getAllowedFormulas());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {

    log.warn("Validation error: {}", ex.getMessage());

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Erreur de validation");
    body.put("message", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }
}
