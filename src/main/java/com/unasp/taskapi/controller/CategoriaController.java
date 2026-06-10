package com.unasp.taskapi.controller;

import com.unasp.taskapi.dto.CategoriaRequestDTO;
import com.unasp.taskapi.dto.CategoriaResponseDTO;
import com.unasp.taskapi.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO criar(@RequestBody @Valid CategoriaRequestDTO dto) {
        return categoriaService.criar(dto);
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar() {
        return categoriaService.listar();
    }
}