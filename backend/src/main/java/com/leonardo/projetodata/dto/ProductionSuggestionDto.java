package com.leonardo.projetodata.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductionSuggestionDto {
    @JsonProperty("product_uuid")
    private UUID productUuid;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("unit_price")
    private BigDecimal unitPrice;
    @JsonProperty("max_production_quantity")
    private BigDecimal maxProductionQuantity;
    @JsonProperty("total_revenue")
    private BigDecimal totalRevenue;
}