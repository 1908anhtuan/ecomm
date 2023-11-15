package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.HttpResponseRequest.CreateCategoryRequest;
import fontys.individual.school.domain.HttpResponseRequest.CreateCategoryResponse;
import fontys.individual.school.domain.HttpResponseRequest.GetAllCategoriesResponse;

public interface CategoryUseCases {
    CreateCategoryResponse createCategory(CreateCategoryRequest request);
    GetAllCategoriesResponse getAll();
}
