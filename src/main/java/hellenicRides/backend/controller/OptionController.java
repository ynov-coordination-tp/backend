package hellenicRides.backend.controller;

import hellenicRides.backend.entity.Option;
import hellenicRides.backend.service.OptionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionController {

  private final OptionService optionService;

  @GetMapping
  public ResponseEntity<List<Option>> getAllOptions() {
    return ResponseEntity.ok(optionService.getAllOptions());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Option> getOptionById(@PathVariable Long id) {
    return optionService
        .getOptionById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
