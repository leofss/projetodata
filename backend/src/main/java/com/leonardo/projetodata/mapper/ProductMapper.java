package com.leonardo.projetodata.mapper;

import com.leonardo.projetodata.dto.ProductCompositionResponseDto;
import com.leonardo.projetodata.dto.ProductCreateDto;
import com.leonardo.projetodata.dto.ProductResponseDto;
import com.leonardo.projetodata.dto.RawMaterialDto;
import com.leonardo.projetodata.model.Product;
import com.leonardo.projetodata.model.ProductComposition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {RawMaterialMapper.class, ProductCompositionMapper.class})
public interface ProductMapper {
        @Mapping(target = "compositions", ignore = true)
        Product toEntity(ProductCreateDto dto);

        ProductResponseDto toDto(Product entity);
}
