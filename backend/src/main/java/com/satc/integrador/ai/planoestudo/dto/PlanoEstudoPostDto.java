package com.satc.integrador.ai.planoestudo.dto;

import com.satc.integrador.ai.enums.TipoExercicio;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class PlanoEstudoPostDto {
    public Integer idUsuario;
    public Integer idPreferencia; //preferencias que moldaram esse plano

    public Integer qtExerciciosDia;
    public List<TipoExercicio> tiposExerciciosContidos;
}