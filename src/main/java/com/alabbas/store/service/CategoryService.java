package com.alabbas.store.service;
import com.alabbas.store.entity.Category;
import com.alabbas.store.exception.BusinessException;
import com.alabbas.store.exception.ResourceNotFoundException;
import com.alabbas.store.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameEnIgnoreCase(category.getNameEn())) {
            throw new BusinessException("category.name.en.exists");
        }

        if (categoryRepository.existsByNameAr(category.getNameAr())) {
            throw new BusinessException("category.name.ar.exists");
        }


        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category.not.found", id));
    }


    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = getCategoryById(id);

        existingCategory.setNameEn(updatedCategory.getNameEn());
        existingCategory.setNameAr(updatedCategory.getNameAr());
        existingCategory.setActive(updatedCategory.getActive());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        Category existingCategory = getCategoryById(id);
        categoryRepository.delete(existingCategory);
    }

}
