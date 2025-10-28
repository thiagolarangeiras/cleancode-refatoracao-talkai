package com.satc.integrador.ai.chatgpt.dto;

import com.satc.integrador.ai.enums.TipoExercicio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExercicioGpt {
    public TipoExercicio tipo;
    public Object dados;
}