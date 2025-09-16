package br.com.alura.projeto.registration;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.course.CourseStatus;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegistrationIntegrationTest {

  @Container
  static final MySQLContainer<?> mysql =
      new MySQLContainer<>("mysql:8.4")
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

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;
  @Autowired CourseRepository courses;
  @Autowired UserRepository users;
  @Autowired RegistrationRepository registrations;

  @Autowired EntityManager em;
  @Autowired PlatformTransactionManager txm;

  final String activeCode = "JAVA01";
  final String inactiveCode = "JAVA02";
  final String email = "tito@alura.com";

  @BeforeEach
  void seed() throws Exception {
    registrations.deleteAll();
    courses.deleteAll();
    users.deleteAll();

    User student = new User("Tito", email, Role.STUDENT, "secret");
    User instructor = new User("Ana Silva", "ana@alura.com", Role.INSTRUCTOR, "secret");
    users.saveAll(List.of(student, instructor));

    Object category = newCategory("Programming");

    Course active = newInstance(Course.class);
    setProp(active, "code", activeCode);
    setProp(active, "status", CourseStatus.ACTIVE);
    setProp(active, "name", "Java Fundamentals");
    setProp(active, "instructor", instructor);
    setProp(active, "category", category);

    Course inactive = newInstance(Course.class);
    setProp(inactive, "code", inactiveCode);
    setProp(inactive, "status", CourseStatus.INACTIVE);
    setProp(inactive, "name", "Spring Boot API");
    setProp(inactive, "instructor", instructor);
    setProp(inactive, "category", category);

    courses.saveAll(List.of(active, inactive));
  }

  @Test
  @Order(1)
  void post_register_created_and_persists() throws Exception {
    NewRegistrationDTO body = new NewRegistrationDTO();
    body.setCourseCode(activeCode);
    body.setStudentEmail(email);

    mvc.perform(
            post("/registration/new")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(body)))
        .andExpect(status().is2xxSuccessful());

    Assertions.assertTrue(
        registrations.existsByUserAndCourse(
            users.findByEmail(email).orElseThrow(), courses.findByCode(activeCode).orElseThrow()));
  }

  @Test
  @Order(2)
  void post_register_fails_when_already_registered() throws Exception {
    registrations.save(
        new Registration(
            users.findByEmail(email).orElseThrow(), courses.findByCode(activeCode).orElseThrow()));

    NewRegistrationDTO body = new NewRegistrationDTO();
    body.setCourseCode(activeCode);
    body.setStudentEmail(email);

    ServletException ex =
        Assertions.assertThrows(
            ServletException.class,
            () ->
                mvc.perform(
                        post("/registration/new")
                            .contentType(APPLICATION_JSON)
                            .content(om.writeValueAsString(body)))
                    .andReturn());
    Throwable cause = ex.getRootCause() != null ? ex.getRootCause() : ex.getCause();
    Assertions.assertInstanceOf(IllegalStateException.class, cause);
    Assertions.assertTrue(cause.getMessage().contains("already registered"));
  }

  @Test
  @Order(3)
  void post_register_fails_when_course_not_active() throws Exception {
    NewRegistrationDTO body = new NewRegistrationDTO();
    body.setCourseCode(inactiveCode);
    body.setStudentEmail(email);

    ServletException ex =
        Assertions.assertThrows(
            ServletException.class,
            () ->
                mvc.perform(
                        post("/registration/new")
                            .contentType(APPLICATION_JSON)
                            .content(om.writeValueAsString(body)))
                    .andReturn());
    Throwable cause = ex.getRootCause() != null ? ex.getRootCause() : ex.getCause();
    Assertions.assertInstanceOf(IllegalStateException.class, cause);
    Assertions.assertTrue(cause.getMessage().contains("not ACTIVE"));
  }

  @Test
  @Order(4)
  void get_report_returns_items() throws Exception {
    var student = users.findByEmail(email).orElseThrow();
    var active = courses.findByCode(activeCode).orElseThrow();
    registrations.save(new Registration(student, active));

    mvc.perform(get("/registration/report"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$", not(empty())));
  }

  private Object newCategory(String name) throws Exception {
    Class<?> catClazz = Class.forName("br.com.alura.projeto.category.Category");
    Object category = newInstance(catClazz);
    setProp(category, "name", name);
    setProp(category, "code", "CAT-PROG");
    setProp(category, "color", "#0A84FF");
    setProp(category, "order", 1);

    new TransactionTemplate(txm)
        .execute(
            status -> {
              em.persist(category);
              em.flush();
              return null;
            });
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
    String setter =
        "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);

    for (Method m : clazz.getMethods()) {
      if (m.getName().equals(setter)
          && m.getParameterCount() == 1
          && m.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
        try {
          m.invoke(target, value);
          return;
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    }
    for (Method m : clazz.getDeclaredMethods()) {
      if (m.getName().equals(setter)
          && m.getParameterCount() == 1
          && m.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
        try {
          m.setAccessible(true);
          m.invoke(target, value);
          return;
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    }
    Field f = findField(clazz, propertyName);
    if (f == null)
      throw new RuntimeException(
          "Missing setter and field: " + propertyName + " on " + clazz.getName());
    try {
      f.setAccessible(true);
      f.set(target, value);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static Field findField(Class<?> type, String name) {
    Class<?> c = type;
    while (c != null) {
      try {
        return c.getDeclaredField(name);
      } catch (NoSuchFieldException ignored) {
        c = c.getSuperclass();
      }
    }
    return null;
  }
}
