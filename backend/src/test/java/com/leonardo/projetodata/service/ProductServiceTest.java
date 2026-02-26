package com.leonardo.projetodata.service;

import com.leonardo.projetodata.dto.PageableDto;
import com.leonardo.projetodata.dto.ProductCompositionCreateDto;
import com.leonardo.projetodata.dto.ProductCreateDto;
import com.leonardo.projetodata.dto.ProductResponseDto;
import com.leonardo.projetodata.exception.DuplicateRawMaterialInProduct;
import com.leonardo.projetodata.exception.ProductCodeNotUnique;
import com.leonardo.projetodata.exception.ProductNotFound;
import com.leonardo.projetodata.mapper.PageableMapper;
import com.leonardo.projetodata.mapper.ProductCompositionMapper;
import com.leonardo.projetodata.mapper.ProductMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private RawMaterialService rawMaterialService;

    @Mock
    private ProductCompositionMapper productCompositionMapper;

    @Mock
    private PageableMapper pageableMapper;

    private UUID productUuid;
    private UUID rawMaterialUuid;

    private Product product;
    private ProductCreateDto createDto;
    private ProductResponseDto responseDto;
    private RawMaterial rawMaterial;

    @BeforeEach
    void setup() {
        productUuid = UUID.randomUUID();
        rawMaterialUuid = UUID.randomUUID();

        rawMaterial = new RawMaterial();
        rawMaterial.setUuid(rawMaterialUuid);

        ProductCompositionCreateDto compDto = new ProductCompositionCreateDto();
        compDto.setRawMaterialUuId(rawMaterialUuid);
        compDto.setQuantityRequired(BigDecimal.TEN);

        createDto = new ProductCreateDto();
        createDto.setName("Product A");
        createDto.setCode("CODE123");
        createDto.setPrice(BigDecimal.valueOf(100));
        createDto.setCompositions(List.of(compDto));

        product = new Product();
        product.setUuid(productUuid);
        product.setCompositions(new ArrayList<>());

        responseDto = new ProductResponseDto();
    }

    @Test
    void shouldCreateProductSuccessfully() {

        when(productRepository.existsByCode("CODE123")).thenReturn(false);
        when(productMapper.toEntity(createDto)).thenReturn(product);
        when(rawMaterialService.findByUuid(rawMaterialUuid)).thenReturn(rawMaterial);

        ProductComposition composition = new ProductComposition();
        when(productCompositionMapper.toEntity(any())).thenReturn(composition);

        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.create(createDto);

        assertNotNull(result);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowWhenCodeAlreadyExistsOnCreate() {

        when(productRepository.existsByCode("CODE123")).thenReturn(true);

        assertThrows(ProductCodeNotUnique.class,
                () -> productService.create(createDto));

        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDuplicateRawMaterialInCreate() {

        ProductCompositionCreateDto comp1 = new ProductCompositionCreateDto();
        comp1.setRawMaterialUuId(rawMaterialUuid);
        comp1.setQuantityRequired(BigDecimal.ONE);

        ProductCompositionCreateDto comp2 = new ProductCompositionCreateDto();
        comp2.setRawMaterialUuId(rawMaterialUuid);
        comp2.setQuantityRequired(BigDecimal.TEN);

        createDto.setCompositions(List.of(comp1, comp2));

        when(productRepository.existsByCode("CODE123")).thenReturn(false);

        assertThrows(DuplicateRawMaterialInProduct.class,
                () -> productService.create(createDto));
    }

    @Test
    void shouldUpdateProductSuccessfully() {

        when(productRepository.existsByCodeIgnoreCaseAndUuidNot("CODE123", productUuid))
                .thenReturn(false);

        when(productRepository.findById(productUuid))
                .thenReturn(Optional.of(product));

        when(rawMaterialService.findByUuid(rawMaterialUuid))
                .thenReturn(rawMaterial);

        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.update(createDto, productUuid);

        assertNotNull(result);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowWhenCodeAlreadyExistsOnUpdate() {

        when(productRepository.existsByCodeIgnoreCaseAndUuidNot("CODE123", productUuid))
                .thenReturn(true);

        assertThrows(ProductCodeNotUnique.class,
                () -> productService.update(createDto, productUuid));
    }

    @Test
    void shouldThrowWhenDuplicateRawMaterialOnUpdate() {

        ProductCompositionCreateDto comp1 = new ProductCompositionCreateDto();
        comp1.setRawMaterialUuId(rawMaterialUuid);
        comp1.setQuantityRequired(BigDecimal.ONE);

        ProductCompositionCreateDto comp2 = new ProductCompositionCreateDto();
        comp2.setRawMaterialUuId(rawMaterialUuid);

        createDto.setCompositions(List.of(comp1, comp2));

        when(productRepository.existsByCodeIgnoreCaseAndUuidNot("CODE123", productUuid))
                .thenReturn(false);

        assertThrows(DuplicateRawMaterialInProduct.class,
                () -> productService.update(createDto, productUuid));
    }

    @Test
    void shouldReturnPagedProducts() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(page);
        when(productMapper.toDto(product)).thenReturn(responseDto);
        when(pageableMapper.toDto(any())).thenReturn(new PageableDto());

        PageableDto result = productService.getAll(pageable);

        assertNotNull(result);
        verify(productRepository).findAll(pageable);
    }

    @Test
    void shouldThrowWhenProductNotFound() {

        when(productRepository.findById(productUuid))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFound.class,
                () -> productService.update(createDto, productUuid));
    }
}
