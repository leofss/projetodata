package com.leonardo.projetodata.repository;

import com.leonardo.projetodata.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByCode(String code);
    boolean existsByCodeIgnoreCaseAndUuidNot(String code, UUID uuid);

}
