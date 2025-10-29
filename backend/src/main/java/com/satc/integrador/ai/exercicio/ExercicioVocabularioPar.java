package com.satc.integrador.ai.exercicio;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//{
//    "tipo": "VOCABULARIO_PARES",
//    "dados": {
//        "pares_esquerda": ["Social", "media", "platforms", "influence", "how", "people", "communicate"],
//        "pares_direita": ["Social", "midia", "plataforma", "influencia", "como", "pessoas", "comunicam"],
//    }
//},

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercicio_vocabulario_pares")
public class ExercicioVocabularioPar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_ordem_exercicio")
    private Integer idOrdemExercicio;

    @Column(name = "id_plano_estudo")
    private Integer idPlanoEstudo;

    @Column(name = "pares_esquerda")
    private List<String> paresEsquerda;

    @Column(name = "pares_direita")
    private List<String> paresDireita;

    @Column(name = "finalizado")
    private Boolean finalizado;
}