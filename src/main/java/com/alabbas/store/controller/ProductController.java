package com.alabbas.store.controller;

import com.alabbas.store.dto.ApiResponse;
import com.alabbas.store.dto.AddStockRequest;
import com.alabbas.store.dto.BulkAddStockRequest;
import com.alabbas.store.dto.ProductRequest;
import com.alabbas.store.dto.ProductResponse;
import com.alabbas.store.dto.ReceivedProductsResponse;
import com.alabbas.store.dto.ReceivedProductsSummaryResponse;
import com.alabbas.store.dto.UploadedImageResponse;
import com.alabbas.store.entity.Product;
import com.alabbas.store.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.multipart.MultipartFile;
import java.util.Locale;
import java.util.List;

@RestController
@RequestMapping
public class ProductController {

    private final ProductService productService;
    private final MessageSource messageSource;


    public ProductController(ProductService productService ,  MessageSource messageSource) {
        this.productService = productService;
        this.messageSource = messageSource;
    }

    @PostMapping("/api/private/products")
    public ApiResponse createProduct(@Valid @RequestBody ProductRequest request) {
         productService.createProduct(request);
        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "product.created.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(201)
                .message(message)
                .build();

    }

    @PostMapping(value = "/api/private/products/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadedImageResponse createProductWithImage(@Valid @RequestPart("product") ProductRequest request,
                                                        @RequestPart(value = "image", required = false) MultipartFile image) {
        Product product = productService.createProduct(request, image);
        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "product.created.success",
                null,
                locale
        );

        return UploadedImageResponse.builder()
                .status(201)
                .message(message)
                .uploadedImageUrl(product.getImageUrl())
                .build();
    }

    @GetMapping("/api/public/products")
    public Page<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size ,Sort.by(Sort.Direction.DESC, "createdAt"));
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/api/public/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/api/private/products/received-products")
    public Page<ReceivedProductsSummaryResponse> getReceivedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getReceivedProducts(pageable);
    }

    @GetMapping("/api/private/products/received-products/{transactionId}")
    public List<ReceivedProductsResponse> getReceivedProductsByTransactionId(@PathVariable String transactionId) {
        return productService.getReceivedProductsByTransactionId(transactionId);
    }


    @DeleteMapping("/api/private/products/{id}")
    public ApiResponse deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "product.deleted.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();
    }

    @PutMapping("/api/private/products/{id}")
    public ApiResponse updateProduct(@PathVariable Long id,
                                     @Valid @RequestBody ProductRequest request) {

        productService.updateProduct(id, request);

        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "product.updated.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();
    }

    @PutMapping(value = "/api/private/products/{id}/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadedImageResponse updateProductWithImage(@PathVariable Long id,
                                                        @Valid @RequestPart("product") ProductRequest request,
                                                        @RequestPart(value = "image", required = false) MultipartFile image) {

        Product product = productService.updateProduct(id, request, image);

        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "product.updated.success",
                null,
                locale
        );

        return UploadedImageResponse.builder()
                .status(200)
                .message(message)
                .uploadedImageUrl(product.getImageUrl())
                .build();
    }

    @PatchMapping("/api/private/products/{id}/stock/add")
    public ApiResponse addStock(@PathVariable Long id,
                                @Valid @RequestBody AddStockRequest request) {

        productService.addStock(id, request);

        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "product.stock.added.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();
    }

    @PatchMapping("/api/private/products/stock/add")
    public ApiResponse addStock(@Valid @RequestBody BulkAddStockRequest request) {

        productService.addStock(request);

        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "product.stock.bulk.added.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();
    }

    @GetMapping("/api/public/products/category/{categoryId}")
    public Page<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size)  {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getProductsByCategory(categoryId ,pageable);

    }


    @GetMapping("/api/public/products/search")
    public Page<ProductResponse> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.searchProducts(q, pageable);
    }

    @GetMapping("/api/public/products/search/category")
    public Page<ProductResponse> searchProductsInCategory(
            @RequestParam(required = false) String q,
            @RequestParam Long categoryId,
            @RequestParam(required = false) Integer stockQuantity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.searchProductsInCategory(q,categoryId,stockQuantity,pageable);
    }
}
