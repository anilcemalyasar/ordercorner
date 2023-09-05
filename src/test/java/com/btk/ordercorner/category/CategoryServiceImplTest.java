package com.btk.ordercorner.category;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.btk.ordercorner.model.entity.Category;
import com.btk.ordercorner.model.entity.Product;
import com.btk.ordercorner.repository.CategoryRepository;
import com.btk.ordercorner.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImpl categoryServiceImpl;

    @Test
    void test_findAllCategories() {
        // given
        List<Category> categories = new ArrayList<Category>();
        Category category1 = new Category(1, "Elektronik", new ArrayList<Product>());
        Category category2 = new Category(2, "Giyim", new ArrayList<Product>());
        categories.add(category1);
        categories.add(category2);

        // when
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> categoryList = categoryServiceImpl.findAllCategories();

        assertEquals(categories.size(), categoryList.size());
    }

    @Test
    void test_findCategoryById() {
        // given
        Category category1 = new Category(1, "Elektronik", new ArrayList<Product>());
        Optional<Category> optional = Optional.of(category1);
        // when
        when(categoryRepository.findById(category1.getCategoryId())).thenReturn(optional);

        Category found = categoryServiceImpl.findById(1);

        assertAll(
            () -> assertEquals(found.getCategoryId(), category1.getCategoryId()),
            () -> assertEquals(found.getCategoryName(), category1.getCategoryName()),
            () -> assertNotNull(found.getCategoryId())
        );
    }

}
