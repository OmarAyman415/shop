package com.omar.shop.controller;


import com.omar.shop.exceptions.ResourceNotFoundException;
import com.omar.shop.model.Product;
import com.omar.shop.request.AddProductRequest;
import com.omar.shop.request.UpdateProductRequest;
import com.omar.shop.response.ApiResponse;
import com.omar.shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", products));
        } catch (ResourceNotFoundException e) {
            //it has to be products in database so if there is problem,
            // it will be server side not failing get the products
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to retrieve products", INTERNAL_SERVER_ERROR));
        }
    }


    /**
     * Returns a product by the given product id.
     *
     * @param productId the product id
     * @return a response entity containing the product
     * @throws ResourceNotFoundException if the product does not exist
     */
    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product theProduct = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product retrieved successfully", theProduct));
        } catch (ResourceNotFoundException e) {

            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Adds a new product with the given information.
     *
     * @param product the product information
     * @return a response entity containing the newly added product
     * @throws ResourceNotFoundException if the product could be added
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product newProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Product added successfully", newProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductRequest product) {
        try {
            Product theProduct = productService.updateProduct(product, productId);
            return ResponseEntity.ok(new ApiResponse("Product updated successfully", theProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/brand-adn-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> theProducts = productService.getProductsByBrandAndName(brand, name);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", theProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> theProducts = productService.getProductsByCategoryAndBrand(category, brand);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", theProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> theProducts = productService.getProductsByName(name);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", theProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        try {
            List<Product> theProducts = productService.getProductsByBrand(brand);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", theProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> theProducts = productService.getProductsByCategory(category);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", theProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            Long productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Products count retrieved successfully", productCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
