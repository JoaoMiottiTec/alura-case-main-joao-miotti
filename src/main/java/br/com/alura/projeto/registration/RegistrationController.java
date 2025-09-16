package br.com.alura.projeto.registration;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

  private final RegistrationService service;
  private final RegistrationRepository registrations;

  public RegistrationController(RegistrationService service, RegistrationRepository registrations) {
    this.service = service;
    this.registrations = registrations;
  }

  @PostMapping("/registration/new")
  public ResponseEntity<RegistrationDTO> createCourse(@Valid @RequestBody NewRegistrationDTO body) {
    RegistrationDTO dto = service.register(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping("/registration/report")
  public ResponseEntity<List<RegistrationReportItem>> report() {
    List<RegistrationReportItem> items = registrations.reportTopCourses();
    return ResponseEntity.ok(items);
  }
}
