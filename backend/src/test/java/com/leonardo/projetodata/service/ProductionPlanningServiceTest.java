package com.leonardo.projetodata.service;

import com.leonardo.projetodata.dto.ProductionSuggestionDto;
import com.leonardo.projetodata.model.Product;
import com.leonardo.projetodata.model.ProductComposition;
import com.leonardo.projetodata.model.RawMaterial;
import com.leonardo.projetodata.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductionPlanningServiceTest {
    @InjectMocks
    private ProductionPlanningService productionPlanningService;

    @Mock
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;

    private RawMaterial rm1;
    private RawMaterial rm2;

    @BeforeEach
    void setup() {
        rm1 = new RawMaterial();
        rm1.setUuid(UUID.randomUUID());
        rm1.setQuantityInStock(BigDecimal.valueOf(100));

        rm2 = new RawMaterial();
        rm2.setUuid(UUID.randomUUID());
        rm2.setQuantityInStock(BigDecimal.valueOf(50));

        ProductComposition comp1 = new ProductComposition();
        comp1.setRawMaterial(rm1);
        comp1.setQuantityRequired(BigDecimal.valueOf(10));

        product1 = new Product();
        product1.setUuid(UUID.randomUUID());
        product1.setName("Product 1");
        product1.setPrice(BigDecimal.valueOf(20));
        product1.setCompositions(List.of(comp1));

        ProductComposition comp2 = new ProductComposition();
        comp2.setRawMaterial(rm2);
        comp2.setQuantityRequired(BigDecimal.valueOf(25));

        product2 = new Product();
        product2.setUuid(UUID.randomUUID());
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(15));
        product2.setCompositions(List.of(comp2));
    }

    @Test
    void shouldSuggestProductionCorrectlyAndSortByRevenue() {

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<ProductionSuggestionDto> result = productionPlanningService.suggestProduction();

        assertEquals(2, result.size());

        ProductionSuggestionDto first = result.get(0);
        ProductionSuggestionDto second = result.get(1);

        assertEquals(product1.getUuid(), first.getProductUuid());
        assertEquals(BigDecimal.valueOf(10), first.getMaxProductionQuantity());
        assertEquals(BigDecimal.valueOf(200), first.getTotalRevenue());

        assertEquals(product2.getUuid(), second.getProductUuid());
        assertEquals(BigDecimal.valueOf(2), second.getMaxProductionQuantity());
        assertEquals(BigDecimal.valueOf(30), second.getTotalRevenue());
    }

    @Test
    void shouldFilterOutProductsWithZeroMaxProduction() {

        RawMaterial emptyStock = new RawMaterial();
        emptyStock.setUuid(UUID.randomUUID());
        emptyStock.setQuantityInStock(BigDecimal.ZERO);

        ProductComposition comp = new ProductComposition();
        comp.setRawMaterial(emptyStock);
        comp.setQuantityRequired(BigDecimal.valueOf(5));

        Product zeroProduct = new Product();
        zeroProduct.setUuid(UUID.randomUUID());
        zeroProduct.setName("Zero Product");
        zeroProduct.setPrice(BigDecimal.valueOf(100));
        zeroProduct.setCompositions(List.of(comp));

        when(productRepository.findAll()).thenReturn(List.of(product1, zeroProduct));

        List<ProductionSuggestionDto> result = productionPlanningService.suggestProduction();

        assertEquals(1, result.size());
        assertEquals(product1.getUuid(), result.get(0).getProductUuid());
    }

    @Test
    void shouldCalculateMaxProductionBasedOnLowestStock() {

        RawMaterial rmLow = new RawMaterial();
        rmLow.setUuid(UUID.randomUUID());
        rmLow.setQuantityInStock(BigDecimal.valueOf(7));

        ProductComposition compA = new ProductComposition();
        compA.setRawMaterial(rm1);
        compA.setQuantityRequired(BigDecimal.valueOf(10));

        ProductComposition compB = new ProductComposition();
        compB.setRawMaterial(rmLow);
        compB.setQuantityRequired(BigDecimal.valueOf(3));

        Product multiProduct = new Product();
        multiProduct.setUuid(UUID.randomUUID());
        multiProduct.setName("Multi Product");
        multiProduct.setPrice(BigDecimal.valueOf(5));
        multiProduct.setCompositions(List.of(compA, compB));

        when(productRepository.findAll()).thenReturn(List.of(multiProduct));

        List<ProductionSuggestionDto> result = productionPlanningService.suggestProduction();

        ProductionSuggestionDto dto = result.get(0);

        assertEquals(BigDecimal.valueOf(2), dto.getMaxProductionQuantity());
        assertEquals(BigDecimal.valueOf(10), dto.getTotalRevenue());
    }
}
