package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.domain.Category;
import fontys.individual.school.persistence.entity.CategoryEntity;

public class CategoryConverter {
    public static Category convertToCategory(CategoryEntity entity){
        if(entity == null){
            return null;
        }
        Category category = Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
        return category;
    }

    public static CategoryEntity convertToCategoryEntity(Category category) {
        if(category == null){
            return null;
        }
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
        return categoryEntity;
    }
}
