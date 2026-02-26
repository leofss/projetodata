package com.leonardo.projetodata.service;

import com.leonardo.projetodata.dto.*;
import com.leonardo.projetodata.enumerator.ErrorMessage;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RawMaterialService rawMaterialService;
    private final ProductCompositionMapper productCompositionMapper;
    private final PageableMapper pageableMapper;

    @Transactional
    public ProductResponseDto create(ProductCreateDto productCreateDto) {
        validateUniqueCodeForCreate(productCreateDto.getCode());
        validateUniqueRawMaterials(productCreateDto.getCompositions());
        Product product = productMapper.toEntity(productCreateDto);
        product.setName(productCreateDto.getName());
        product.setCode(productCreateDto.getCode());
        product.setPrice(productCreateDto.getPrice());

        product.setCompositions(buildCompositions(product, productCreateDto.getCompositions()));

        Product productCreated = productRepository.save(product);
        return productMapper.toDto(productCreated);
    }

    public PageableDto getAll(Pageable pageable){
        Page<Product> rawMaterialPage = productRepository.findAll(pageable);
        return pageableMapper.toDto(rawMaterialPage.map(productMapper::toDto));
    }

    @Transactional
    public ProductResponseDto update(ProductCreateDto productCreateDto, UUID uuid) {
        validateUniqueCodeForUpdate(productCreateDto.getCode(), uuid);
        validateUniqueRawMaterials(productCreateDto.getCompositions());
        Product product = findByUuid(uuid);

        product.setName(productCreateDto.getName());
        product.setCode(productCreateDto.getCode());
        product.setPrice(productCreateDto.getPrice());


        updateCompositions(product, productCreateDto.getCompositions());

        Product updated = productRepository.save(product);

        return productMapper.toDto(updated);
    }

    public void delete(UUID uuid){
        Product product = findByUuid(uuid);
        productRepository.delete(product);
    }


    private void updateCompositions(Product product, List<ProductCompositionCreateDto> compositionDtos) {
        Map<UUID, ProductComposition> existing = product.getCompositions().stream()
                .collect(Collectors.toMap(c -> c.getRawMaterial().getUuid(), Function.identity()));

        for (ProductCompositionCreateDto dto : compositionDtos) {
            RawMaterial rawMaterial = rawMaterialService.findByUuid(dto.getRawMaterialUuId());

            if (existing.containsKey(rawMaterial.getUuid())) {
                ProductComposition comp = existing.get(rawMaterial.getUuid());
                comp.setQuantityRequired(dto.getQuantityRequired());
                existing.remove(rawMaterial.getUuid());
            } else {
                ProductComposition comp = new ProductComposition();
                comp.setProduct(product);
                comp.setRawMaterial(rawMaterial);
                comp.setQuantityRequired(dto.getQuantityRequired());

                product.getCompositions().add(comp);
            }
        }

        for (ProductComposition toRemove : existing.values()) {
            product.getCompositions().remove(toRemove);
        }
    }

    private Product findByUuid(UUID uuid){
        return productRepository.findById(uuid).orElseThrow(() -> new ProductNotFound(ErrorMessage.PRODUCT_NOT_FOUND));
    }

    private List<ProductComposition> buildCompositions(Product product,
                                                       List<ProductCompositionCreateDto> compositionDto) {
        return compositionDto.stream().map(dto -> {
            RawMaterial rawMaterial = rawMaterialService.findByUuid(dto.getRawMaterialUuId());

            ProductComposition composition = productCompositionMapper.toEntity(dto);
            composition.setProduct(product);
            composition.setRawMaterial(rawMaterial);
            composition.setQuantityRequired(dto.getQuantityRequired());

            return composition;
        }).toList();
    }

    private void validateUniqueRawMaterials(List<ProductCompositionCreateDto> compositions) {

        Set<UUID> rawMaterialIds = new HashSet<>();

        for (ProductCompositionCreateDto comp : compositions) {
            if (!rawMaterialIds.add(comp.getRawMaterialUuId())) {
                throw new DuplicateRawMaterialInProduct(ErrorMessage.DUPLICATE_RAW_MATERIAL_IN_PRODUCT);
            }
        }
    }

    private void validateUniqueCodeForUpdate(String code, UUID uuid){
        if (productRepository.existsByCodeIgnoreCaseAndUuidNot(code, uuid)) {
            throw new ProductCodeNotUnique(ErrorMessage.PRODUCT_CODE_ALREADY_EXISTS, code);
        }
    }
    private void validateUniqueCodeForCreate(String code){
        if (productRepository.existsByCode(code)) {
            throw new ProductCodeNotUnique(ErrorMessage.PRODUCT_CODE_ALREADY_EXISTS, code);
        }
    }
}
