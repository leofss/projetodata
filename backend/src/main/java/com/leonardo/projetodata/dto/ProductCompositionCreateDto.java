package com.leonardo.projetodata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCompositionCreateDto {
    @NotNull
    @JsonProperty("raw_material_uuid")
    private UUID rawMaterialUuId;
    @NotNull
    @Positive(message = "quantity required must be greater than zero")
    @JsonProperty("quantity_required")
    private BigDecimal quantityRequired;
}
