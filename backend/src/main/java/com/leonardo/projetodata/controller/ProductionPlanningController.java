package com.leonardo.projetodata.controller;

import com.leonardo.projetodata.dto.ProductionSuggestionDto;
import com.leonardo.projetodata.service.ProductionPlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/production-planning")
@RequiredArgsConstructor
public class ProductionPlanningController {
    private final ProductionPlanningService productionPlanningService;

    @GetMapping("/suggestions")
    public ResponseEntity<List<ProductionSuggestionDto>> getSuggestions(){
        List<ProductionSuggestionDto> productionSuggestionDtoList = productionPlanningService.suggestProduction();
        return ResponseEntity.ok(productionSuggestionDtoList);
    }

}
