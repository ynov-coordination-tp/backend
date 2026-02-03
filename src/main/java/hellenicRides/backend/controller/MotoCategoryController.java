package hellenicRides.backend.controller;

import hellenicRides.backend.entity.MotoCategory;
import hellenicRides.backend.service.MotoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moto-categories")
@RequiredArgsConstructor
public class MotoCategoryController {

  private final MotoService motoService;

  @GetMapping
  public ResponseEntity<List<MotoCategory>> getAllMotoCategories() {
    return ResponseEntity.ok(motoService.getAllMotoCategories());
  }

  // TODO Additional endpoint to match "GET /moto-categories" request
}
