package com.leonardo.projetodata.service;

import com.leonardo.projetodata.dto.PageableDto;
import com.leonardo.projetodata.dto.RawMaterialDto;
import com.leonardo.projetodata.exception.RawMaterialCodeNotUnique;
import com.leonardo.projetodata.exception.RawMaterialNotFound;
import com.leonardo.projetodata.mapper.PageableMapper;
import com.leonardo.projetodata.mapper.RawMaterialMapper;
import com.leonardo.projetodata.model.RawMaterial;
import com.leonardo.projetodata.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RawMaterialServiceTest {
    @InjectMocks
    private RawMaterialService rawMaterialService;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private RawMaterialMapper rawMaterialMapper;

    @Mock
    private PageableMapper pageableMapper;

    private RawMaterial rawMaterial;
    private RawMaterialDto rawMaterialDto;
    private UUID uuid;

    @BeforeEach
    void setup() {
        uuid = UUID.randomUUID();

        rawMaterial = new RawMaterial();
        rawMaterial.setUuid(uuid);
        rawMaterial.setCode("CODE123");

        rawMaterialDto = new RawMaterialDto();
        rawMaterialDto.setCode("CODE123");
    }

    @Test
    void shouldCreateRawMaterialSuccessfully() {
        when(rawMaterialRepository.existsByCode("CODE123")).thenReturn(false);
        when(rawMaterialMapper.toEntity(rawMaterialDto)).thenReturn(rawMaterial);
        when(rawMaterialRepository.save(rawMaterial)).thenReturn(rawMaterial);
        when(rawMaterialMapper.toDto(rawMaterial)).thenReturn(rawMaterialDto);

        RawMaterialDto result = rawMaterialService.create(rawMaterialDto);

        assertNotNull(result);
        assertEquals("CODE123", result.getCode());
        verify(rawMaterialRepository).save(rawMaterial);
    }

    @Test
    void shouldThrowExceptionWhenCodeAlreadyExistsOnCreate() {
        when(rawMaterialRepository.existsByCode("CODE123")).thenReturn(true);

        assertThrows(RawMaterialCodeNotUnique.class,
                () -> rawMaterialService.create(rawMaterialDto));

        verify(rawMaterialRepository, never()).save(any());
    }

    @Test
    void shouldReturnRawMaterialByUuid() {
        when(rawMaterialRepository.findById(uuid)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialMapper.toDto(rawMaterial)).thenReturn(rawMaterialDto);

        RawMaterialDto result = rawMaterialService.getByUuid(uuid);

        assertNotNull(result);
        verify(rawMaterialRepository).findById(uuid);
    }

    @Test
    void shouldThrowExceptionWhenRawMaterialNotFound() {
        when(rawMaterialRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(RawMaterialNotFound.class,
                () -> rawMaterialService.getByUuid(uuid));
    }

    @Test
    void shouldUpdateRawMaterialSuccessfully() {
        when(rawMaterialRepository.existsByCodeIgnoreCaseAndUuidNot("CODE123", uuid))
                .thenReturn(false);

        when(rawMaterialRepository.findById(uuid))
                .thenReturn(Optional.of(rawMaterial));

        when(rawMaterialRepository.save(rawMaterial))
                .thenReturn(rawMaterial);

        when(rawMaterialMapper.toDto(rawMaterial))
                .thenReturn(rawMaterialDto);

        RawMaterialDto result = rawMaterialService.update(rawMaterialDto, uuid);

        assertNotNull(result);
        verify(rawMaterialMapper).updateEntityFromDto(rawMaterialDto, rawMaterial);
        verify(rawMaterialRepository).save(rawMaterial);
    }

    @Test
    void shouldThrowExceptionWhenCodeAlreadyExistsOnUpdate() {
        when(rawMaterialRepository.existsByCodeIgnoreCaseAndUuidNot("CODE123", uuid))
                .thenReturn(true);

        assertThrows(RawMaterialCodeNotUnique.class,
                () -> rawMaterialService.update(rawMaterialDto, uuid));

        verify(rawMaterialRepository, never()).save(any());
    }

    @Test
    void shouldDeleteRawMaterialSuccessfully() {
        when(rawMaterialRepository.findById(uuid))
                .thenReturn(Optional.of(rawMaterial));

        rawMaterialService.delete(uuid);

        verify(rawMaterialRepository).delete(rawMaterial);
    }

    @Test
    void shouldReturnPagedRawMaterials() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RawMaterial> page = new PageImpl<>(List.of(rawMaterial));

        when(rawMaterialRepository.findAll(pageable)).thenReturn(page);
        when(rawMaterialMapper.toDto(rawMaterial)).thenReturn(rawMaterialDto);
        when(pageableMapper.toDto(any())).thenReturn(new PageableDto());

        PageableDto result = rawMaterialService.getAll(pageable);

        assertNotNull(result);
        verify(rawMaterialRepository).findAll(pageable);
    }
}
