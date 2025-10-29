package com.satc.integrador.ai.preferencia;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "preferencias")
public class Preferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "idioma")
    private String idioma;

    @Column(name = "dia_semana")
    private List<DayOfWeek> diaSemana;

    @Column(name = "tipo_exercicio")
    private List<String> tipoExercicio;

    @Column(name = "temas")
    private List<String> temas;

    @Column(name = "dificuldade")
    private String dificuldade;

    @Column(name = "nivel")
    private String nivel;

    @Column(name = "tempo_minutos")
    private Integer tempoMinutos;

    //valores de controle
    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "data_criacao")
    private Date dataCriacao;
}