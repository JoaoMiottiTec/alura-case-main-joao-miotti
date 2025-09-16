package br.com.alura.projeto.registration;

import java.time.LocalDateTime;

public class RegistrationDTO {
    private final String courseCode;
    private final String studentEmail;
    private final LocalDateTime registeredAt;

    public RegistrationDTO(String courseCode, String studentEmail, LocalDateTime registeredAt) {
        this.courseCode = courseCode;
        this.studentEmail = studentEmail;
        this.registeredAt = registeredAt;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
}
