package hellenicRides.backend.controller;

import hellenicRides.backend.entity.MotoLocation;
import hellenicRides.backend.service.MotoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moto-locations")
@RequiredArgsConstructor
public class MotoLocationController {

  private final MotoService motoService;

  @GetMapping
  public ResponseEntity<List<MotoLocation>> getAllMotoLocations() {
    return ResponseEntity.ok(motoService.getAllMotoLocations());
  }
}
