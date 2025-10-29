package com.satc.integrador.ai.exercicio;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//{
//    "tipo": "GRAMATICA_ORDEM",
//    "dados": {
//        "frase_completa": "Social media platforms influence how people communicate.",
//        "ordem_correta": ["Social", "media", "platforms", "influence", "how", "people", "communicate."],
//        "ordem_aleatoria": ["communicate.", "media", "Social", "influence", "platforms", "how", "people"]
//    }
//},

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

    @Column(name = "id_plano_estudo")
    private Integer idPlanoEstudo;

    @Column(name = "frase_completa")
    private String fraseCompleta;

    @Column(name = "ordem_correta")
    private List<String> ordemCorreta;

    @Column(name = "ordem_aleatoria")
    private List<String> ordemAleatoria;

    @Column(name = "finalizado")
    private Boolean finalizado;
}