package com.leonardo.projetodata.mapper;

import com.leonardo.projetodata.dto.RawMaterialDto;
import com.leonardo.projetodata.model.RawMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RawMaterialMapper {
    @Mapping(source = "quantity", target = "quantityInStock")
    RawMaterial toEntity(RawMaterialDto dto);

    @Mapping(source = "quantityInStock", target = "quantity")
    RawMaterialDto toDto(RawMaterial entity);

    @Mapping(source = "quantity", target = "quantityInStock")
    void updateEntityFromDto(RawMaterialDto rawMaterialDto, @MappingTarget RawMaterial rawMaterial);
}
