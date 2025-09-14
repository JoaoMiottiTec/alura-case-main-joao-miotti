package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock CourseRepository courses;
    @Mock UserRepository users;
    @Mock CategoryRepository categories;

    @InjectMocks CourseService service;

    private NewCourseForm form;
    private User instructor;
    private Category category;

    @BeforeEach
    void setup() {
        form = new NewCourseForm();
        form.setName("Java Web");
        form.setCode("java-web");
        form.setDescription("desc");
        form.setInstructorEmail("ana@alura.com");
        form.setCategoryId(1L);

        instructor = new User("Ana", "ana@alura.com", Role.INSTRUCTOR, "x");
        category   = new Category("Back-End","backend","#00AAFF",1);
    }

    @Test
    void create_ok() {
        when(courses.existsByCode("java-web")).thenReturn(false);
        when(users.findByEmail("ana@alura.com")).thenReturn(Optional.of(instructor));
        when(categories.findById(1L)).thenReturn(Optional.of(category));

        Course saved = new Course(form.getName(), form.getCode(), instructor, category, form.getDescription());
        when(courses.save(any(Course.class))).thenReturn(saved);

        CourseDTO dto = service.create(form);

        assertEquals("java-web", dto.getCode());
        assertEquals("Java Web", dto.getName());
        verify(courses).save(any(Course.class));
    }

    @Test
    void create_fails_whenInvalidCode() {
        form.setCode("BAD"); // não passa na regex (maiúsculo)
        assertThrows(IllegalArgumentException.class, () -> service.create(form));
        verifyNoInteractions(users, categories, courses);
    }

    @Test
    void create_fails_whenDuplicateCode() {
        when(courses.existsByCode("java-web")).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> service.create(form));
        verifyNoMoreInteractions(users, categories);
    }

    @Test
    void create_fails_whenInstructorNotFound() {
        when(courses.existsByCode("java-web")).thenReturn(false);
        when(users.findByEmail("ana@alura.com")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.create(form));
    }

    @Test
    void create_fails_whenNotInstructorRole() {
        when(courses.existsByCode("java-web")).thenReturn(false);
        User student = new User("Bob","ana@alura.com", Role.STUDENT, "x");
        when(users.findByEmail("ana@alura.com")).thenReturn(Optional.of(student));
        assertThrows(IllegalArgumentException.class, () -> service.create(form));
    }

    @Test
    void create_fails_whenCategoryNotFound() {
        when(courses.existsByCode("java-web")).thenReturn(false);
        when(users.findByEmail("ana@alura.com")).thenReturn(Optional.of(instructor));
        when(categories.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.create(form));
    }

    @Test
    void inactivate_ok() {
        Course course = new Course("Java Web","java-web", instructor, category, "desc");
        when(courses.findByCode("java-web")).thenReturn(Optional.of(course));

        service.inactivateByCode("java-web");

        assertEquals(CourseStatus.INACTIVE, course.getStatus());
        assertNotNull(course.getInactiveAt());
    }

    @Test
    void inactivate_fails_whenNotFound() {
        when(courses.findByCode("nope")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.inactivateByCode("nope"));
    }

    @Test
    void reactivate_ok() {
        Course course = new Course("Java Web","java-web", instructor, category, "desc");
        course.updateStatus(CourseStatus.INACTIVE);

        when(courses.findByCode("java-web")).thenReturn(Optional.of(course));

        service.reactivateByCode("java-web");

        assertEquals(CourseStatus.ACTIVE, course.getStatus());
        assertNull(course.getInactiveAt());
    }

    @Test
    void reactivate_fails_whenNotFound() {
        when(courses.findByCode("nope")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.reactivateByCode("nope"));
    }

}
