package com.leonardo.projetodata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCompositionResponseDto {
    @NotNull
    @JsonProperty("quantity_required")
    private BigDecimal quantityRequired;
    @NotNull
    @JsonProperty("raw_material")
    private RawMaterialDto rawMaterial;
}
