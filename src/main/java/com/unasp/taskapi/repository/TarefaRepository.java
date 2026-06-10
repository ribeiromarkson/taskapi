package com.unasp.taskapi.repository;

import com.unasp.taskapi.model.StatusTarefa;
import com.unasp.taskapi.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByStatus(StatusTarefa status);
}