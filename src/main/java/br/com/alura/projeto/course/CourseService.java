package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import br.com.alura.projeto.course.CodeValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courses;
    private final UserRepository users;
    private final CategoryRepository categories;

    public CourseService(CourseRepository courses,
                         UserRepository users,
                         CategoryRepository categories) {
        this.courses = courses;
        this.users = users;
        this.categories = categories;
    }

    public List<CourseDTO> listAll() {
        return courses.findAll().stream()
                .map(CourseDTO::new)
                .toList();
    }

    @Transactional
    public CourseDTO create(NewCourseForm form) {
        if (!CodeValidator.isValid(form.getCode())) {
            throw new IllegalArgumentException("Invalid code: use lowercase letters and hyphens.");
        }
        if (courses.existsByCode(form.getCode())) {
            throw new IllegalStateException("Code already in use.");
        }

        User instructor = users.findByEmail(form.getInstructorEmail())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found for the given email."));
        if (instructor.getRole() != Role.INSTRUCTOR) {
            throw new IllegalArgumentException("User is not an INSTRUCTOR.");
        }

        Category category = categories.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found."));

        Course course = new Course(
                form.getName(),
                form.getCode(),
                instructor,
                category,
                form.getDescription()
        );

        return new CourseDTO(courses.save(course));
    }

    @Transactional
    public void inactivateByCode(String code) {
        Course course = courses.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found for the given code."));
        course.updateStatus(CourseStatus.INACTIVE);
    }

    @Transactional
    public void reactivateByCode(String code) {
        Course course = courses.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found for the given code."));
        course.updateStatus(CourseStatus.ACTIVE);
    }
}
