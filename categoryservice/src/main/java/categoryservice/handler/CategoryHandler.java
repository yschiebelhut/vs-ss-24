package categoryservice.handler;

import categoryservice.database.dataAccessObjects.CategoryDAO;
import categoryservice.database.dataobjects.Category;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CategoryHandler {

    private CategoryDAO helper;

    private final WebClient webClient;

    void log(String message) {
        System.out.println("[CategoryHandler] " + message);
    }

    public CategoryHandler(WebClient webClient) {
        helper = new CategoryDAO();
        this.webClient = webClient;
    }

    public List<Category> getAllCategories() {
        log("Retrieving all categories");
        return helper.getObjectList();
    }

    public Category getCategory(Integer id) {
        log("Retrieving category " + id);
        Category category = helper.getObjectById(id);
        if (category == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Invalid Category ID"
            );
        }
        
        return category;
    }

    public Category getCategoryByName(String name) {
        log("Searching for category " + name);
        Category category = helper.getObjectByName(name);

        if (category == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Invalid Category Name"
            );
        }

        return category;
    }

    public void addCategory(Category category) {
        Category cat = new Category(category.getName());
        helper.saveObject(cat);
        log("Added Category " + category.getName());
    }

    public void delCategory(Category cat) {
        deleteProductsByCategory(cat.getId());

        helper.deleteById(cat.getId());
        log("Deleted Category" + cat.getName());
    }

    public void deleteCategoryById(Integer id) {
        deleteProductsByCategory(id);
        
        helper.deleteById(id);
        log("Deleted Category " + id);
    }

    private void deleteProductsByCategory(Integer categoryId) {
        try {
            webClient.delete()
                    .uri("/delete-product-by-category/"+categoryId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            log("Deleted all Products belonging to " + categoryId);                    
        } catch(Exception e) {
            log("Failed to delete all Products");
            e.printStackTrace();

            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to delete related products"
            );
        }
    }
}
