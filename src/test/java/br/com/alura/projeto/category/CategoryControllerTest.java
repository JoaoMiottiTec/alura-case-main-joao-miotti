package br.com.alura.projeto.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryRepository repo;

    @TestConfiguration
    static class ViewConfig {
        @Bean
        InternalResourceViewResolver viewResolver() {
            InternalResourceViewResolver r = new InternalResourceViewResolver();
            r.setPrefix("/WEB-INF/views/");
            r.setSuffix(".jsp");
            return r;
        }
    }

    @Test
    void list__renders_category_list() throws Exception {
        when(repo.findAll()).thenReturn(List.of(
                new Category("Back-End", "backend", "#0AF", 1),
                new Category("Front-End", "frontend", "#0BF", 2)
        ));

        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/list"))
                .andExpect(model().attributeExists("categories"));

        verify(repo).findAll();
    }

    @Test
    void createForm__renders_newForm() throws Exception {
        mockMvc.perform(get("/admin/category/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/newForm"));
    }

    @Test
    void save__redirects_on_success() throws Exception {
        when(repo.existsByCode("backend")).thenReturn(false);

        mockMvc.perform(post("/admin/category/new")
                        .param("name", "Back-End")
                        .param("code", "backend")
                        .param("color", "#00AAFF")
                        .param("order", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        verify(repo).save(any(Category.class));
    }

    @Test
    void save__returns_form_when_duplicate_code() throws Exception {
        when(repo.existsByCode("backend")).thenReturn(true);

        mockMvc.perform(post("/admin/category/new")
                        .param("name", "Back-End")
                        .param("code", "backend")
                        .param("color", "#00AAFF")
                        .param("order", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/newForm"));
    }

    @Test
    void update__redirects_on_success() throws Exception {
        Category existing = new Category("Back-End", "backend", "#00AAFF", 1);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));

        mockMvc.perform(post("/admin/category/{id}/edit", 1L)
                        .param("name", "Novo Nome")
                        .param("color", "#112233")
                        .param("order", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        verify(repo).save(any(Category.class));
    }

    @Test
    void update__redirects_with_error_when_invalid_fields() throws Exception {
        mockMvc.perform(post("/admin/category/{id}/edit", 1L)
                        .param("name", "")
                        .param("color", "blue")
                        .param("order", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    void update__redirects_with_error_when_category_not_found() throws Exception {
        when(repo.findById(99L)).thenReturn(Optional.empty());
    
        mockMvc.perform(post("/admin/category/{id}/edit", 99L)
                        .param("name", "Valid Name")
                        .param("color", "#112233")
                        .param("order", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }
}
