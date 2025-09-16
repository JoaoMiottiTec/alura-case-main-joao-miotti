package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "Registration",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_registration_user_course",
            columnNames = {"user_id", "course_id"}))
public class Registration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "course_id")
  private Course course;

  @Column(name = "registered_at", nullable = false)
  private LocalDateTime registeredAt = LocalDateTime.now();

  protected Registration() {}

  public Registration(User user, Course course) {
    this.user = user;
    this.course = course;
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public Course getCourse() {
    return course;
  }

  public LocalDateTime getRegisteredAt() {
    return registeredAt;
  }
}
