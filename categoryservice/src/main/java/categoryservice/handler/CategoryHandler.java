package categoryservice.handler;

import categoryservice.database.dataAccessObjects.CategoryDAO;
import categoryservice.database.dataobjects.Category;
import org.springframework.stereotype.Service;
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
        return helper.getObjectById(id);
    }

    public Category getCategoryByName(String name) {
        log("Searching for category " + name);
        return helper.getObjectByName(name);
    }

    public Boolean addCategory(Category category) {
        Category cat = new Category(category.getName());
        helper.saveObject(cat);
        log("Added Category " + category.getName());
        return true;
    }

    public Boolean delCategory(Category cat) {
        if (!deleteProductsByCategory(cat.getId())) {
            return false;
        }

        helper.deleteById(cat.getId());
        log("Deleted Category" + cat.getName());
        return true;
    }

    public Boolean deleteCategoryById(Integer id) {
        if (!deleteProductsByCategory(id)) {
            return false;
        }
        
        helper.deleteById(id);
        log("Deleted Category " + id);
        return true;
    }

    private Boolean deleteProductsByCategory(Integer categoryId) {
        try {
            webClient.delete()
                    .uri("/delete-product-by-category/"+categoryId)
                    .retrieve();
            log("Deleted all Products belonging to " + categoryId);
            return true;
        } catch(Exception e) {
            log("Failed to delete all Products");
            e.printStackTrace();
            return false;
        }
    }
}
