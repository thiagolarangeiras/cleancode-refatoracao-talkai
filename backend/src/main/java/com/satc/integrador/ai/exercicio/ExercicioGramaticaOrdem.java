package com.satc.integrador.ai.exercicio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.satc.integrador.ai.planoestudo.PlanoEstudo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercicio_gramatica_ordem")
public class ExercicioGramaticaOrdem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_ordem_exercicio")
    private Integer idOrdemExercicio;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_plano_estudo")
    private PlanoEstudo planoEstudo;

    @Column(name = "frase_completa")
    private String fraseCompleta;

    @Column(name = "ordem_correta")
    private List<String> ordemCorreta;

    @Column(name = "ordem_aleatoria")
    private List<String> ordemAleatoria;

    @Column(name = "finalizado")
    private Boolean finalizado;
}