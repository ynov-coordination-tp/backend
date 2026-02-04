package hellenicRides.backend.controller;

import hellenicRides.backend.entity.Admin;
import hellenicRides.backend.entity.AdminPost;
import hellenicRides.backend.service.AdminCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@Tag(name = "Admins", description = "CRUD operations for admins.")
public class AdminController {

  private final AdminCrudService adminCrudService;

  @GetMapping
  @Operation(summary = "List admins", description = "Public endpoint to list all admins.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of admins.")})
  public ResponseEntity<List<Admin>> getAllAdmins() {
    return ResponseEntity.ok(adminCrudService.getAllAdmins());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get admin by id", description = "Public endpoint to fetch an admin by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Admin found."),
    @ApiResponse(responseCode = "404", description = "Admin not found.")
  })
  public ResponseEntity<Admin> getAdminById(
      @Parameter(description = "Admin id") @PathVariable Long id) {
    return adminCrudService
        .getAdminById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Create admin", description = "Create a new admin.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Admin created."),
    @ApiResponse(responseCode = "400", description = "Invalid request.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Admin> createAdmin(@RequestBody AdminPost adminPost) {
    Admin created = adminCrudService.createAdmin(adminPost);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update admin", description = "Update an existing admin by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Admin updated."),
    @ApiResponse(responseCode = "404", description = "Admin not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Admin> updateAdmin(
      @Parameter(description = "Admin id") @PathVariable Long id,
      @RequestBody AdminPost adminPost) {
    return adminCrudService
        .updateAdmin(id, adminPost)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete admin", description = "Delete an admin by id.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Admin deleted."),
    @ApiResponse(responseCode = "404", description = "Admin not found.")
  })
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteAdmin(
      @Parameter(description = "Admin id") @PathVariable Long id) {
    if (!adminCrudService.deleteAdmin(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
