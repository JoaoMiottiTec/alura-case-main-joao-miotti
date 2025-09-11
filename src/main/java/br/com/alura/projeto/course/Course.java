package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @NotBlank
    private String name;

    @NotBlank
    @Length(min = 4, max = 10)
    @Column(nullable = false, unique = true, length = 10)
    private String  code;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name =  "instructor_id")
    private User instructor;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @Length(max = 5000)
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status = CourseStatus.ACTIVE;

    @Column(name = "inactive_at")
    private LocalDateTime inactiveAt;

    protected Course() {}

    public Course(String name,String code,User instructor,Category category, String description) {
        this.name = name;
        this.code = code;
        this.instructor = instructor;
        this.category = category;
        this.description = description;
        this.status = CourseStatus.ACTIVE;
        this.inactiveAt = null;
    }

    public void updateStatus(CourseStatus newStatus) {
        if (this.status == newStatus) return;
    
        this.status = newStatus;
        this.inactiveAt = (newStatus == CourseStatus.INACTIVE) 
            ? LocalDateTime.now() 
            : null;
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
    public User getInstructor() {
         return instructor; 
         }
    public Category getCategory() {
         return category; 
         }
    public String getDescription() {
         return description; 
         }
    public CourseStatus getStatus() {
         return status; 
         }
    public LocalDateTime getInactiveAt() {
         return inactiveAt; 
         }

}