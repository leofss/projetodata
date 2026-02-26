package com.leonardo.projetodata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDto {
    private UUID uuid;
    @NotBlank
    private String name;
    @NotBlank
    private String code;
    @NotNull
    private BigDecimal price;
    @NotNull
    private List<ProductCompositionResponseDto> compositions;
}
