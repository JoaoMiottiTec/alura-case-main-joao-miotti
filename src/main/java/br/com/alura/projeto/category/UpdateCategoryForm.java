package br.com.alura.projeto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UpdateCategoryForm {

    private String name;
    private int order;
    private String color;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
