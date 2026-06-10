package com.unasp.taskapi.controller;

import com.unasp.taskapi.dto.TarefaRequestDTO;
import com.unasp.taskapi.dto.TarefaResponseDTO;
import com.unasp.taskapi.model.StatusTarefa;
import com.unasp.taskapi.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponseDTO criar(@RequestBody @Valid TarefaRequestDTO dto) {
        return tarefaService.criar(dto);
    }

    @GetMapping
    public List<TarefaResponseDTO> listar() {
        return tarefaService.listar();
    }

    @GetMapping("/{id}")
    public TarefaResponseDTO buscarPorId(@PathVariable Long id) {
        return tarefaService.buscarPorId(id);
    }

    @GetMapping("/status")
    public List<TarefaResponseDTO> buscarPorStatus(@RequestParam StatusTarefa status) {
        return tarefaService.buscarPorStatus(status);
    }

    @PutMapping("/{id}")
    public TarefaResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid TarefaRequestDTO dto) {
        return tarefaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
    }
}