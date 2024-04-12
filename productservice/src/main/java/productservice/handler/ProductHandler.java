package productservice.handler;

import org.springframework.stereotype.Service;
import productservice.model.Product;

import java.util.List;

@Service
public class ProductHandler {

    public List<Product> getAllProducts() {
        // Todo: Implement
        return null;
    }

    public Boolean addProduct(Product product) {
        // Todo: Implement
        return true;
    }

    public Boolean deleteProduct(Integer id) {
        // Todo: Implement
        return true;
    }

    public Product getSpecificProduct(Integer id) {
        // Todo: Implement
        return new Product();
    }
}
