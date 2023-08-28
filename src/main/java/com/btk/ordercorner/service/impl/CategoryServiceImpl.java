package com.btk.ordercorner.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.btk.ordercorner.exception.category.CategoryAlreadyExistsException;
import com.btk.ordercorner.exception.category.CategoryNotFoundException;
import com.btk.ordercorner.model.dto.CategoryDto;
import com.btk.ordercorner.model.entity.Category;
import com.btk.ordercorner.model.vm.AddCategoryVm;
import com.btk.ordercorner.model.vm.UpdateCategoryVm;
import com.btk.ordercorner.repository.CategoryRepository;
import com.btk.ordercorner.service.CategoryService;
import com.btk.ordercorner.util.mapper.ModelMapperManager;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    private CategoryRepository categoryRepository;
    private ModelMapperManager modelMapperManager;
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapperManager modelMapperManager) {
        this.categoryRepository = categoryRepository;
        this.modelMapperManager = modelMapperManager;
    }

    @Cacheable(value = "categories")
    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
        .map(category -> modelMapperManager.forResponse().map(category,CategoryDto.class)).collect(Collectors.toList());
    }

    @Cacheable(value = "categories", key = "#categoryId")
    @Override
    public CategoryDto getByCategoryId(int categoryId) {
        if(!existsById(categoryId)) {
            logger.error("Bu ID numarasına sahip bir  kategori bulunmamaktadır!");
            throw new CategoryNotFoundException("Bu ID numarasına sahip bir  kategori bulunmamaktadır!");
        }
        return modelMapperManager.forResponse().map(categoryRepository.findById(categoryId).get(),CategoryDto.class);
    }


    @CachePut(value = "categories", key = "#categoryVm.categoryId")
    @Override
    public int addNewCategory(AddCategoryVm categoryVm) {
        Authentication auth = getAuth();
        List<Category> categories = categoryRepository.findByCategoryNameIgnoreCase(categoryVm.getCategoryName());
        if (!categories.isEmpty()) {
            String errorMsg = "Bu kategori ismine sahip bir kategori zaten sisteme kayıtlıdır!";
            logger.error(errorMsg);
            throw new CategoryAlreadyExistsException(errorMsg);
        }
        CategoryDto categoryDto = modelMapperManager.forRequest().map(categoryVm, CategoryDto.class);
        Category category = modelMapperManager.forRequest().map(categoryDto, Category.class);

        logger.info(auth.getName() + " isimli admin tarafından " + category.getCategoryName()
                + " adlı kategori sisteme eklendi!");
        categoryRepository.save(category);
        return category.getCategoryId();

    }

    @CacheEvict(value = "categories", key = "#categoryId")
    @Override
    public String deleteByCategoryId(int categoryId) {
        Authentication auth = getAuth();
        if (!existsById(categoryId)) {
            String errorMessage = "Bu ID numarasına sahip bir kategori bulunmamaktadır!";
            logger.error(errorMessage);
            throw new CategoryNotFoundException(errorMessage);
        }
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        String message = auth.getName() + " isimli admin tarafından " + category.getCategoryName()
                + " kategorisi silinmiştir!";
        logger.info(message);
        categoryRepository.deleteById(categoryId);
        return message;

    }

    @Override
    public boolean existsById(int categoryId) {
        return categoryRepository.existsById(categoryId) ? true : false;
    }

    public Authentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }

    @Override
    public String updateCategory(UpdateCategoryVm categoryVm) {
        Authentication auth = getAuth();
        if (!existsById(categoryVm.getCategoryId())) {
            String errorMessage = "Bu ID numarasına sahip bir kategori bulunmamaktadır!";
            logger.error(errorMessage);
            throw new CategoryNotFoundException(errorMessage);
        }
        Category category = categoryRepository.findById(categoryVm.getCategoryId()).get();
        category.setCategoryName(categoryVm.getCategoryName());
        String message = auth.getName() + " tarafından belirtilen kategori güncellenmiştir!\nGüncel kategori adı: " + categoryVm.getCategoryName(); 
        logger.info(message);
        return message;
    }


}
