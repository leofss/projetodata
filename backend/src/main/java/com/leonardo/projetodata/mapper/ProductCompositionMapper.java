package com.leonardo.projetodata.mapper;

import com.leonardo.projetodata.dto.ProductCompositionCreateDto;
import com.leonardo.projetodata.dto.ProductCompositionResponseDto;
import com.leonardo.projetodata.model.ProductComposition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RawMaterialMapper.class})

public interface ProductCompositionMapper {
    @Mapping(target = "product", ignore = true)
    ProductComposition toEntity(ProductCompositionCreateDto dto);
    @Mapping(source = "rawMaterial", target = "rawMaterial")
    ProductCompositionResponseDto toDto(ProductComposition entity);
}
