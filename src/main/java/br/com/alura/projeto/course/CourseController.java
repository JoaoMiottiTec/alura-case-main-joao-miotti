package br.com.alura.projeto.course;

import br.com.alura.projeto.category.CategoryRepository;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CourseController {
// I did not observe a service layer in the code in the pattern created because it is a test, but for the sake of representing a complete project, I will add it.
    private final CourseService service;
    private final CategoryRepository categories;

    public CourseController(CourseService service, CategoryRepository categories) {
        this.service = service;
        this.categories = categories;
    }

    @GetMapping("/admin/courses")
    public String list(Model model) {
        model.addAttribute("courses", service.listAll());
        return "course-list";
    }

    @GetMapping("/admin/course/new")
    public String create(Model model) {
        model.addAttribute("form", new  NewCourseForm());
        model.addAttribute("categories", categories.findAll());
        return "course-new";
    }

    @PostMapping("/admin/course/new")
    public String save(@Valid @ModelAttribute("form") NewCourseForm form, Model model) {
        try {
            service.create(form);
            return "redirect:/admin/courses";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("categories", categories.findAll());
            return "course-new";
        }
    }

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<?> updateStatus(@PathVariable String code) {
        service.inactivateByCode(code);
        return ResponseEntity.ok().build();
    }
}
