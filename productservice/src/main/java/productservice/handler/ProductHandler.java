package productservice.handler;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import productservice.model.Product;
import productservice.model.ProductDAO;

import java.util.List;

@Service
public class ProductHandler {
    private ProductDAO productDAO;
    private final WebClient webclient;

    void log(String message) {
        System.out.println("[ProductHandler] " + message);
    }

    public ProductHandler(WebClient webclient) {
        productDAO = new ProductDAO();
        this.webclient = webclient;
    }

    public List<Product> getAllProducts() {
        log("Get all");
        return productDAO.getObjectList();
    }

    public Integer addProduct(Product product) {
        validateCategory(product.getCategory());

        Product newProduct = new Product(product);
        productDAO.saveObject(newProduct);
        log("Added Product " + product.getName() + " with id " + product.getId());
        return product.getId();
    }

    public Boolean deleteProduct(Integer id) {
        productDAO.deleteById(id);
        log("Deleted Product " + id);
        return true;
    }

    public Product getProductById(Integer id) {
        log("Retrieving Product " + id);
        return productDAO.getObjectById(id);
    }

    public Product getProductByName(String name) {
        log("Searching Product " + name);
        return productDAO.getObjectByName(name);
    }

    public List<Product> getProductsForSearchValues(String searchDescription,
                                                    Double searchMinPrice, Double searchMaxPrice) {
        return new ProductDAO().getProductListByCriteria(searchDescription, searchMinPrice, searchMaxPrice);
    }

    public void deleteProductByCategory(Integer categoryId) {
        log("Deleting all Products with category " + categoryId);
        List<Product> allProducts = getAllProducts();
        for (Product product : allProducts) {
            if (product.getCategory() == categoryId) {
                deleteProduct(product.getId());
                log("Deleting Product " + product.getId());
            }
        }
    }

    private void validateCategory(Integer categoryId) {
        try {
            webclient.get()
                .uri("/get-category/" + categoryId)
                .retrieve()
                .toBodilessEntity()
                .block();
            log("Category " + categoryId + " is valid");
        } catch (Exception e) {
            log("Category " + categoryId + " is invalid");
            System.out.println(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid Category ID" + categoryId
            );
        }
    }
}
