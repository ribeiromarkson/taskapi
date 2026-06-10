package com.unasp.taskapi.service;

import com.unasp.taskapi.dto.CategoriaResponseDTO;
import com.unasp.taskapi.dto.TarefaRequestDTO;
import com.unasp.taskapi.dto.TarefaResponseDTO;
import com.unasp.taskapi.exception.ResourceNotFoundException;
import com.unasp.taskapi.model.Categoria;
import com.unasp.taskapi.model.StatusTarefa;
import com.unasp.taskapi.model.Tarefa;
import com.unasp.taskapi.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final CategoriaService categoriaService;

    public TarefaService(TarefaRepository tarefaRepository, CategoriaService categoriaService) {
        this.tarefaRepository = tarefaRepository;
        this.categoriaService = categoriaService;
    }

    public TarefaResponseDTO criar(TarefaRequestDTO dto) {
        Categoria categoria = categoriaService.buscarEntidadePorId(dto.categoriaId());

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setStatus(dto.status());
        tarefa.setDataEntrega(dto.dataEntrega());
        tarefa.setCategoria(categoria);

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        return toResponseDTO(tarefaSalva);
    }

    public List<TarefaResponseDTO> listar() {
        return tarefaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TarefaResponseDTO buscarPorId(Long id) {
        Tarefa tarefa = buscarEntidadePorId(id);
        return toResponseDTO(tarefa);
    }

    public List<TarefaResponseDTO> buscarPorStatus(StatusTarefa status) {
        return tarefaRepository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto) {
        Tarefa tarefa = buscarEntidadePorId(id);
        Categoria categoria = categoriaService.buscarEntidadePorId(dto.categoriaId());

        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setStatus(dto.status());
        tarefa.setDataEntrega(dto.dataEntrega());
        tarefa.setCategoria(categoria);

        Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);

        return toResponseDTO(tarefaAtualizada);
    }

    public void deletar(Long id) {
        Tarefa tarefa = buscarEntidadePorId(id);
        tarefaRepository.delete(tarefa);
    }

    private Tarefa buscarEntidadePorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));
    }

    private TarefaResponseDTO toResponseDTO(Tarefa tarefa) {
        CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
                tarefa.getCategoria().getId(),
                tarefa.getCategoria().getNome()
        );

        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataEntrega(),
                categoriaDTO
        );
    }
}