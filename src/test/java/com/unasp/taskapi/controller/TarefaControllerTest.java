package com.unasp.taskapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.unasp.taskapi.dto.CategoriaResponseDTO;
import com.unasp.taskapi.dto.TarefaRequestDTO;
import com.unasp.taskapi.dto.TarefaResponseDTO;
import com.unasp.taskapi.exception.GlobalExceptionHandler;
import com.unasp.taskapi.exception.ResourceNotFoundException;
import com.unasp.taskapi.model.StatusTarefa;
import com.unasp.taskapi.service.TarefaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TarefaControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private TarefaService tarefaService;

    @InjectMocks
    private TarefaController tarefaController;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders
                .standaloneSetup(tarefaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void deveCriarTarefaComSucesso() throws Exception {
        TarefaRequestDTO request = criarRequestDTO();
        TarefaResponseDTO response = criarResponseDTO();

        when(tarefaService.criar(any(TarefaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Fazer trabalho de Spring Boot"))
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.categoria.nome").value("Faculdade"));

        verify(tarefaService, times(1)).criar(any(TarefaRequestDTO.class));
    }

    @Test
    void deveListarTarefasComSucesso() throws Exception {
        when(tarefaService.listar()).thenReturn(List.of(criarResponseDTO()));

        mockMvc.perform(get("/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].titulo").value("Fazer trabalho de Spring Boot"));

        verify(tarefaService, times(1)).listar();
    }

    @Test
    void deveBuscarTarefaPorIdComSucesso() throws Exception {
        when(tarefaService.buscarPorId(1L)).thenReturn(criarResponseDTO());

        mockMvc.perform(get("/tarefas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Fazer trabalho de Spring Boot"));

        verify(tarefaService, times(1)).buscarPorId(1L);
    }

    @Test
    void deveBuscarTarefasPorStatusComSucesso() throws Exception {
        when(tarefaService.buscarPorStatus(StatusTarefa.PENDENTE)).thenReturn(List.of(criarResponseDTO()));

        mockMvc.perform(get("/tarefas/status")
                        .param("status", "PENDENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDENTE"));

        verify(tarefaService, times(1)).buscarPorStatus(StatusTarefa.PENDENTE);
    }

    @Test
    void deveAtualizarTarefaComSucesso() throws Exception {
        TarefaRequestDTO request = new TarefaRequestDTO(
                "Finalizar trabalho de Spring Boot",
                "Ajustar documentação, testes e deploy",
                StatusTarefa.EM_ANDAMENTO,
                LocalDate.of(2026, 6, 11),
                1L
        );

        TarefaResponseDTO response = new TarefaResponseDTO(
                1L,
                "Finalizar trabalho de Spring Boot",
                "Ajustar documentação, testes e deploy",
                StatusTarefa.EM_ANDAMENTO,
                LocalDate.of(2026, 6, 11),
                new CategoriaResponseDTO(1L, "Faculdade")
        );

        when(tarefaService.atualizar(eq(1L), any(TarefaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/tarefas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Finalizar trabalho de Spring Boot"))
                .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"));

        verify(tarefaService, times(1)).atualizar(eq(1L), any(TarefaRequestDTO.class));
    }

    @Test
    void deveDeletarTarefaComSucesso() throws Exception {
        doNothing().when(tarefaService).deletar(1L);

        mockMvc.perform(delete("/tarefas/1"))
                .andExpect(status().isNoContent());

        verify(tarefaService, times(1)).deletar(1L);
    }

    @Test
    void deveRetornarNotFoundQuandoTarefaNaoForEncontrada() throws Exception {
        when(tarefaService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Tarefa não encontrada com ID: 99"));

        mockMvc.perform(get("/tarefas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.mensagem").value("Tarefa não encontrada com ID: 99"));

        verify(tarefaService, times(1)).buscarPorId(99L);
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

    private TarefaResponseDTO criarResponseDTO() {
        return new TarefaResponseDTO(
                1L,
                "Fazer trabalho de Spring Boot",
                "Criar API REST com JPA, Swagger e testes",
                StatusTarefa.PENDENTE,
                LocalDate.of(2026, 6, 11),
                new CategoriaResponseDTO(1L, "Faculdade")
        );
    }
}