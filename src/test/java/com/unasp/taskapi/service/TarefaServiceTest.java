package com.unasp.taskapi.service;

import com.unasp.taskapi.dto.TarefaRequestDTO;
import com.unasp.taskapi.dto.TarefaResponseDTO;
import com.unasp.taskapi.exception.ResourceNotFoundException;
import com.unasp.taskapi.model.Categoria;
import com.unasp.taskapi.model.StatusTarefa;
import com.unasp.taskapi.model.Tarefa;
import com.unasp.taskapi.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    void deveCriarTarefaComSucesso() {
        TarefaRequestDTO request = criarRequestDTO();
        Categoria categoria = criarCategoria();
        Tarefa tarefaSalva = criarTarefa();

        when(categoriaService.buscarEntidadePorId(1L)).thenReturn(categoria);
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaSalva);

        TarefaResponseDTO response = tarefaService.criar(request);

        assertEquals(1L, response.id());
        assertEquals("Fazer trabalho de Spring Boot", response.titulo());
        assertEquals(StatusTarefa.PENDENTE, response.status());
        assertEquals("Faculdade", response.categoria().nome());

        verify(categoriaService, times(1)).buscarEntidadePorId(1L);
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveListarTarefasComSucesso() {
        Tarefa tarefa = criarTarefa();

        when(tarefaRepository.findAll()).thenReturn(List.of(tarefa));

        List<TarefaResponseDTO> resultado = tarefaService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Fazer trabalho de Spring Boot", resultado.get(0).titulo());
        verify(tarefaRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarTarefaPorIdComSucesso() {
        Tarefa tarefa = criarTarefa();

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        TarefaResponseDTO response = tarefaService.buscarPorId(1L);

        assertEquals(1L, response.id());
        assertEquals("Fazer trabalho de Spring Boot", response.titulo());
        verify(tarefaRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoForEncontrada() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tarefaService.buscarPorId(99L)
        );

        assertEquals("Tarefa não encontrada com ID: 99", exception.getMessage());
        verify(tarefaRepository, times(1)).findById(99L);
    }

    @Test
    void deveBuscarTarefasPorStatusComSucesso() {
        Tarefa tarefa = criarTarefa();

        when(tarefaRepository.findByStatus(StatusTarefa.PENDENTE)).thenReturn(List.of(tarefa));

        List<TarefaResponseDTO> resultado = tarefaService.buscarPorStatus(StatusTarefa.PENDENTE);

        assertEquals(1, resultado.size());
        assertEquals(StatusTarefa.PENDENTE, resultado.get(0).status());
        verify(tarefaRepository, times(1)).findByStatus(StatusTarefa.PENDENTE);
    }

    @Test
    void deveAtualizarTarefaComSucesso() {
        Tarefa tarefaExistente = criarTarefa();
        Categoria categoria = criarCategoria();

        TarefaRequestDTO request = new TarefaRequestDTO(
                "Finalizar trabalho de Spring Boot",
                "Ajustar documentação, testes e deploy",
                StatusTarefa.EM_ANDAMENTO,
                LocalDate.of(2026, 6, 11),
                1L
        );

        Tarefa tarefaAtualizada = criarTarefa();
        tarefaAtualizada.setTitulo("Finalizar trabalho de Spring Boot");
        tarefaAtualizada.setDescricao("Ajustar documentação, testes e deploy");
        tarefaAtualizada.setStatus(StatusTarefa.EM_ANDAMENTO);
        tarefaAtualizada.setDataEntrega(LocalDate.of(2026, 6, 11));

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaExistente));
        when(categoriaService.buscarEntidadePorId(1L)).thenReturn(categoria);
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaAtualizada);

        TarefaResponseDTO response = tarefaService.atualizar(1L, request);

        assertEquals("Finalizar trabalho de Spring Boot", response.titulo());
        assertEquals(StatusTarefa.EM_ANDAMENTO, response.status());
        assertEquals(LocalDate.of(2026, 6, 11), response.dataEntrega());

        verify(tarefaRepository, times(1)).findById(1L);
        verify(categoriaService, times(1)).buscarEntidadePorId(1L);
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveDeletarTarefaComSucesso() {
        Tarefa tarefa = criarTarefa();

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        tarefaService.deletar(1L);

        verify(tarefaRepository, times(1)).findById(1L);
        verify(tarefaRepository, times(1)).delete(tarefa);
    }

    private TarefaRequestDTO criarRequestDTO() {
        return new TarefaRequestDTO(
                "Fazer trabalho de Spring Boot",
                "Criar API REST com JPA, Swagger e testes",
                StatusTarefa.PENDENTE,
                LocalDate.of(2026, 6, 11),
                1L
        );
    }

    private Categoria criarCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Faculdade");
        return categoria;
    }

    private Tarefa criarTarefa() {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);
        tarefa.setTitulo("Fazer trabalho de Spring Boot");
        tarefa.setDescricao("Criar API REST com JPA, Swagger e testes");
        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa.setDataEntrega(LocalDate.of(2026, 6, 11));
        tarefa.setCategoria(criarCategoria());
        return tarefa;
    }
}