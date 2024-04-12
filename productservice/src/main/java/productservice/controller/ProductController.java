package productservice.controller;

import jakarta.websocket.server.PathParam;
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
        return new ResponseEntity<>(productHandler.getSpecificProduct(id), HttpStatus.OK);
    }

    @PostMapping(value = "add-product")
    public ResponseEntity<Boolean> addProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productHandler.addProduct(product), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete-product/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Integer id) {
        return new ResponseEntity<>(productHandler.deleteProduct(id), HttpStatus.OK);
    }
}
