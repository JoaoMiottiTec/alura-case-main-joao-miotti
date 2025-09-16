package br.com.alura.projeto.category;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/admin/categories")
    public String list(Model model) {
        List<CategoryDTO> list = categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .toList();

        model.addAttribute("categories", list);
        return "admin/category/list";
    }

    @GetMapping("/admin/category/new")
    public String create(NewCategoryForm newCategory, Model model) {
        return "admin/category/newForm";
    }

    @Transactional
    @PostMapping("/admin/category/new")
    public String save(@Valid NewCategoryForm form, BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return create(form, model);
        }
        if (categoryRepository.existsByCode(form.getCode())) {
            return create(form, model);
        }
        categoryRepository.save(form.toModel());
        ra.addFlashAttribute("success", "Categoria criada com sucesso!");
        return "redirect:/admin/categories";
    }

    @Transactional
    @PostMapping("/admin/category/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid UpdateCategoryForm form,
                         BindingResult binding,
                         RedirectAttributes ra) {
                        
        if (form.getOrder() < 1) {
            binding.rejectValue("order", "min", "Ordem deve ser maior ou igual a 1.");
        }
        String color = form.getColor() == null ? "" : form.getColor().trim();
        if (!color.matches("^#[0-9a-fA-F]{6}$")) {
            binding.rejectValue("color", "pattern", "Cor deve estar no formato #RRGGBB.");
        }

        String name = form.getName() == null ? "" : form.getName().trim();
        if (name.isEmpty() || name.length() < 2 || name.length() > 50) {
            binding.rejectValue("name", "length", "Nome deve ter entre 2 e 50 caracteres.");
        }

        if (binding.hasErrors()) {
            ra.addFlashAttribute("error", "Não foi possível salvar. Verifique os campos.");
            return "redirect:/admin/categories";
        }

        Category entity = categoryRepository.findById(id).orElse(null);
        if (entity == null) {
            ra.addFlashAttribute("error", "Categoria não encontrada: " + id);
            return "redirect:/admin/categories";
        }

        entity.update(name, entity.getCode(), color, form.getOrder());
        categoryRepository.save(entity);

        ra.addFlashAttribute("success", "Categoria atualizada com sucesso!");
        return "redirect:/admin/categories";
    }
}
