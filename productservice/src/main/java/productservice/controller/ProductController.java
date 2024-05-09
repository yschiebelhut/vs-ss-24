package productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productservice.handler.ProductHandler;
import productservice.model.Product;

import jakarta.validation.Valid;

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
    public ResponseEntity<Integer> addProduct(@Valid @RequestBody Product product) {
        return new ResponseEntity<>(productHandler.addProduct(product), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete-product/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteProduct(@PathVariable Integer id) {
        productHandler.getProductById(id);
        productHandler.deleteProduct(id);
    }

    @DeleteMapping(value = "delete-product-by-category/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteProductByCategory(@PathVariable Integer id) {
        productHandler.deleteProductByCategory(id);
    }
}
