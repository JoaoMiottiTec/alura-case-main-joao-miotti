package br.com.alura.projeto.course;

public record courseDTO(Long id, String name, String code, String instructorEmail, String description, String status  ) {
    public courseDTO( Course course) {
        this(course.getId(), course.getName(), course.getCode(), course.getDescription(), course.getInstructorEmail(), course.getStatus())
    }
}