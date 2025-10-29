package com.satc.integrador.ai.planoestudo;

import com.satc.integrador.ai.enums.TipoExercicio;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plano_estudo")
public class PlanoEstudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_preferencia")
    private Integer idPreferencia;

    @Column(name = "nome")
    private String nome;

    @Column(name = "qt_exercicio")
    private Integer qtExercicios;

    @Column(name = "qt_exercicio_finalizados")
    private Integer qtExerciciosFinalizados;

    @Column(name = "tipos_exercicios_contidos")
    private List<TipoExercicio> tiposExerciciosContidos;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "finalizado")
    private Boolean finalizado;
}