package com.satc.integrador.ai.planoestudo;

import com.satc.integrador.ai.enums.TipoExercicio;
import com.satc.integrador.ai.exercicio.ExercicioGramaticaComplementar;
import com.satc.integrador.ai.exercicio.ExercicioGramaticaOrdem;
import com.satc.integrador.ai.exercicio.ExercicioVocabularioPar;

import java.time.LocalDate;
import java.util.List;

public class PlanoEstudoBuilder {

    private Integer id;
    private Integer idUsuario;
    private Integer idPreferencia;
    private String nome;
    private Integer qtExercicios;
    private Integer qtExerciciosFinalizados;
    private List<TipoExercicio> tiposExerciciosContidos;
    private LocalDate data;
    private Boolean ativo;
    private Boolean finalizado;
    private List<ExercicioGramaticaComplementar> exercicioGramaticaComplementares;
    private List<ExercicioGramaticaOrdem> exercicioGramaticaOrdens;
    private List<ExercicioVocabularioPar> exercicioVocabularioPares;

    public static PlanoEstudoBuilder builder() {
        return new PlanoEstudoBuilder();
    }

    public PlanoEstudoBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public PlanoEstudoBuilder idUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }

    public PlanoEstudoBuilder idPreferencia(Integer idPreferencia) {
        this.idPreferencia = idPreferencia;
        return this;
    }

    public PlanoEstudoBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public PlanoEstudoBuilder qtExercicios(Integer qtExercicios) {
        this.qtExercicios = qtExercicios;
        return this;
    }

    public PlanoEstudoBuilder qtExerciciosFinalizados(Integer qtExerciciosFinalizados) {
        this.qtExerciciosFinalizados = qtExerciciosFinalizados;
        return this;
    }

    public PlanoEstudoBuilder tiposExerciciosContidos(List<TipoExercicio> tiposExerciciosContidos) {
        this.tiposExerciciosContidos = tiposExerciciosContidos;
        return this;
    }

    public PlanoEstudoBuilder data(LocalDate data) {
        this.data = data;
        return this;
    }

    public PlanoEstudoBuilder ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public PlanoEstudoBuilder finalizado(Boolean finalizado) {
        this.finalizado = finalizado;
        return this;
    }

    public PlanoEstudoBuilder exercicioGramaticaComplementares(List<ExercicioGramaticaComplementar> lista) {
        this.exercicioGramaticaComplementares = lista;
        return this;
    }

    public PlanoEstudoBuilder exercicioGramaticaOrdens(List<ExercicioGramaticaOrdem> lista) {
        this.exercicioGramaticaOrdens = lista;
        return this;
    }

    public PlanoEstudoBuilder exercicioVocabularioPares(List<ExercicioVocabularioPar> lista) {
        this.exercicioVocabularioPares = lista;
        return this;
    }

    public PlanoEstudo build() {
        PlanoEstudo plano = new PlanoEstudo(
                idUsuario,
                idPreferencia,
                nome,
                qtExercicios,
                qtExerciciosFinalizados,
                tiposExerciciosContidos,
                data,
                ativo,
                finalizado
        );

        plano.setId(id);
        plano.setExercicioGramaticaComplementares(exercicioGramaticaComplementares);
        plano.setExercicioGramaticaOrdens(exercicioGramaticaOrdens);
        plano.setExercicioVocabularioPares(exercicioVocabularioPares);

        return plano;
    }
}
