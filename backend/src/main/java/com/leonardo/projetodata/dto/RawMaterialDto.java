package com.leonardo.projetodata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leonardo.projetodata.enumerator.MeasurementType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class RawMaterialDto {
    private UUID uuid;
    @NotBlank
    @Pattern(
            regexp = "^[A-Z0-9]+$",
            message = "Code must contain only uppercase letters and numbers."    )
    private String code;
    @NotBlank
    private String name;
    @NotNull
    @Digits(integer = 17, fraction = 2)
    private BigDecimal quantity;
    @NotNull
    @JsonProperty("measurement_type")
    private MeasurementType measurementType;
}
