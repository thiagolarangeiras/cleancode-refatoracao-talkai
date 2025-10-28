package com.satc.integrador.ai.planoestudo.dto;

import com.satc.integrador.ai.enums.TipoExercicio;
import com.satc.integrador.ai.exercicio.dto.ExercicioGramaticaComplementarGetDto;
import com.satc.integrador.ai.exercicio.dto.ExercicioGramaticaOrdemGetDto;
import com.satc.integrador.ai.exercicio.dto.ExercicioVocabularioParesGetDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanoEstudoGetDto {
    public Integer id;
    public Integer idUsuario;
    public Integer idPreferencia;
    public String nome;
    public LocalDate data;
    public Boolean ativo;
    public Boolean finalizado;
    public Integer qtExercicios;
    public Integer qtExerciciosFinalizados;
    public List<TipoExercicio> tiposExerciciosContidos;
    public List<ExercicioGramaticaComplementarGetDto> exerGramaCompl;
    public List<ExercicioGramaticaOrdemGetDto> exerGramaOrdem;
    public List<ExercicioVocabularioParesGetDto> exerVocPares;
}
