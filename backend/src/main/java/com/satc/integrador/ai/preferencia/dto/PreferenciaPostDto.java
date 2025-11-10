package com.satc.integrador.ai.preferencia.dto;

import java.time.DayOfWeek;
import java.util.List;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreferenciaPostDto {
    private Integer idUsuario;
    private String idioma;
    private List<String> tipoExercicio;
    private List<String> temas;
    private String dificuldade;
    private String nivel;
    private List<DayOfWeek> diaSemana;
    private Integer tempoMinuto;
}