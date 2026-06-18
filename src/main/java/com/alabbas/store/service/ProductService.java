package com.alabbas.store.service;

import com.alabbas.store.dto.AddStockRequest;
import com.alabbas.store.dto.BulkAddStockRequest;
import com.alabbas.store.dto.ProductRequest;
import com.alabbas.store.dto.ProductResponse;
import com.alabbas.store.dto.ReceivedProductsResponse;
import com.alabbas.store.dto.ReceivedProductsSummaryResponse;
import com.alabbas.store.dto.StockItemRequest;
import com.alabbas.store.entity.Category;
import com.alabbas.store.entity.Product;
import com.alabbas.store.entity.ReceivedProduct;
import com.alabbas.store.exception.BusinessException;
import com.alabbas.store.exception.ResourceNotFoundException;
import com.alabbas.store.repository.CategoryRepository;
import com.alabbas.store.repository.ProductRepository;
import com.alabbas.store.repository.ReceivedProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReceivedProductRepository receivedProductRepository;

    @Value("${app.upload.images-path}")
    private String imagesPath;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ReceivedProductRepository receivedProductRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.receivedProductRepository = receivedProductRepository;
    }

    public Product createProduct(ProductRequest  request) {

        // 🔥 SKU duplicate validation
        if (productRepository.existsBySkuIgnoreCase(request.getSku())) {
            throw new BusinessException("product.sku.exists");
        }

        // 🔥 get category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category.not.found"));

        // 🔥 build product
        Product product = Product.builder()
                .sku(request.getSku())
                .nameEn(request.getNameEn())
                .nameAr(request.getNameAr())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .storePrice(request.getStorePrice())
                .stockQuantity(request.getStockQuantity())
                .published(request.getPublished())
                .category(category)
                .companyEn(request.getCompanyEn())
                .companyAr(request.getCompanyAr())
                .build();

        return productRepository.save(product);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToProductResponse);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product.not.found"));
    }

    public void deleteProduct(@PathVariable Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);

    }
    @Transactional(readOnly = true)
    public Page<ReceivedProductsSummaryResponse> getReceivedProducts(Pageable pageable) {
        return receivedProductRepository.findReceivedProductTransactions(pageable);
    }

    @Transactional(readOnly = true)
    public List<ReceivedProductsResponse> getReceivedProductsByTransactionId(String transactionId) {
        List<ReceivedProduct> receivedProducts = receivedProductRepository.findByTransactionIdOrderByCreatedAtDesc(transactionId);

        if (receivedProducts.isEmpty()) {
            throw new ResourceNotFoundException("received.products.not.found");
        }

        return receivedProducts.stream()
                .map(this::mapToReceivedProductsResponse)
                .toList();
    }
    public Product updateProduct(Long id, ProductRequest request) {

        Product existingProduct = getProductById(id);

        if (!existingProduct.getSku().equalsIgnoreCase(request.getSku())
                && productRepository.existsBySkuIgnoreCase(request.getSku())) {
            throw new BusinessException("product.sku.exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category.not.found"));

        existingProduct.setSku(request.getSku());
        existingProduct.setNameEn(request.getNameEn());
        existingProduct.setNameAr(request.getNameAr());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStorePrice(request.getStorePrice());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setStockQuantity(request.getStockQuantity());
        existingProduct.setPublished(request.getPublished());
        existingProduct.setCategory(category);

        return productRepository.save(existingProduct);
    }

    public Product updateProduct(Long id, ProductRequest request, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            request.setImageUrl(saveProductImage(image));
        }

        return updateProduct(id, request);
    }

    @Transactional
    public Product addStock(Long id, AddStockRequest request) {
        Product product = getProductById(id);
        product.setStockQuantity(product.getStockQuantity() + request.getQuantity());
        Product savedProduct = productRepository.save(product);

        String transactionId = UUID.randomUUID().toString();
        saveReceivedProduct(savedProduct, request.getQuantity(), request.getAmount(), transactionId);

        return savedProduct;
    }

    public Product createProduct(ProductRequest request, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            request.setImageUrl(saveProductImage(image));
        }

        return createProduct(request);
    }

    @Transactional
    public void addStock(BulkAddStockRequest request) {
        String transactionId = UUID.randomUUID().toString();

        for (StockItemRequest item : request.getItems()) {
            Product product = getProductById(item.getProductId());
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            saveReceivedProduct(product, item.getQuantity(), item.getAmount(), transactionId);
        }
    }

    public Page<ProductResponse> getProductsByCategory(Long categoryId , Pageable pageable) {
        return productRepository.findByCategoryId(categoryId , pageable)
                .map(this::mapToProductResponse);

    }


    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchProducts(keyword, pageable)
                .map(this::mapToProductResponse);
    }

    public Page<ProductResponse> searchProductsInCategory(String keyword, Long categoryId, Pageable pageable) {
        return productRepository
                .searchProductsInCategory(keyword, categoryId, pageable)
                .map(this::mapToProductResponse);

    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .nameEn(product.getNameEn())
                .nameAr(product.getNameAr())
                .description(product.getDescription())
                .price(product.getPrice())
                .storePrice(product.getStorePrice())
                .imageUrl(product.getImageUrl())
                .uploadedImageUrl(product.getImageUrl())
                .stockQuantity(product.getStockQuantity())
                .published(product.getPublished())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getNameEn())
                .companyEn(product.getCompanyEn())
                .companyAr(product.getCompanyAr())
                .build();
    }

    private ReceivedProductsResponse mapToReceivedProductsResponse(ReceivedProduct receivedProduct) {
        Product product = receivedProduct.getProduct();

        return ReceivedProductsResponse.builder()
                .id(receivedProduct.getId())
                .transactionId(receivedProduct.getTransactionId())
                .amount(receivedProduct.getAmount())
                .quantity(receivedProduct.getQuantity())
                .nameEn(product.getNameEn())
                .nameAr(product.getNameAr())
                .createdAt(receivedProduct.getCreatedAt())
                .build();
    }

    private String saveProductImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new BusinessException("product.image.required");
        }

        String originalFilename = image.getOriginalFilename();
        String extension = getExtension(originalFilename);
        String filename = UUID.randomUUID() + extension;
        Path uploadDir = Paths.get(imagesPath).toAbsolutePath().normalize();
        Path targetPath = uploadDir.resolve(filename).normalize();

        if (!targetPath.startsWith(uploadDir)) {
            throw new BusinessException("product.image.invalid");
        }

        try {
            Files.createDirectories(uploadDir);
            image.transferTo(targetPath);
            return "/images/" + filename;
        } catch (IOException ex) {
            throw new BusinessException("product.image.upload.failed");
        }
    }

    private String getExtension(String filename) {
        if (filename == null) {
            return "";
        }

        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            return "";
        }

        return filename.substring(dotIndex);
    }

    private void saveReceivedProduct(Product product, Integer quantity, BigDecimal amount, String transactionId) {
        ReceivedProduct receivedProduct = ReceivedProduct.builder()
                .transactionId(transactionId)
                .product(product)
                .quantity(quantity)
                .amount(amount)
                .build();

        receivedProductRepository.save(receivedProduct);
    }
}
