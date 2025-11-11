package com.satc.integrador.ai.exercicio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.satc.integrador.ai.planoestudo.PlanoEstudo;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_plano_estudo")
    private PlanoEstudo planoEstudo;

    @Column(name = "pares_esquerda")
    private List<String> paresEsquerda;

    @Column(name = "pares_direita")
    private List<String> paresDireita;

    @Column(name = "finalizado")
    private Boolean finalizado;
}