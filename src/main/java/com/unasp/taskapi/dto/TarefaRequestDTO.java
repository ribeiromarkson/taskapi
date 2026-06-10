package com.unasp.taskapi.dto;

import com.unasp.taskapi.model.StatusTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TarefaRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
        String titulo,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "O status é obrigatório")
        StatusTarefa status,

        LocalDate dataEntrega,

        @NotNull(message = "O ID da categoria é obrigatório")
        Long categoriaId
) {
}
