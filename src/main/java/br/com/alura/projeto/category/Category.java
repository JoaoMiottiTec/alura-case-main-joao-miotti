package br.com.alura.projeto.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Length;

@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank private String name;

  @NotBlank
  @Length(min = 4, max = 10) private String code;

  @NotBlank private String color;

  @NotNull @Min(1)
  @Column(name = "`order`")
  private int order;

  private LocalDateTime createdAt = LocalDateTime.now();

  @Deprecated
  public Category() {}

  public Category(String name, String code, String color, int order) {
    this.name = name;
    this.code = code;
    this.color = color;
    this.order = order;
  }

  public void update(String name, String code, String color, int order) {
    this.name = name;
    this.code = code;
    this.color = color;
    this.order = order;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public int getOrder() {
    return order;
  }

  public String getCode() {
    return code;
  }
}
