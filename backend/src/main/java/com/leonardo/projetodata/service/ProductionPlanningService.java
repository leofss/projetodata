package com.leonardo.projetodata.service;

import com.leonardo.projetodata.dto.ProductionSuggestionDto;
import com.leonardo.projetodata.model.Product;
import com.leonardo.projetodata.model.ProductComposition;
import com.leonardo.projetodata.model.RawMaterial;
import com.leonardo.projetodata.repository.ProductRepository;
import com.leonardo.projetodata.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionPlanningService {
    private final ProductRepository productRepository;

    public List<ProductionSuggestionDto> suggestProduction() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(this::calculateProduction)
                .filter(dto -> dto.getMaxProductionQuantity().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(ProductionSuggestionDto::getTotalRevenue).reversed())
                .toList();
    }

    private ProductionSuggestionDto calculateProduction(Product product) {
        BigDecimal maxProduction = product.getCompositions().stream()
                .map(composition -> {
                    BigDecimal availableStock =
                            composition.getRawMaterial().getQuantityInStock();

                    BigDecimal required =
                            composition.getQuantityRequired();

                    return availableStock
                            .divide(required, 0, RoundingMode.DOWN);
                })
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal totalRevenue = product.getPrice()
                .multiply(maxProduction);

        return new ProductionSuggestionDto(
                product.getUuid(),
                product.getName(),
                product.getPrice(),
                maxProduction,
                totalRevenue
        );
    }
}
