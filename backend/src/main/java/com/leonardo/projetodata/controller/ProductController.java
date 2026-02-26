package com.leonardo.projetodata.controller;

import com.leonardo.projetodata.dto.PageableDto;
import com.leonardo.projetodata.dto.ProductCreateDto;
import com.leonardo.projetodata.dto.ProductResponseDto;
import com.leonardo.projetodata.dto.RawMaterialDto;
import com.leonardo.projetodata.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductCreateDto productCreateDto){
        ProductResponseDto productResponseDto = productService.create(productCreateDto);
        return ResponseEntity.status(201).body(productResponseDto);
    }

    @GetMapping
    public ResponseEntity<PageableDto> getAll(Pageable pageable){
        PageableDto productResponsePage = productService.getAll(pageable);
        return ResponseEntity.ok(productResponsePage);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuid){
        productService.delete(uuid);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable UUID uuid,
                                                 @RequestBody @Valid ProductCreateDto productCreateDto){
        ProductResponseDto productResponseDto = productService.update(productCreateDto, uuid);
        return ResponseEntity.ok(productResponseDto);
    }
}
