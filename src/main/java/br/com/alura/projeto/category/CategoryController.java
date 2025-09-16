package br.com.alura.project.category;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoryController {

  private final CategoryRepository categoryRepository;

  public CategoryController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @GetMapping("/admin/categories")
  public String list(Model model) {
    List<CategoryDTO> list = categoryRepository.findAll().stream().map(CategoryDTO::new).toList();

    model.addAttribute("categories", list);
    return "admin/category/list";
  }

  @GetMapping("/admin/category/new")
  public String create(NewCategoryForm newCategory, Model model) {
    return "admin/category/newForm";
  }

  @Transactional
  @PostMapping("/admin/category/new")
  public String save(
      @Valid NewCategoryForm form, BindingResult result, Model model, RedirectAttributes ra) {
    if (result.hasErrors()) {
      return create(form, model);
    }
    if (categoryRepository.existsByCode(form.getCode())) {
      return create(form, model);
    }
    categoryRepository.save(form.toModel());
    ra.addFlashAttribute("success", "Category created successfully!");
    return "redirect:/admin/categories";
  }

  @Transactional
  @PostMapping("/admin/category/{id}/edit")
  public String update(
      @PathVariable Long id,
      @Valid UpdateCategoryForm form,
      BindingResult binding,
      RedirectAttributes ra) {

    if (form.getOrder() < 1) {
      binding.rejectValue("order", "min", "Order must be greater than or equal to 1.");
    }
    String color = form.getColor() == null ? "" : form.getColor().trim();
    if (!color.matches("^#[0-9a-fA-F]{6}$")) {
      binding.rejectValue("color", "pattern", "Color must be in the format #RRGGBB.");
    }

    String name = form.getName() == null ? "" : form.getName().trim();
    if (name.isEmpty() || name.length() < 2 || name.length() > 50) {
      binding.rejectValue("name", "length", "Name must be between 2 and 50 characters.");
    }

    if (binding.hasErrors()) {
      ra.addFlashAttribute("error", "Could not save. Please check the fields.");
      return "redirect:/admin/categories";
    }

    Category entity = categoryRepository.findById(id).orElse(null);
    if (entity == null) {
      ra.addFlashAttribute("error", "Category not found: " + id);
      return "redirect:/admin/categories";
    }

    entity.update(name, entity.getCode(), color, form.getOrder());
    categoryRepository.save(entity);

    ra.addFlashAttribute("success", "Category updated successfully!");
    return "redirect:/admin/categories";
  }
}
