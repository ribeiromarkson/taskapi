package com.unasp.taskapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unasp.taskapi.dto.CategoriaRequestDTO;
import com.unasp.taskapi.dto.CategoriaResponseDTO;
import com.unasp.taskapi.exception.GlobalExceptionHandler;
import com.unasp.taskapi.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(categoriaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveCriarCategoriaComSucesso() throws Exception {
        CategoriaResponseDTO response = new CategoriaResponseDTO(1L, "Faculdade");

        when(categoriaService.criar(any(CategoriaRequestDTO.class))).thenReturn(response);

        CategoriaRequestDTO request = new CategoriaRequestDTO("Faculdade");

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Faculdade"));

        verify(categoriaService, times(1)).criar(any(CategoriaRequestDTO.class));
    }

    @Test
    void deveListarCategoriasComSucesso() throws Exception {
        CategoriaResponseDTO categoria = new CategoriaResponseDTO(1L, "Faculdade");

        when(categoriaService.listar()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Faculdade"));

        verify(categoriaService, times(1)).listar();
    }

    @Test
    void deveRetornarBadRequestQuandoCategoriaForInvalida() throws Exception {
        String jsonInvalido = """
                {
                  "nome": ""
                }
                """;

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Erro de validação"));
    }
}