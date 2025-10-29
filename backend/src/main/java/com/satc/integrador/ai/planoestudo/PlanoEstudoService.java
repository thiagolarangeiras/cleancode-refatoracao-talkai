package com.satc.integrador.ai.planoestudo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satc.integrador.ai.chatgpt.GptService;
import com.satc.integrador.ai.chatgpt.dto.ExercicioGramaticaComplDto;
import com.satc.integrador.ai.chatgpt.dto.ExercicioGramaticaOrdemDto;
import com.satc.integrador.ai.chatgpt.dto.ExercicioVocParesDto;
import com.satc.integrador.ai.chatgpt.dto.ExercicioGpt;
import com.satc.integrador.ai.enums.TipoExercicio;
import com.satc.integrador.ai.exercicio.*;
import com.satc.integrador.ai.preferencia.PreferenciaService;
import com.satc.integrador.ai.preferencia.dto.PreferenciaGetDto;
import com.satc.integrador.ai.planoestudo.dto.PlanoEstudoGetDto;
import com.satc.integrador.ai.planoestudo.dto.PlanoEstudoListaGetDto;
import com.satc.integrador.ai.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanoEstudoService {

    @Autowired
    private PlanoEstudoRepo planoEstudoRepo;

    @Autowired
    private ExercicioGramaticaComplementarRepo exercicioGramaticaComplementarRepo;

    @Autowired
    private ExercicioGramaticaOrdemRepo exercicioGramaticaOrdemRepo;

    @Autowired
    private ExercicioVocabularioParRepo exercicioVocabularioParRepo;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PreferenciaService preferenciaService;

    @Autowired
    private GptService gptService;

    public void addExercicios(PlanoEstudoGetDto dto){
        dto.exerGramaCompl = exercicioGramaticaComplementarRepo.findByPlanoEstudo(dto.getId())
                .stream()
                .map(ExercicioGramaticaComplementar::mapToDto)
                .toList();

        dto.exerGramaOrdem = exercicioGramaticaOrdemRepo.findByPlanoEstudo(dto.getId())
                .stream()
                .map(ExercicioGramaticaOrdem::mapToDto)
                .toList();;
        dto.exerVocPares =  exercicioVocabularioParRepo.findByPlanoEstudo(dto.getId())
                .stream()
                .map(ExercicioVocabularioPar::mapToDto)
                .toList();;
    }

    public PlanoEstudoListaGetDto getAll(Integer page, Integer count){
        Pageable pageable = PageRequest.of(page, count);
        var planos = new PlanoEstudoListaGetDto();
        planos.planos = planoEstudoRepo.findByIdUsuario(usuarioService.getCurrentUserid(), pageable)
                .stream()
                .map(PlanoEstudo::mapToDto)
                .toList();
        Integer completos = 0;
        for(var plano : planos.planos){
            plano.exerGramaCompl = exercicioGramaticaComplementarRepo.findByPlanoEstudo(plano.id)
                    .stream()
                    .map(ExercicioGramaticaComplementar::mapToDto)
                    .toList();
            plano.exerGramaOrdem = exercicioGramaticaOrdemRepo.findByPlanoEstudo(plano.id)
                    .stream()
                    .map(ExercicioGramaticaOrdem::mapToDto)
                    .toList();
            plano.exerVocPares = exercicioVocabularioParRepo.findByPlanoEstudo(plano.id)
                    .stream()
                    .map(ExercicioVocabularioPar::mapToDto)
                    .toList();
            if(plano.finalizado){
                completos++;
            }
        }
        planos.porcentagemCompleta = (completos * 100) / planos.planos.size();
        return planos;
    }

    public PlanoEstudoGetDto getId(Integer id){
        Integer userId = usuarioService.getCurrentUserid();
        PlanoEstudo planoEstudo = planoEstudoRepo.findByIdUsuario(id, userId);
        PlanoEstudoGetDto dto = PlanoEstudo.mapToDto(planoEstudo);
        addExercicios(dto);
        return dto;
    }

    public PlanoEstudoGetDto getCurrent() {
        Integer userId = usuarioService.getCurrentUserid();
        PlanoEstudo planoEstudo = planoEstudoRepo.findByIdUsuarioActive(userId);
        if(planoEstudo == null){
            return generateNewPlan();
        }
        PlanoEstudoGetDto dto = PlanoEstudo.mapToDto(planoEstudo);
        addExercicios(dto);
        return dto;
    }

    public PlanoEstudoGetDto generateNewPlan() {
        Integer userId = usuarioService.getCurrentUserid();
        PreferenciaGetDto dtoPreferencia = preferenciaService.getCurrent();
        List<ExercicioGpt> exerciciosResponse = new ArrayList<ExercicioGpt>();
        {
            Integer qtExercicios = (dtoPreferencia.tempoMinutos() / 5) / 3;
            List<ExerciciosCall> exer = new ArrayList<ExerciciosCall>();
            exer.add(new ExerciciosCall(qtExercicios, TipoExercicio.GRAMATICA_COMPLEMENTAR));
            exer.add(new ExerciciosCall(qtExercicios, TipoExercicio.GRAMATICA_ORDEM));
            exer.add(new ExerciciosCall(qtExercicios, TipoExercicio.VOCABULARIO_PARES));
            String responseJson = gptService.gerarExercicios(dtoPreferencia, exer);
            ObjectMapper mapper = new ObjectMapper();
            try {
                exerciciosResponse = mapper.readValue(responseJson, new TypeReference<List<ExercicioGpt>>() {});
            } catch (JsonProcessingException e) {
                System.out.println("Erro");
            }

        }
        PlanoEstudo planoEstudo = new PlanoEstudo(
                userId,
                dtoPreferencia.id(),
                dtoPreferencia.nivel(),
                dtoPreferencia.idioma(),
                dtoPreferencia.tempoMinutos() / 5,
                new ArrayList<TipoExercicio>(){{
                    add(TipoExercicio.VOCABULARIO_PARES);
                    add(TipoExercicio.GRAMATICA_ORDEM);
                    add(TipoExercicio.GRAMATICA_COMPLEMENTAR);
                }}
        );
        var planoAntigo = planoEstudoRepo.findByIdUsuarioActive(userId);
        if(planoAntigo != null){
            planoAntigo.setAtivo(false);
            planoEstudoRepo.save(planoAntigo);
        }
        planoEstudoRepo.save(planoEstudo);
        Integer i = 0;
        ObjectMapper mapper = new ObjectMapper();
        for(ExercicioGpt exerRespose: exerciciosResponse){
            switch (exerRespose.tipo){
                case TipoExercicio.GRAMATICA_COMPLEMENTAR: {
                    ExercicioGramaticaComplDto x = mapper.convertValue(exerRespose.dados, ExercicioGramaticaComplDto.class);
                    ExercicioGramaticaComplementar exercicio = new ExercicioGramaticaComplementar();
                    exercicio.setIdOrdemExercicio(i);
                    exercicio.setIdPlanoEstudo(planoEstudo.getId());
                    exercicio.setFraseCompleta(x.fraseCompleta);
                    exercicio.setFraseIncompleta(x.fraseIncompleta);
                    exercicio.setOpcaoCorreta(x.opcaoCorreta);
                    exercicio.setOpcaoIncorreta(x.opcaoIncorreta);
                    exercicioGramaticaComplementarRepo.save(exercicio);
                } break;
                case TipoExercicio.GRAMATICA_ORDEM: {
                    ExercicioGramaticaOrdemDto x = mapper.convertValue(exerRespose.dados, ExercicioGramaticaOrdemDto.class);
                    ExercicioGramaticaOrdem exercicio = new ExercicioGramaticaOrdem();
                    exercicio.setIdOrdemExercicio(i);
                    exercicio.setIdPlanoEstudo(planoEstudo.getId());
                    exercicio.setFraseCompleta(x.fraseCompleta);
                    exercicio.setOrdemCorreta(x.ordemCorreta);
                    exercicio.setOrdemAleatoria(x.ordemAleatoria);
                    exercicioGramaticaOrdemRepo.save(exercicio);
                } break;
                case TipoExercicio.VOCABULARIO_PARES: {
                    ExercicioVocParesDto x = mapper.convertValue(exerRespose.dados, ExercicioVocParesDto.class);
                    ExercicioVocabularioPar exercicio = new ExercicioVocabularioPar();
                    exercicio.setIdOrdemExercicio(i);
                    exercicio.setIdPlanoEstudo(planoEstudo.getId());
                    exercicio.setParesEsquerda(x.paresEsquerda);
                    exercicio.setParesDireita(x.paresDireita);
                    exercicioVocabularioParRepo.save(exercicio);
                } break;
            }
            i++;
        }
        return PlanoEstudo.mapToDto(planoEstudo);
    }

    public Boolean finalizarPlanoDiario(Integer id){
        Integer userId = usuarioService.getCurrentUserid();
        PlanoEstudo planoEstudo = planoEstudoRepo.findByIdUsuario(id, userId);
        planoEstudo.setFinalizado(true);
        planoEstudoRepo.save(planoEstudo);
        return true;
    }

    public Boolean finalizarExercicio(Integer id, Integer idExercicio){
        Integer userId = usuarioService.getCurrentUserid();
        PlanoEstudo planoEstudo = planoEstudoRepo.findByIdUsuario(id, userId);

        // Essa coisa de separar uma tabela para eercicio ficou meio ruim, acho melhor salvar tudo em uma tabela
        // so e salvar os exercicios em um campo json numa tabela geral chamada exercicios, dai fica mais facil
        ExercicioGramaticaComplementar compl = exercicioGramaticaComplementarRepo.findByUsuario(idExercicio, id);
        if (compl != null){
            compl.setFinalizado(true);
            exercicioGramaticaComplementarRepo.save(compl);
        }
        ExercicioGramaticaOrdem ordem = exercicioGramaticaOrdemRepo.findByUsuario(idExercicio, id);
        if (ordem != null){
            ordem.setFinalizado(true);
            exercicioGramaticaOrdemRepo.save(ordem);
        }
        ExercicioVocabularioPar vocPares = exercicioVocabularioParRepo.findByUsuario(idExercicio, id);
        if (vocPares != null){
            vocPares.setFinalizado(true);
            exercicioVocabularioParRepo.save(vocPares);
        }
        planoEstudo.setQtExerciciosFinalizados(planoEstudo.getQtExerciciosFinalizados() + 1);
        planoEstudoRepo.save(planoEstudo);
        PlanoEstudoGetDto dto = PlanoEstudo.mapToDto(planoEstudo);
        addExercicios(dto);
        return true;
    }
}