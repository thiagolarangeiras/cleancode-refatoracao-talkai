package com.satc.integrador.ai.chatgpt;

import com.satc.integrador.ai.planoestudo.ExerciciosCall;
import com.satc.integrador.ai.preferencia.Preferencia;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("local")
public class GptFakeService implements IGptService{
    @Override
    public String gerarExercicios(Preferencia preferencia, List<ExerciciosCall> exercicios) {
        String s = """
        [
                    {
                        "tipo": "GRAMATICA_COMPLEMENTAR",
                        "dados": {
                              "frase_completa": "She goes to school every day.",
                              "frase_incompleta": "She ___ to school every day.",
                              "opcao_correta": "goes",
                              "opcao_incorreta": ["go", "going", "gone"]
                        }
                   },
                   {
                        "tipo": "GRAMATICA_ORDEM",
                        "dados": {
                              "frase_completa": "Ela vai visitar a avó dela amanhã.",
                              "ordem_correta": ["She", "is", "going", "to", "visit", "her", "grandmother", "tomorrow"],
                              "ordem_aleatoria": ["visit", "tomorrow", "her", "going", "She", "is", "to", "grandmother"]
                        }
                   },
                    {
                        "tipo": "VOCABULARIO_PARES",
                        "dados": {
                              "pares_esquerda": ["Car", "House", "Computer", "Water"],
                              "pares_direita": ["Carro", "Casa", "Computador", "Água"]
                        }
                    }
        ]
        """;
        return s;
    }
}
