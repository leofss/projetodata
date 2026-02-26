package com.leonardo.projetodata.mapper;

import com.leonardo.projetodata.dto.PageableDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageableMapper {
    PageableDto toDto(Page<?> page);

}
