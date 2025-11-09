package com.satc.integrador.ai.chatgpt;

import com.satc.integrador.ai.planoestudo.ExerciciosCall;
import com.satc.integrador.ai.preferencia.Preferencia;

import java.util.List;

public interface IGptService {
    String gerarExercicios(Preferencia preferencia, List<ExerciciosCall> exercicios);
}
