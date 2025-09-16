package br.com.alura.projeto.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

  @Autowired MockMvc mockMvc;

  @MockBean CourseService service;

  @MockBean CategoryRepository categories;

  @TestConfiguration
  static class ViewConfig {
    @Bean
    InternalResourceViewResolver internalResourceViewResolver() {
      InternalResourceViewResolver r = new InternalResourceViewResolver();
      r.setPrefix("/WEB-INF/views/");
      r.setSuffix(".jsp");
      return r;
    }
  }

  @Test
  void list__should_render_course_list_page() throws Exception {
    List<CourseDTO> dtos =
        List.of(
            new CourseDTO(
                1L, "Java Web", "java-web", "ana@alura.com", "Back-End", "desc", "ACTIVE"),
            new CourseDTO(
                2L, "Spring Boot", "spring-boot", "joao@alura.com", "Back-End", "desc", "ACTIVE"));
    when(service.listAll()).thenReturn(dtos);

    mockMvc
        .perform(get("/admin/courses"))
        .andExpect(status().isOk())
        .andExpect(view().name("course-list"))
        .andExpect(model().attributeExists("courses"));

    verify(service).listAll();
  }

  @Test
  void create__should_render_new_course_form() throws Exception {
    when(categories.findAll())
        .thenReturn(
            List.of(
                new Category("Back-End", "backend", "#0AF", 1),
                new Category("Front-End", "frontend", "#0BF", 2)));

    mockMvc
        .perform(get("/admin/course/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("course-new"))
        .andExpect(model().attributeExists("form"))
        .andExpect(model().attributeExists("categories"));

    verify(categories).findAll();
  }

  @Test
  void save__should_redirect_on_success() throws Exception {
    when(service.create(any(NewCourseForm.class)))
        .thenReturn(
            new CourseDTO(
                1L, "Java Web", "java-web", "ana@alura.com", "Back-End", "desc", "ACTIVE"));

    mockMvc
        .perform(
            post("/admin/course/new")
                .param("name", "Java Web")
                .param("code", "java-web")
                .param("instructorEmail", "ana@alura.com")
                .param("categoryId", "1")
                .param("description", "desc"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/courses"));

    verify(service).create(any(NewCourseForm.class));
  }

  @Test
  void save__should_return_to_form_on_validation_error_from_service() throws Exception {
    when(categories.findAll()).thenReturn(List.of(new Category("Back-End", "backend", "#0AF", 1)));
    when(service.create(any(NewCourseForm.class)))
        .thenThrow(new IllegalArgumentException("Invalid code"));

    mockMvc
        .perform(
            post("/admin/course/new")
                .param("name", "Java Web")
                .param("code", "INVALID")
                .param("instructorEmail", "ana@alura.com")
                .param("categoryId", "1")
                .param("description", "desc"))
        .andExpect(status().isOk())
        .andExpect(view().name("course-new"))
        .andExpect(model().attributeExists("error"))
        .andExpect(model().attributeExists("categories"));

    verify(service).create(any(NewCourseForm.class));
    verify(categories, atLeastOnce()).findAll();
  }

  @Test
  void inactivate__should_return_ok() throws Exception {
    mockMvc.perform(post("/course/{code}/inactive", "java-web")).andExpect(status().isOk());

    verify(service).inactivateByCode("java-web");
  }

  @Test
  void editForm__renders_edit_page_with_form_and_combos() throws Exception {
    Category cat = new Category("Back-End", "backend", "#0AF", 1);
    User instructor = new User("Ana", "ana@alura.com", Role.INSTRUCTOR, "x");
    Course course = new Course("Java Web", "java-web", instructor, cat, "desc");

    when(service.findByCodeOrThrow("java-web")).thenReturn(course);
    when(categories.findAll()).thenReturn(List.of(cat));

    mockMvc
        .perform(get("/admin/course/{code}/edit", "java-web"))
        .andExpect(status().isOk())
        .andExpect(view().name("course-edit"))
        .andExpect(model().attributeExists("code"))
        .andExpect(model().attributeExists("form"))
        .andExpect(model().attributeExists("categories"))
        .andExpect(model().attributeExists("statuses"));

    verify(service).findByCodeOrThrow("java-web");
    verify(categories).findAll();
  }

  @Test
  void editForm__redirects_to_list_when_course_not_found() throws Exception {
    when(service.findByCodeOrThrow("nope"))
        .thenThrow(new IllegalArgumentException("Course not found"));

    mockMvc
        .perform(get("/admin/course/{code}/edit", "nope"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/courses"));
  }

  @Test
  void edit__redirects_on_success() throws Exception {
    when(service.update(anyString(), any(UpdateCourseForm.class)))
        .thenReturn(
            new CourseDTO(
                1L, "Java Web", "java-web", "ana@alura.com", 1L, "Back-End", "desc", "ACTIVE"));

    mockMvc
        .perform(
            post("/admin/course/{code}/edit", "java-web")
                .param("name", "Java Web")
                .param("instructorEmail", "ana@alura.com")
                .param("categoryId", "1")
                .param("description", "desc desc desc")
                .param("status", "ACTIVE"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/courses"));

    verify(service).update(anyString(), any(UpdateCourseForm.class));
  }

  @Test
  void edit__returns_edit_view_on_binding_errors() throws Exception {
    when(categories.findAll()).thenReturn(List.of(new Category("Back-End", "backend", "#0AF", 1)));

    mockMvc
        .perform(
            post("/admin/course/{code}/edit", "java-web")
                .param("name", "")
                .param("instructorEmail", "invalid-email")
                .param("categoryId", "")
                .param("description", "short")
                .param("status", "ACTIVE"))
        .andExpect(status().isOk())
        .andExpect(view().name("course-edit"))
        .andExpect(model().attributeExists("code"))
        .andExpect(model().attributeExists("categories"))
        .andExpect(model().attributeExists("statuses"));

    verify(service, never()).update(anyString(), any(UpdateCourseForm.class));
  }

  @Test
  void edit__redirects_back_to_edit_when_service_throws() throws Exception {
    when(service.update(eq("java-web"), any(UpdateCourseForm.class)))
        .thenThrow(new IllegalArgumentException("boom"));

    mockMvc
        .perform(
            post("/admin/course/{code}/edit", "java-web")
                .param("name", "Name")
                .param("instructorEmail", "ana@alura.com")
                .param("categoryId", "1")
                .param("description", "valid description here")
                .param("status", "ACTIVE"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/course/java-web/edit"));

    verify(service).update(eq("java-web"), any(UpdateCourseForm.class));
  }
}
