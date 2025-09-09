package br.com.alura.projeto.category;

public record CategoryDTO(Long id, String name, String code, String color, int order) {
    public CategoryDTO(Category category) {
        this(category.getId(), category.getName(), category.getCode(), category.getColor(), category.getOrder());
    }
}
