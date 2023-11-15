package fontys.individual.school.business.impl;

import fontys.individual.school.business.Interface.CategoryUseCases;
import fontys.individual.school.business.impl.Converter.CategoryConverter;
import fontys.individual.school.domain.Category;
import fontys.individual.school.domain.HttpResponseRequest.CreateCategoryRequest;
import fontys.individual.school.domain.HttpResponseRequest.CreateCategoryResponse;
import fontys.individual.school.domain.HttpResponseRequest.GetAllCategoriesResponse;
import fontys.individual.school.persistence.CategoryRepository;
import fontys.individual.school.persistence.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryUseCases {
    private final CategoryRepository categoryRepository;
    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
        CategoryEntity savedCategory = saveCategory(request);

        return CreateCategoryResponse.builder()
                .category(CategoryConverter.convertToCategory(savedCategory))
                .build();
    }

    @Override
    public GetAllCategoriesResponse getAll() {
        List<CategoryEntity> results = categoryRepository.findAll();

        List<Category> categories = results
                .stream()
                .map(CategoryConverter::convertToCategory)
                .toList();

        return GetAllCategoriesResponse.builder()
                .categoryList(categories)
                .build();
    }

    public CategoryEntity saveCategory(CreateCategoryRequest request){
        CategoryEntity savedCategory = CategoryEntity.builder()
                .description(request.getDescription())
                .name(request.getName())
                .build();

        return categoryRepository.save(savedCategory);
    }
}
