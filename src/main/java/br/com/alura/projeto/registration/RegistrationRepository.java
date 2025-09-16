package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserAndCourse(User user, Course course);

    @Query(value = """
        SELECT
            c.name  AS courseName,
            c.code  AS courseCode,
            i.name  AS instructorName,
            i.email AS instructorEmail,
            COUNT(r.id) AS registrations
        FROM Registration r
        JOIN Course c   ON c.id = r.course_id
        JOIN `User` i   ON i.id = c.instructor_id
        GROUP BY c.id, c.name, c.code, i.name, i.email
        ORDER BY registrations DESC, c.name ASC
        """, nativeQuery = true)
    List<RegistrationReportItem> reportTopCourses();
}
