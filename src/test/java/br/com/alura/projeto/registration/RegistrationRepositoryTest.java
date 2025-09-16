package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.course.CourseStatus;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegistrationRepositoryTest {

  @Container
  static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4")
      .withDatabaseName("testdb")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void props(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", mysql::getJdbcUrl);
    r.add("spring.datasource.username", mysql::getUsername);
    r.add("spring.datasource.password", mysql::getPassword);
    r.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    r.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    r.add("spring.flyway.enabled", () -> true);
  }

  @Autowired RegistrationRepository registrations;
  @Autowired CourseRepository courses;
  @Autowired UserRepository users;
  @Autowired EntityManager em;

  User instructor;
  Object category;
  Course javaFund;
  Course springApi;
  User alice;
  User bob;

  @BeforeEach
  void setup() throws Exception {
    registrations.deleteAll();
    courses.deleteAll();
    users.deleteAll();

    instructor = new User("Ana Silva", "ana@alura.com", Role.INSTRUCTOR, "x");
    alice      = new User("Alice", "alice@alura.com", Role.STUDENT, "x");
    bob        = new User("Bob",   "bob@alura.com",   Role.STUDENT, "x");
    users.saveAll(List.of(instructor, alice, bob));

    category = newCategory("Programming");

    javaFund = newInstance(Course.class);
    setProp(javaFund, "code", "JAVA01");
    setProp(javaFund, "name", "Java Fundamentals");
    setProp(javaFund, "status", CourseStatus.ACTIVE);
    setProp(javaFund, "instructor", instructor);
    setProp(javaFund, "category", category);

    springApi = newInstance(Course.class);
    setProp(springApi, "code", "SPR02");
    setProp(springApi, "name", "Spring Boot API");
    setProp(springApi, "status", CourseStatus.ACTIVE);
    setProp(springApi, "instructor", instructor);
    setProp(springApi, "category", category);

    courses.saveAll(List.of(javaFund, springApi));

    registrations.save(new Registration(alice, javaFund));
    registrations.save(new Registration(bob,   javaFund));
    registrations.save(new Registration(alice, springApi));
  }

  @Test
  void existsByUserAndCourse_returns_true_and_false() {
    boolean exists1 = registrations.existsByUserAndCourse(alice, javaFund);
    boolean exists2 = registrations.existsByUserAndCourse(bob, springApi);

    assertThat(exists1).isTrue();
    assertThat(exists2).isFalse();
  }

  @Test
  void reportTopCourses_returns_projection_in_desc_order() {
    List<RegistrationReportItem> items = registrations.reportTopCourses();

    assertThat(items).hasSize(2);

    RegistrationReportItem first  = items.get(0);
    RegistrationReportItem second = items.get(1);

    assertThat(first.getCourseCode()).isEqualTo("JAVA01");
    assertThat(first.getCourseName()).isEqualTo("Java Fundamentals");
    assertThat(first.getInstructorName()).isEqualTo("Ana Silva");
    assertThat(first.getInstructorEmail()).isEqualTo("ana@alura.com");
    assertThat(first.getRegistrations()).isEqualTo(2L);

    assertThat(second.getCourseCode()).isEqualTo("SPR02");
    assertThat(second.getCourseName()).isEqualTo("Spring Boot API");
    assertThat(second.getInstructorName()).isEqualTo("Ana Silva");
    assertThat(second.getInstructorEmail()).isEqualTo("ana@alura.com");
    assertThat(second.getRegistrations()).isEqualTo(1L);
  }

  private Object newCategory(String name) throws Exception {
    Class<?> catClazz = Class.forName("br.com.alura.projeto.category.Category");
    Object category = newInstance(catClazz);
    setProp(category, "name",  name);
    setProp(category, "code",  "CAT-PROG");
    setProp(category, "color", "#0A84FF");
    setProp(category, "order", 1);
    em.persist(category);
    em.flush();
    return category;
  }

  private static <T> T newInstance(Class<T> type) {
    try {
      Constructor<T> ctor = type.getDeclaredConstructor();
      ctor.setAccessible(true);
      return ctor.newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Cannot instantiate " + type.getName(), e);
    }
  }

  private static void setProp(Object target, String propertyName, Object value) {
    Class<?> clazz = target.getClass();
    String setter = "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);

    for (Method m : clazz.getMethods()) {
      if (m.getName().equals(setter) && m.getParameterCount() == 1 &&
          m.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
        try { m.invoke(target, value); return; } catch (Exception ex) { throw new RuntimeException(ex); }
      }
    }
    for (Method m : clazz.getDeclaredMethods()) {
      if (m.getName().equals(setter) && m.getParameterCount() == 1 &&
          m.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
        try { m.setAccessible(true); m.invoke(target, value); return; } catch (Exception ex) { throw new RuntimeException(ex); }
      }
    }
    try {
      Field f = findField(clazz, propertyName);
      f.setAccessible(true);
      f.set(target, value);
    } catch (Exception ex) {
      throw new RuntimeException("Cannot set property '" + propertyName + "' on " + clazz.getName(), ex);
    }
  }

  private static Field findField(Class<?> type, String name) throws NoSuchFieldException {
    Class<?> c = type;
    while (c != null) {
      try { return c.getDeclaredField(name); }
      catch (NoSuchFieldException ignored) { c = c.getSuperclass(); }
    }
    throw new NoSuchFieldException(name);
  }
}
