package productservice.handler;

import org.springframework.stereotype.Service;
import productservice.model.Product;
import productservice.model.ProductDAO;

import java.util.List;

@Service
public class ProductHandler {
    private ProductDAO productDAO;

    public ProductHandler() {
        productDAO = new ProductDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.getObjectList();
    }

    public Integer addProduct(Product product) {
        Product newProduct = new Product(product);
        productDAO.saveObject(newProduct);
        return product.getId();
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
}
