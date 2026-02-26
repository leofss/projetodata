package com.leonardo.projetodata.service;

import com.leonardo.projetodata.dto.PageableDto;
import com.leonardo.projetodata.dto.RawMaterialDto;
import com.leonardo.projetodata.enumerator.ErrorMessage;
import com.leonardo.projetodata.exception.RawMaterialCodeNotUnique;
import com.leonardo.projetodata.exception.RawMaterialNotFound;
import com.leonardo.projetodata.mapper.PageableMapper;
import com.leonardo.projetodata.mapper.RawMaterialMapper;
import com.leonardo.projetodata.model.RawMaterial;
import com.leonardo.projetodata.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RawMaterialService {
    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialMapper rawMaterialMapper;
    private final PageableMapper pageableMapper;

    @Transactional
    public RawMaterialDto create(RawMaterialDto rawMaterialDto){
        validateUniqueCodeForCreate(rawMaterialDto.getCode());
        RawMaterial rawMaterial = rawMaterialMapper.toEntity(rawMaterialDto);
        RawMaterial rawMaterialCreated = rawMaterialRepository.save(rawMaterial);
        return rawMaterialMapper.toDto(rawMaterialCreated);
    }

    public PageableDto getAll(Pageable pageable){
        Page<RawMaterial> rawMaterialPage = rawMaterialRepository.findAll(pageable);
        return pageableMapper.toDto(rawMaterialPage.map(rawMaterialMapper::toDto));
    }

    @Transactional
    public RawMaterialDto update(RawMaterialDto rawMaterialDto, UUID uuid){
        validateUniqueCodeForUpdate(rawMaterialDto.getCode(), uuid);
        RawMaterial rawMaterial = findByUuid(uuid);
        rawMaterialMapper.updateEntityFromDto(rawMaterialDto, rawMaterial);
        RawMaterial rawMaterialUpdated = rawMaterialRepository.save(rawMaterial);
        return rawMaterialMapper.toDto(rawMaterialUpdated);
    }

    public RawMaterialDto getByUuid(UUID uuid){
        RawMaterial rawMaterial = findByUuid(uuid);
        return rawMaterialMapper.toDto(rawMaterial);
    }

    public void delete(UUID uuid){
        RawMaterial rawMaterial = findByUuid(uuid);
        rawMaterialRepository.delete(rawMaterial);
    }

    public RawMaterial findByUuid(UUID uuid){
        return rawMaterialRepository.findById(uuid).orElseThrow(
                () -> new RawMaterialNotFound(ErrorMessage.RAW_MATERIAL_NOT_FOUND)
        );
    }

    private void validateUniqueCodeForUpdate(String code, UUID uuid){
        if (rawMaterialRepository.existsByCodeIgnoreCaseAndUuidNot(code, uuid)) {
            throw new RawMaterialCodeNotUnique(
                    ErrorMessage.RAW_MATERIAL_CODE_ALREADY_EXISTS, code
            );
        }
    }
    private void validateUniqueCodeForCreate(String code){
        if (rawMaterialRepository.existsByCode(code)) {
            throw new RawMaterialCodeNotUnique(
                    ErrorMessage.RAW_MATERIAL_CODE_ALREADY_EXISTS, code
            );
        }
    }
}
