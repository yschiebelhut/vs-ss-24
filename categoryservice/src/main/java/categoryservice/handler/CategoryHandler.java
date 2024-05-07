package categoryservice.handler;

import categoryservice.database.dataAccessObjects.CategoryDAO;
import categoryservice.database.dataobjects.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryHandler {

    private CategoryDAO helper;

    public CategoryHandler() {
        helper = new CategoryDAO();
    }

    public List<Category> getAllCategories() {
        return helper.getObjectList();
    }

    public Category getCategory(Integer id) {
        return helper.getObjectById(id);
    }

    public Category getCategoryByName(String name) {
        return helper.getObjectByName(name);
    }

    public Boolean addCategory(Category category) {
        Category cat = new Category(category.getName());
        helper.saveObject(cat);
        return true;
    }

    public Boolean delCategory(Category cat) {
// 		Products are also deleted because of relation in Category.java
        helper.deleteById(cat.getId());
        return true;
    }

    public Boolean deleteCategoryById(Integer id) {
        helper.deleteById(id);
        return true;
    }
}
