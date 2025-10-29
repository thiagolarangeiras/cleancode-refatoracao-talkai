package com.satc.integrador.ai.exercicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExercicioVocabularioParRepo extends JpaRepository<ExercicioVocabularioPar, Integer> {

    @Query(value = "select * from exercicio_vocabulario_pares where id_plano_estudo = ?1", nativeQuery = true)
    List<ExercicioVocabularioPar> findByPlanoEstudo(Integer idPlanoEstudo);

    @Query(value = "select * from exercicio_vocabulario_pares where id = ?1 and id_plano_estudo = ?2", nativeQuery = true)
    ExercicioVocabularioPar findByUsuario(Integer id, Integer idPlanoEstudo);
}