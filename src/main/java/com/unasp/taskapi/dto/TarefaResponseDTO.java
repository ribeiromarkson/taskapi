package com.unasp.taskapi.dto;

import com.unasp.taskapi.model.StatusTarefa;

import java.time.LocalDate;

public record TarefaResponseDTO(
        Long id,
        String titulo,
        String descricao,
        StatusTarefa status,
        LocalDate dataEntrega,
        CategoriaResponseDTO categoria
) {
}