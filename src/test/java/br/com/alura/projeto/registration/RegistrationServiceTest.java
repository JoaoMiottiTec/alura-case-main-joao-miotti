package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.course.CourseStatus;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

  @Mock RegistrationRepository registrations;
  @Mock UserRepository users;
  @Mock CourseRepository courses;

  @Captor ArgumentCaptor<Registration> regCaptor;

  RegistrationService service;

  @BeforeEach
  void setUp() {
    service = new RegistrationService(registrations, users, courses);
  }

  private NewRegistrationDTO dto(String courseCode, String email) {
    NewRegistrationDTO d = new NewRegistrationDTO();
    d.setCourseCode(courseCode);
    d.setStudentEmail(email);
    return d;
  }

  @Nested
  @DisplayName("register(dto)")
  class Register {

    @Test
    @DisplayName("should register when course is ACTIVE, user exists and not registered yet")
    void shouldRegister_whenCourseActiveUserExistsAndNotRegistered() {
      String courseCode = "JAVA01";
      String email = "tito@alura.com";
      NewRegistrationDTO input = dto(courseCode, email);

      Course course = mock(Course.class);
      when(course.getStatus()).thenReturn(CourseStatus.ACTIVE);
      when(course.getCode()).thenReturn(courseCode);

      User student = mock(User.class);
      when(student.getEmail()).thenReturn(email);

      when(courses.findByCode(courseCode)).thenReturn(Optional.of(course));
      when(users.findByEmail(email)).thenReturn(Optional.of(student));
      when(registrations.existsByUserAndCourse(student, course)).thenReturn(false);

      LocalDateTime fixedNow = LocalDateTime.of(2025, 9, 16, 12, 0, 0);
      Registration savedMock = mock(Registration.class);
      when(savedMock.getRegisteredAt()).thenReturn(fixedNow);
      when(registrations.save(any(Registration.class))).thenReturn(savedMock);

      RegistrationDTO out = service.register(input);

      assertThat(out.getCourseCode()).isEqualTo(courseCode);
      assertThat(out.getStudentEmail()).isEqualTo(email);
      assertThat(out.getRegisteredAt()).isEqualTo(fixedNow);

      verify(registrations).save(regCaptor.capture());
      Registration toSave = regCaptor.getValue();
      try {
        assertThat(toSave.getUser()).isSameAs(student);
        assertThat(toSave.getCourse()).isSameAs(course);
      } catch (Throwable ignored) {}

      verify(courses).findByCode(courseCode);
      verify(users).findByEmail(email);
      verify(registrations).existsByUserAndCourse(student, course);
      verifyNoMoreInteractions(courses, users, registrations);
    }

    @Test
    @DisplayName("should throw when course not found")
    void shouldThrow_whenCourseNotFound() {
      String code = "NOPE";
      when(courses.findByCode(code)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.register(dto(code, "tito@alura.com")))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Course not found");

      verify(courses).findByCode(code);
      verifyNoMoreInteractions(courses);
      verifyNoInteractions(users, registrations);
    }

    @Test
    @DisplayName("should throw when course is not ACTIVE")
    void shouldThrow_whenCourseNotActive() {
      String code = "JAVA02";
      Course inactive = mock(Course.class);
      when(inactive.getStatus()).thenReturn(CourseStatus.INACTIVE);
      when(courses.findByCode(code)).thenReturn(Optional.of(inactive));

      assertThatThrownBy(() -> service.register(dto(code, "tito@alura.com")))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("not ACTIVE");

      verify(courses).findByCode(code);
      verifyNoMoreInteractions(courses);
      verifyNoInteractions(users, registrations);
    }

    @Test
    @DisplayName("should throw when user not found")
    void shouldThrow_whenUserNotFound() {
      String code = "JAVA01";
      String email = "ghost@alura.com";

      Course active = mock(Course.class);
      when(active.getStatus()).thenReturn(CourseStatus.ACTIVE);
      when(courses.findByCode(code)).thenReturn(Optional.of(active));
      when(users.findByEmail(email)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.register(dto(code, email)))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("User not found");

      verify(courses).findByCode(code);
      verify(users).findByEmail(email);
      verifyNoMoreInteractions(courses, users);
      verifyNoInteractions(registrations);
    }

    @Test
    @DisplayName("should throw when user already registered in course")
    void shouldThrow_whenAlreadyRegistered() {
      String code = "JAVA01";
      String email = "tito@alura.com";

      Course active = mock(Course.class);
      when(active.getStatus()).thenReturn(CourseStatus.ACTIVE);

      User student = mock(User.class);

      when(courses.findByCode(code)).thenReturn(Optional.of(active));
      when(users.findByEmail(email)).thenReturn(Optional.of(student));
      when(registrations.existsByUserAndCourse(student, active)).thenReturn(true);

      assertThatThrownBy(() -> service.register(dto(code, email)))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("already registered");

      verify(courses).findByCode(code);
      verify(users).findByEmail(email);
      verify(registrations).existsByUserAndCourse(student, active);
      verifyNoMoreInteractions(courses, users, registrations);
    }
  }
}
