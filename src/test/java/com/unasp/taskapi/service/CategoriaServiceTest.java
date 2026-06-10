package com.unasp.taskapi.service;

import com.unasp.taskapi.dto.CategoriaRequestDTO;
import com.unasp.taskapi.dto.CategoriaResponseDTO;
import com.unasp.taskapi.exception.ResourceNotFoundException;
import com.unasp.taskapi.model.Categoria;
import com.unasp.taskapi.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void deveCriarCategoriaComSucesso() {
        CategoriaRequestDTO request = new CategoriaRequestDTO("Faculdade");

        Categoria categoriaSalva = new Categoria();
        categoriaSalva.setId(1L);
        categoriaSalva.setNome("Faculdade");

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaSalva);

        CategoriaResponseDTO response = categoriaService.criar(request);

        assertEquals(1L, response.id());
        assertEquals("Faculdade", response.nome());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void deveListarCategoriasComSucesso() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Faculdade");

        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<CategoriaResponseDTO> resultado = categoriaService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Faculdade", resultado.get(0).nome());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarCategoriaPorIdComSucesso() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Faculdade");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.buscarEntidadePorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Faculdade", resultado.getNome());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoForEncontrada() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.buscarEntidadePorId(99L)
        );

        assertEquals("Categoria não encontrada com ID: 99", exception.getMessage());
        verify(categoriaRepository, times(1)).findById(99L);
    }
}