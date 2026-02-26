package com.leonardo.projetodata.repository;

import com.leonardo.projetodata.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, UUID> {
    boolean existsByCode(String code);
    boolean existsByCodeIgnoreCaseAndUuidNot(String code, UUID uuid);
}
