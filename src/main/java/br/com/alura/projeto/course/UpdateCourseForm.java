package br.com.alura.projeto.course;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateCourseForm {

    @NotBlank
    @Length(min = 2, max = 100)
    private String name;

    @NotBlank
    @Email
    private String instructorEmail;

    @NotNull
    private Long categoryId;

    @NotBlank
    @Length(min = 10, max = 500)
    private String description;

    @NotNull
    private CourseStatus status;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getInstructorEmail() { return instructorEmail; }
    public void setInstructorEmail(String instructorEmail) { this.instructorEmail = instructorEmail; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
}
