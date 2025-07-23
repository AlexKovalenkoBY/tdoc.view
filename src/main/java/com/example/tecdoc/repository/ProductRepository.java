package com.example.tecdoc.repository;

import com.example.tecdoc.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.brand = :brand AND p.article = :article")
    List<Product> findByBrandAndArticle(@Param("brand") String brand, @Param("article") String article);
    
    List<Product> findByBrandContainingIgnoreCaseOrArticleContainingIgnoreCase(String brand, String article);
}