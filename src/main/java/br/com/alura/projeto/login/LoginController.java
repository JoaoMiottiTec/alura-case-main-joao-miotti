package br.com.alura.projeto.login;

import br.com.alura.projeto.course.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

  private final CourseService service;

  public LoginController(CourseService service) {
    this.service = service;
  }

  @GetMapping("/")
  public String home(
      @RequestParam(value = "category", required = false) String category, Model model) {
    model.addAttribute("categories", service.listActiveCategoryNames());
    model.addAttribute("selectedCategory", category);
    model.addAttribute("courses", service.listActive(category));
    return "login";
  }
}
