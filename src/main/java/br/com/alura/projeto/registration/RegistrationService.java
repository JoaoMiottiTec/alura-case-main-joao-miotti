package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.*;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

  private final RegistrationRepository registrations;
  private final UserRepository users;
  private final CourseRepository courses;

  public RegistrationService(
      RegistrationRepository registrations, UserRepository users, CourseRepository courses) {
    this.registrations = registrations;
    this.users = users;
    this.courses = courses;
  }

  @Transactional
  public RegistrationDTO register(NewRegistrationDTO dto) {
    Course course =
        courses
            .findByCode(dto.getCourseCode())
            .orElseThrow(
                () -> new IllegalArgumentException("Course not found: " + dto.getCourseCode()));
    if (course.getStatus() != CourseStatus.ACTIVE) {
      throw new IllegalStateException("Course is not ACTIVE.");
    }

    User student =
        users
            .findByEmail(dto.getStudentEmail())
            .orElseThrow(
                () -> new IllegalArgumentException("User not found: " + dto.getStudentEmail()));

    if (registrations.existsByUserAndCourse(student, course)) {
      throw new IllegalStateException("User already registered in this course.");
    }

    Registration saved = registrations.save(new Registration(student, course));
    return new RegistrationDTO(course.getCode(), student.getEmail(), saved.getRegisteredAt());
  }
}
