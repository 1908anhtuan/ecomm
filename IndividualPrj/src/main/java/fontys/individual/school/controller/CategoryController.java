package fontys.individual.school.controller;

import fontys.individual.school.business.Interface.CategoryUseCases;
import fontys.individual.school.domain.HttpResponseRequest.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class CategoryController {
    private final CategoryUseCases categoryUseCases;

    @PostMapping
    public ResponseEntity<CreateCategoryResponse> createCategory(@RequestBody @Valid CreateCategoryRequest request){
        CreateCategoryResponse response = categoryUseCases.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<GetAllCategoriesResponse> getAllCategories(){
        GetAllCategoriesResponse response = categoryUseCases.getAll();
        return ResponseEntity.ok(response);
    }
}
