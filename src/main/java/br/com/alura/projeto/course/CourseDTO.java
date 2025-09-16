package br.com.alura.projeto.course;

public class CourseDTO {
  private final Long id;
  private final String name;
  private final String code;
  private final String instructorEmail;
  private final Long categoryId;
  private final String categoryName;
  private final String description;
  private final String status;

  public CourseDTO(Course c) {
    this.id = c.getId();
    this.name = c.getName();
    this.code = c.getCode();
    this.instructorEmail = c.getInstructor().getEmail();
    this.categoryId = c.getCategory().getId();
    this.categoryName = c.getCategory().getName();
    this.description = c.getDescription();
    this.status = c.getStatus().name();
  }

  public CourseDTO(
      Long id,
      String name,
      String code,
      String instructorEmail,
      Long categoryId,
      String categoryName,
      String description,
      String status) {
    this.id = id;
    this.name = name;
    this.code = code;
    this.instructorEmail = instructorEmail;
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.description = description;
    this.status = status;
  }

  public CourseDTO(
      Long id,
      String name,
      String code,
      String instructorEmail,
      String categoryName,
      String description,
      String status) {
    this(id, name, code, instructorEmail, null, categoryName, description, status);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }

  public String getInstructorEmail() {
    return instructorEmail;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public String getDescription() {
    return description;
  }

  public String getStatus() {
    return status;
  }
}
