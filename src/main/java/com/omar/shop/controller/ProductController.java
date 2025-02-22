package com.omar.shop.controller;


import com.omar.shop.dto.ProductDto;
import com.omar.shop.exceptions.AlreadyExistException;
import com.omar.shop.exceptions.ResourceNotFoundException;
import com.omar.shop.model.Product;
import com.omar.shop.request.AddProductRequest;
import com.omar.shop.request.UpdateProductRequest;
import com.omar.shop.response.ApiResponse;
import com.omar.shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private static final String PRODUCT_RETRIEVED = "Products retrieved successfully";
    private static final String PRODUCT_NOT_FOUND = "Products not found";
    private final IProductService productService;

    @GetMapping("/all")
    @Transactional
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse(PRODUCT_RETRIEVED, convertedProducts));
        } catch (ResourceNotFoundException e) {
            //it has to be products in database so if there is problem,
            // it will be server side not failing get the products
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to retrieve products", INTERNAL_SERVER_ERROR));
        }
    }

    @Transactional
    @GetMapping("/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product theProduct = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse(PRODUCT_RETRIEVED, productDto));
        } catch (ResourceNotFoundException e) {

            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product newProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Product added successfully", newProduct));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductRequest product) {
        try {
            Product theProduct = productService.updateProduct(product, productId);
            return ResponseEntity.ok(new ApiResponse("Product updated successfully", theProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @GetMapping("/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> theProducts = productService.getProductsByBrandAndName(brand, name);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(PRODUCT_NOT_FOUND, null));
            }
            List<ProductDto> convertedProductsDtos = productService.getConvertedProducts(theProducts);
            return ResponseEntity.ok(new ApiResponse(PRODUCT_RETRIEVED, convertedProductsDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @GetMapping("/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> theProducts = productService.getProductsByCategoryAndBrand(category, brand);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(PRODUCT_NOT_FOUND, null));
            }
            List<ProductDto> convertedProductsDtos = productService.getConvertedProducts(theProducts);
            return ResponseEntity.ok(new ApiResponse(PRODUCT_RETRIEVED, convertedProductsDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @GetMapping("/by-name/products")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name) {
        try {
            List<Product> theProducts = productService.getProductsByName(name);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(PRODUCT_NOT_FOUND, null));
            }
            List<ProductDto> convertedProductsDtos = productService.getConvertedProducts(theProducts);
            return ResponseEntity.ok(new ApiResponse(PRODUCT_RETRIEVED, convertedProductsDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @GetMapping("/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        try {
            List<Product> theProducts = productService.getProductsByBrand(brand);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(PRODUCT_NOT_FOUND, null));
            }
            List<ProductDto> convertedProductsDtos = productService.getConvertedProducts(theProducts);
            return ResponseEntity.ok(new ApiResponse(PRODUCT_RETRIEVED, convertedProductsDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @GetMapping("/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> theProducts = productService.getProductsByCategory(category);
            if (theProducts.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(PRODUCT_NOT_FOUND, null));
            }
            List<ProductDto> convertedProductsDtos = productService.getConvertedProducts(theProducts);
            return ResponseEntity.ok(new ApiResponse(PRODUCT_RETRIEVED, convertedProductsDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @GetMapping("/count/by-brand-and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            Long productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Products count retrieved successfully", productCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/count/by-brand")
    public ResponseEntity<ApiResponse> countProductsByBrand(@RequestParam String brand) {
        try {
            Long productCount = productService.countProductsByBrand(brand);
            return ResponseEntity.ok(new ApiResponse("Products count retrieved successfully", productCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
