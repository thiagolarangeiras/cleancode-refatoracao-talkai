package com.satc.integrador.ai.preferencia;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;

public class PreferenciaBuilder {

    private Integer id;
    private Integer idUsuario;
    private String idioma;
    private List<DayOfWeek> diaSemana;
    private List<String> tipoExercicio;
    private List<String> temas;
    private String dificuldade;
    private String nivel;
    private Integer tempoMinutos;
    private Boolean ativo;
    private Date dataCriacao;

    public static PreferenciaBuilder builder() {
        return new PreferenciaBuilder();
    }

    public PreferenciaBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public PreferenciaBuilder idUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }

    public PreferenciaBuilder idioma(String idioma) {
        this.idioma = idioma;
        return this;
    }

    public PreferenciaBuilder diaSemana(List<DayOfWeek> diaSemana) {
        this.diaSemana = diaSemana;
        return this;
    }

    public PreferenciaBuilder tipoExercicio(List<String> tipoExercicio) {
        this.tipoExercicio = tipoExercicio;
        return this;
    }

    public PreferenciaBuilder temas(List<String> temas) {
        this.temas = temas;
        return this;
    }

    public PreferenciaBuilder dificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
        return this;
    }

    public PreferenciaBuilder nivel(String nivel) {
        this.nivel = nivel;
        return this;
    }

    public PreferenciaBuilder tempoMinutos(Integer tempoMinutos) {
        this.tempoMinutos = tempoMinutos;
        return this;
    }

    public PreferenciaBuilder ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public PreferenciaBuilder dataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
        return this;
    }

    public Preferencia build() {
        Preferencia preferencia = new Preferencia();
        preferencia.setId(id);
        preferencia.setIdUsuario(idUsuario);
        preferencia.setIdioma(idioma);
        preferencia.setDiaSemana(diaSemana);
        preferencia.setTipoExercicio(tipoExercicio);
        preferencia.setTemas(temas);
        preferencia.setDificuldade(dificuldade);
        preferencia.setNivel(nivel);
        preferencia.setTempoMinutos(tempoMinutos);
        preferencia.setAtivo(ativo);
        preferencia.setDataCriacao(dataCriacao);
        return preferencia;
    }
}
