package productservice.handler;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import productservice.model.Product;
import productservice.model.ProductDAO;

import java.util.List;

@Service
public class ProductHandler {
    private ProductDAO productDAO;
    private final WebClient webclient;

    public ProductHandler(WebClient webclient) {
        productDAO = new ProductDAO();
        this.webclient = webclient;
    }

    public List<Product> getAllProducts() {
        return productDAO.getObjectList();
    }

    public Integer addProduct(Product product) {
        // Todo: Validation Category
        if (validateCategory(product.getCategory())) {
            Product newProduct = new Product(product);
            productDAO.saveObject(newProduct);
            return product.getId();
        }
        return -1;
    }

    public Boolean deleteProduct(Integer id) {
        productDAO.deleteById(id);
        return true;
    }

    public Product getProductById(Integer id) {
        return productDAO.getObjectById(id);
    }

    public Product getProductByName(String name) {
        return productDAO.getObjectByName(name);
    }

    public List<Product> getProductsForSearchValues(String searchDescription,
                                                    Double searchMinPrice, Double searchMaxPrice) {
        return new ProductDAO().getProductListByCriteria(searchDescription, searchMinPrice, searchMaxPrice);
    }

    public Boolean deleteProductByCategory(Integer categoryId) {
        List<Product> allProducts = getAllProducts();
        for (Product product : allProducts) {
            if (product.getCategory() == categoryId) {
                deleteProduct(product.getId());
            }
        }

        return true;
    }

    private Boolean validateCategory(Integer categoryId) {
        try {
            return webclient.get()
                    .uri("/exists-category/" + categoryId)
                    .retrieve().bodyToMono(Boolean.class).block();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
