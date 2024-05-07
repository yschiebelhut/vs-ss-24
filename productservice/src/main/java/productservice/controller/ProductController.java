package productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productservice.handler.ProductHandler;
import productservice.model.Product;

import java.util.List;

@RestController
@RequestMapping(value = "/product/")
public class ProductController {

    @Autowired
    ProductHandler productHandler;

    @GetMapping(value = "get-products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productHandler.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping(value = "get-product/{id}")
    public ResponseEntity<Product> getSpecificProduct(@PathVariable Integer id) {
        return new ResponseEntity<>(productHandler.getProductById(id), HttpStatus.OK);
    }

    @GetMapping(value = "get-product-by-name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        return new ResponseEntity<>(productHandler.getProductByName(name), HttpStatus.OK);
    }

    @GetMapping(value = "get-product-by-search/{desc}/{minPrice}/{maxPrice}")
    public ResponseEntity<List<Product>> getProductBySearch(@PathVariable String desc, @PathVariable Double minPrice, @PathVariable Double maxPrice) {
        return new ResponseEntity<>(productHandler.getProductsForSearchValues(desc, minPrice, maxPrice), HttpStatus.OK);
    }

    @PostMapping(value = "add-product")
    public ResponseEntity<Integer> addProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productHandler.addProduct(product), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete-product/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Integer id) {
        return new ResponseEntity<>(productHandler.deleteProduct(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete-product-by-category/{id}")
    public ResponseEntity<Boolean> deleteProductByCategory(@PathVariable Integer id) {
        return new ResponseEntity<>(productHandler.deleteProductByCategory(id), HttpStatus.OK);
    }
}
