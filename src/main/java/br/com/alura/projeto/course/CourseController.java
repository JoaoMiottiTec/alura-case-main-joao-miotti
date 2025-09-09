package br.com.alura.projeto.course;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CourseController {

    @GetMapping("/admin/courses")
    public String list(@Valid NewCourseForm form) {
        // TODO: Implementar a Questão 1 - Listagem de Cursos aqui...

        return "";
    }

    @GetMapping("/admin/course/new")
    public String create(NewCourseForm form) {
        // TODO: Implementar a Questão 1 - Cadastro de Cursos aqui...

        return "";
    }

    @PostMapping("/admin/course/new")
    public String save(@Valid NewCourseForm form) {
        // TODO: Implementar a Questão 1 - Cadastro de Cursos aqui...

        return "";
    }

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<?> updateStatus(@PathVariable("code") String courseCode) {
        // TODO: Implementar a Questão 2 - Inativação de Curso aqui...

        return ResponseEntity.ok().build();
    }

}
