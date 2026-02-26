package com.leonardo.projetodata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class ProductCreateDto {
    private UUID uuid;
    @NotBlank
    private String name;
    @NotBlank
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Code must contain only uppercase letters and numbers.")
    private String code;
    @NotNull
    @Positive(message = "price must be greater than zero")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal price;
    @NotNull
    @Valid
    private List<ProductCompositionCreateDto> compositions;
}
