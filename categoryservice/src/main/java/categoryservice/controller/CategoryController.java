package categoryservice.controller;

import categoryservice.handler.CategoryHandler;
import categoryservice.database.dataobjects.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/category/")
public class CategoryController {

    @Autowired
    private CategoryHandler categoryHandler;

    @GetMapping(value = "get-category/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Integer id) {
        return new ResponseEntity<>(categoryHandler.getCategory(id), HttpStatus.OK);
    }

//    @GetMapping(value = "get-category/{name}")
//    public ResponseEntity<Category> getCategory(@PathVariable String name) {
//        return new ResponseEntity<>(categoryHandler.getCategoryByName(name), HttpStatus.OK);
//    }

    @GetMapping(value = "get-categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryHandler.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping(value = "add-category")
    public ResponseEntity<Boolean> addCategory(@Valid @RequestBody Category category) {
        return new ResponseEntity<>(categoryHandler.addCategory(category), HttpStatus.OK);
    }

//    @DeleteMapping(value = "delete-category/{cat}")
//    public ResponseEntity<Boolean> deleteCategory(@PathVariable Category cat) {
//        return new ResponseEntity<>(categoryHandler.delCategory(cat), HttpStatus.OK);
//    }

    @DeleteMapping(value = "delete-category/{id}")
    public ResponseEntity<Boolean> deleteCategoryById(@PathVariable Integer id) {
        return new ResponseEntity<>(categoryHandler.deleteCategoryById(id), HttpStatus.OK);
    }

    @GetMapping(value = "exists-category/{id}")
    public ResponseEntity<Boolean> existsCategory(@PathVariable Integer id) {
        return new ResponseEntity<>(categoryHandler.getCategory(id) != null, HttpStatus.OK);
    }
}
