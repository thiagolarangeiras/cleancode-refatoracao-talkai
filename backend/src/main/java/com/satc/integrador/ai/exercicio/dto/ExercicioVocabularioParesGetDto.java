package com.satc.integrador.ai.exercicio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExercicioVocabularioParesGetDto {
    private Integer id;
    private Integer idOrdemExercicio;
    private Integer idPlanoEstudo;

    private List<String> paresEsquerda;
    private List<String> paresDireita;
}