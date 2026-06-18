package com.alabbas.store.controller;
import com.alabbas.store.dto.ApiResponse;
import com.alabbas.store.entity.Category;
import com.alabbas.store.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/api/private/categories")
public class CategoryController {


    private final CategoryService categoryService;
    private final MessageSource messageSource;

    public CategoryController(CategoryService categoryService ,MessageSource messageSource) {
        this.categoryService = categoryService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public ApiResponse createCategory(@Valid @RequestBody Category category) {
         categoryService.createCategory(category);
        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "category.created.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();


    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    public ApiResponse updateCategory(@PathVariable Long id,
                                   @Valid @RequestBody Category category) {
         categoryService.updateCategory(id, category);
        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "category.updated.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();

    }


    @DeleteMapping("/{id}")
    public ApiResponse deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "category.deleted.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();

    }
}
