package com.leonardo.projetodata.controller;

import com.leonardo.projetodata.dto.PageableDto;
import com.leonardo.projetodata.dto.RawMaterialDto;
import com.leonardo.projetodata.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/raw-materials")
@RequiredArgsConstructor
public class RawMaterialController {
    private final RawMaterialService rawMaterialService;

    @PostMapping
    public ResponseEntity<RawMaterialDto> create(@RequestBody @Valid RawMaterialDto rawMaterialDto){
        RawMaterialDto rawMaterialCreatedDto = rawMaterialService.create(rawMaterialDto);
        return ResponseEntity.status(201).body(rawMaterialCreatedDto);
    }

    @GetMapping
    public ResponseEntity<PageableDto> getAll(Pageable pageable){
        PageableDto rawMaterialsResponsePage = rawMaterialService.getAll(pageable);
        return ResponseEntity.ok(rawMaterialsResponsePage);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<RawMaterialDto> update(@PathVariable UUID uuid,
                                                 @RequestBody @Valid RawMaterialDto rawMaterialDto){
        RawMaterialDto rawMaterialUpdatedDto = rawMaterialService.update(rawMaterialDto, uuid);
        return ResponseEntity.ok(rawMaterialUpdatedDto);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RawMaterialDto> getByUuid(@PathVariable UUID uuid){
        RawMaterialDto rawMaterialDto = rawMaterialService.getByUuid(uuid);
        return ResponseEntity.ok(rawMaterialDto);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuid){
        rawMaterialService.delete(uuid);
    }
}
