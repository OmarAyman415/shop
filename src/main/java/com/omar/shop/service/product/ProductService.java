package com.omar.shop.service.product;

import com.omar.shop.dto.ImageDto;
import com.omar.shop.dto.ProductDto;
import com.omar.shop.exceptions.AlreadyExistException;
import com.omar.shop.exceptions.ResourceNotFoundException;
import com.omar.shop.model.Category;
import com.omar.shop.model.Image;
import com.omar.shop.model.Product;
import com.omar.shop.repository.CategoryRepository;
import com.omar.shop.repository.ImageRepository;
import com.omar.shop.repository.ProductRepository;
import com.omar.shop.request.AddProductRequest;
import com.omar.shop.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    public static final String PRODUCT_NOT_FOUND = "Product not found";

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category
        );
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        if (Boolean.TRUE
                .equals(isProductExist(request.getName(), request.getBrand()))
        ) {
            throw new AlreadyExistException("Product with name: " + request.getName() + " already exists");
        }
        /* Check if the category exists in the database.
         * If yes, set it as new product category.
         * If no, save it as new category and set product as new product category. */
        Category productCategory = findOrCreateCategory(request);

        request.setCategory(productCategory);

        Product newProduct = createProduct(request, productCategory);
        return productRepository.save(newProduct);
    }

    private Boolean isProductExist(String productName, String brand) {
        return productRepository.existsByNameAndBrand(productName, brand);
    }

    private Category findOrCreateCategory(AddProductRequest request) {
        return Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
    }


    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, () -> {
                    throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
                });
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(desiredProduct -> updateExistingProduct(desiredProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrand(String brand) {
        return productRepository.countByBrand(brand);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
