package com.satc.integrador.ai.exercicio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.satc.integrador.ai.planoestudo.PlanoEstudo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//{
//    "tipo": "GRAMATICA_COMPLEMENTAR",
//    "dados": {
//        "frase_completa": "Social media platforms influence how people communicate.",
//        "frase_incompleta": "Social media platforms ... how people communicate.",
//        "opcao_correta": "influence",
//        "opcao_incorreta": ["create", "follow", "interrupt"]
//    }
//},

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercicio_gramatica_complementar")
public class ExercicioGramaticaComplementar {

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

    @Column(name = "frase_incompleta")
    private String fraseIncompleta;

    @Column(name = "opcao_correta")
    private String opcaoCorreta;

    @Column(name = "opcao_incorreta")
    private List<String> opcaoIncorreta;

    @Column(name = "finalizado")
    private Boolean finalizado;
}