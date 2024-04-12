package categoryservice.controller;

import categoryservice.handler.CategoryHandler;
import categoryservice.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/category/")
public class CategoryController {

    @Autowired
    private CategoryHandler categoryHandler;

    @GetMapping(value = "get-categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryHandler.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping(value = "add-category")
    public ResponseEntity<Boolean> addCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryHandler.addCategory(category), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete-category/{id}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable Integer id) {
        return new ResponseEntity<>(categoryHandler.deleteCategory(id), HttpStatus.OK);
    }
}
