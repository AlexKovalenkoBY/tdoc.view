package com.example.tecdoc.service;

import com.example.tecdoc.model.Product;
import com.example.tecdoc.model.User;
import com.example.tecdoc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.tecdoc.model.AuditLog;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AuditService auditService;
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    public List<Product> findByBrandAndArticle(String brand, String article) {
        return productRepository.findByBrandAndArticle(brand, article);
    }
    
    public List<Product> searchProducts(String query) {
        return productRepository.findByBrandContainingIgnoreCaseOrArticleContainingIgnoreCase(query, query);
    }
    
    @Transactional
    public Product saveProduct(Product product) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        product.setCreatedBy(currentUser);
        product.setUpdatedBy(currentUser);
        
        Product savedProduct = productRepository.save(product);
        auditService.logProductAction(AuditLog.ActionType.CREATE, savedProduct);
        return savedProduct;
    }
    
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return productRepository.findById(id)
                .map(product -> {
                    Product oldProduct = new Product();
                    oldProduct.setBrand(product.getBrand());
                    oldProduct.setArticle(product.getArticle());
                    oldProduct.setName(product.getName());
                    oldProduct.setDescription(product.getDescription());
                    oldProduct.setPrice(product.getPrice());
                    oldProduct.setQuantity(product.getQuantity());
                    
                    product.setBrand(productDetails.getBrand());
                    product.setArticle(productDetails.getArticle());
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setPrice(productDetails.getPrice());
                    product.setQuantity(productDetails.getQuantity());
                    product.setUpdatedBy(currentUser);
                    
                    Product updatedProduct = productRepository.save(product);
                    auditService.logProductUpdate(oldProduct, updatedProduct);
                    return updatedProduct;
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            auditService.logProductAction(AuditLog.ActionType.DELETE, product);
            productRepository.delete(product);
        });
    }
}