package fontys.individual.school;

import fontys.individual.school.business.impl.CategoryServiceImpl;
import fontys.individual.school.domain.Category;
import fontys.individual.school.domain.HttpResponseRequest.CreateCategoryRequest;
import fontys.individual.school.domain.HttpResponseRequest.CreateCategoryResponse;
import fontys.individual.school.domain.HttpResponseRequest.GetAllCategoriesResponse;
import fontys.individual.school.persistence.CategoryRepository;
import fontys.individual.school.persistence.entity.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void createCategory_ValidRequest_ReturnsCreateCategoryResponse() {
        // Arrange
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Test Category");
        request.setDescription("Test Description");

        CategoryEntity savedCategory = CategoryEntity.builder()
                .id(1L)
                .name(request.getName())
                .description(request.getDescription())
                .build();
        when(categoryRepository.save(any())).thenReturn(savedCategory);

        // Act
        CreateCategoryResponse response = categoryService.createCategory(request);

        // Assert
        assertEquals(savedCategory.getId(), response.getCategory().getId());
        assertEquals(request.getName(), response.getCategory().getName());
        assertEquals(request.getDescription(), response.getCategory().getDescription());
        verify(categoryRepository).save(any());
    }

    @Test
    void getAll_ReturnsGetAllCategoriesResponse() {
        // Arrange
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(CategoryEntity.builder().id(1L).name("Category 1").description("Description 1").build());
        categoryEntities.add(CategoryEntity.builder().id(2L).name("Category 2").description("Description 2").build());
        when(categoryRepository.findAll()).thenReturn(categoryEntities);

        // Act
        GetAllCategoriesResponse response = categoryService.getAll();

        // Assert
        assertEquals(categoryEntities.size(), response.getCategoryList().size());
        for (int i = 0; i < categoryEntities.size(); i++) {
            CategoryEntity categoryEntity = categoryEntities.get(i);
            Category category = response.getCategoryList().get(i);
            assertEquals(categoryEntity.getId(), category.getId());
            assertEquals(categoryEntity.getName(), category.getName());
            assertEquals(categoryEntity.getDescription(), category.getDescription());
        }
        verify(categoryRepository).findAll();
    }
}