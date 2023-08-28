package com.btk.ordercorner.service;

import java.util.List;

import com.btk.ordercorner.model.dto.CategoryDto;
import com.btk.ordercorner.model.vm.AddCategoryVm;
import com.btk.ordercorner.model.vm.UpdateCategoryVm;

public interface CategoryService {
    
    List<CategoryDto> getAllCategories();
    CategoryDto getByCategoryId(int categoryId);
    int addNewCategory(AddCategoryVm categoryVm);
    String deleteByCategoryId(int categoryId);
    boolean existsById(int categoryId);
    String updateCategory(UpdateCategoryVm categoryVm);
}
