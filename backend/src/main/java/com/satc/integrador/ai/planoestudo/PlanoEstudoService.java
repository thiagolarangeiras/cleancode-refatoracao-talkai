package com.satc.integrador.ai.planoestudo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satc.integrador.ai.auth.SecurityUtil;
import com.satc.integrador.ai.auth.UserDetailsImpl;
import com.satc.integrador.ai.chatgpt.IGptService;
import com.satc.integrador.ai.chatgpt.dto.ExercicioGramaticaComplDto;
import com.satc.integrador.ai.chatgpt.dto.ExercicioGramaticaOrdemDto;
import com.satc.integrador.ai.chatgpt.dto.ExercicioVocParesDto;
import com.satc.integrador.ai.chatgpt.dto.ExercicioGpt;
import com.satc.integrador.ai.enums.TipoExercicio;
import com.satc.integrador.ai.exercicio.*;
import com.satc.integrador.ai.preferencia.Preferencia;
import com.satc.integrador.ai.preferencia.PreferenciaRepository;
import com.satc.integrador.ai.planoestudo.dto.PlanoEstudoListaGetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PlanoEstudoService {

    @Autowired
    private PlanoEstudoRepository planoEstudoRepository;

    @Autowired
    private PreferenciaRepository preferenciaRepository;

    @Autowired
    private ExercicioGramaticaComplementarRepo exercicioGramaticaComplementarRepo;

    @Autowired
    private ExercicioGramaticaOrdemRepository exercicioGramaticaOrdemRepository;

    @Autowired
    private ExercicioVocabularioParRepository exercicioVocabularioParRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IGptService gptService;

    public PlanoEstudoListaGetDto getAll(){
        UserDetailsImpl userDetails = SecurityUtil.getCurrentLoggedUser();
        PlanoEstudoListaGetDto planoEstudoListaGetDto = new PlanoEstudoListaGetDto();
        List<PlanoEstudo> planosEstudosSalvos = planoEstudoRepository.findByIdUsuario(userDetails.getUser().getId());
        planoEstudoListaGetDto.setPlanos(planosEstudosSalvos);
        Integer completos = 0;
        for (PlanoEstudo planoEstudo : planosEstudosSalvos) {
            if (planoEstudo.getFinalizado()) {
                completos++;
            }
        }
        planoEstudoListaGetDto.setPorcentagemCompleta((completos * 100) / planosEstudosSalvos.size());
        return planoEstudoListaGetDto;
    }

    public PlanoEstudo getById(Integer id){
        UserDetailsImpl userDetails = SecurityUtil.getCurrentLoggedUser();
        return planoEstudoRepository.findByIdPlanoEstudoAndIdUsuario(id, userDetails.getUser().getId());
    }

    public PlanoEstudo getCurrent() {
        UserDetailsImpl userDetails = SecurityUtil.getCurrentLoggedUser();
        return planoEstudoRepository.findByIdUsuarioAtivo(userDetails.getUser().getId());
    }

    public PlanoEstudo handleNewPlan() {
        UserDetailsImpl userDetails = SecurityUtil.getCurrentLoggedUser();
        Preferencia preferencia = preferenciaRepository.findByIdUsuarioActive(userDetails.getUser().getId());
        List<ExercicioGpt> exerciciosCriados = handleGeracaoExercicios(preferencia);
        PlanoEstudo planoNovo = createNewPlan(userDetails, preferencia, exerciciosCriados);
        disableOldPlan(userDetails);
        buildExercicios(exerciciosCriados, planoNovo);
        return planoNovo;
    }

    private List<ExercicioGpt> handleGeracaoExercicios(Preferencia preferencia) {
        List<ExerciciosCall> exerciciosCalls = new ArrayList<>();
        exerciciosCalls.add(new ExerciciosCall(getQtdExercicioPorTipo(), TipoExercicio.GRAMATICA_COMPLEMENTAR));
        exerciciosCalls.add(new ExerciciosCall(getQtdExercicioPorTipo(), TipoExercicio.GRAMATICA_ORDEM));
        exerciciosCalls.add(new ExerciciosCall(getQtdExercicioPorTipo(), TipoExercicio.VOCABULARIO_PARES));
        String responseJson = gptService.gerarExercicios(preferencia, exerciciosCalls);
        List<ExercicioGpt> exerciciosCriados;
        try {
            exerciciosCriados = objectMapper.readValue(responseJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Não foi possível converter o JSON para o exercicios: " + responseJson);
        }
        return exerciciosCriados;
    }

    private void buildExercicios(List<ExercicioGpt> exerciciosCriados, PlanoEstudo planoEstudo) {
        Integer idOrdemExercicio = 0;
        for (ExercicioGpt exercicioCriado : exerciciosCriados) {
            switch (exercicioCriado.getTipo()){
                case TipoExercicio.GRAMATICA_COMPLEMENTAR: {
                    ExercicioGramaticaComplementar exercicio = buildExercicioGramaticaComplementar(exercicioCriado, planoEstudo, idOrdemExercicio);
                    exercicioGramaticaComplementarRepo.save(exercicio);
                } break;
                case TipoExercicio.GRAMATICA_ORDEM: {
                    ExercicioGramaticaOrdem exercicio = buildExercicioGramaticaOrdem(exercicioCriado, planoEstudo, idOrdemExercicio);
                    exercicioGramaticaOrdemRepository.save(exercicio);
                } break;
                case TipoExercicio.VOCABULARIO_PARES: {
                    ExercicioVocabularioPar exercicio = buildExercicioVocabularioPar(exercicioCriado, planoEstudo, idOrdemExercicio);
                    exercicioVocabularioParRepository.save(exercicio);
                } break;
            }
            idOrdemExercicio++;
        }
    }

    private ExercicioGramaticaComplementar buildExercicioGramaticaComplementar(ExercicioGpt exercicioCriado, PlanoEstudo planoEstudo, Integer idOrdemExercicio) {
        ExercicioGramaticaComplDto exercicioGramaticaComplDto = objectMapper.convertValue(exercicioCriado.getDados(), ExercicioGramaticaComplDto.class);
        ExercicioGramaticaComplementar exercicio = new ExercicioGramaticaComplementar();
        exercicio.setIdOrdemExercicio(idOrdemExercicio);
        exercicio.setPlanoEstudo(planoEstudo);
        exercicio.setFraseCompleta(exercicioGramaticaComplDto.fraseCompleta);
        exercicio.setFraseIncompleta(exercicioGramaticaComplDto.fraseIncompleta);
        exercicio.setOpcaoCorreta(exercicioGramaticaComplDto.opcaoCorreta);
        exercicio.setOpcaoIncorreta(exercicioGramaticaComplDto.opcaoIncorreta);
        return exercicio;
    }

    private ExercicioGramaticaOrdem buildExercicioGramaticaOrdem(ExercicioGpt exercicioCriado, PlanoEstudo planoEstudo, Integer idOrdemExercicio) {
        ExercicioGramaticaOrdemDto exercicioGramaticaOrdemDto = objectMapper.convertValue(exercicioCriado.getDados(), ExercicioGramaticaOrdemDto.class);
        ExercicioGramaticaOrdem exercicio = new ExercicioGramaticaOrdem();
        exercicio.setIdOrdemExercicio(idOrdemExercicio);
        exercicio.setPlanoEstudo(planoEstudo);
        exercicio.setFraseCompleta(exercicioGramaticaOrdemDto.fraseCompleta);
        exercicio.setOrdemCorreta(exercicioGramaticaOrdemDto.ordemCorreta);
        exercicio.setOrdemAleatoria(exercicioGramaticaOrdemDto.ordemAleatoria);
        return exercicio;
    }

    private ExercicioVocabularioPar buildExercicioVocabularioPar(ExercicioGpt exercicioCriado, PlanoEstudo planoEstudo, Integer idOrdemExercicio) {
        ExercicioVocParesDto exercicioVocParesDto = objectMapper.convertValue(exercicioCriado.getDados(), ExercicioVocParesDto.class);
        ExercicioVocabularioPar exercicio = new ExercicioVocabularioPar();
        exercicio.setIdOrdemExercicio(idOrdemExercicio);
        exercicio.setPlanoEstudo(planoEstudo);
        exercicio.setParesEsquerda(exercicioVocParesDto.paresEsquerda);
        exercicio.setParesDireita(exercicioVocParesDto.paresDireita);
        return exercicio;
    }

    private void disableOldPlan(UserDetailsImpl userDetails) {
        var planoAntigo = planoEstudoRepository.findByIdUsuarioAtivo(userDetails.getUser().getId());
        if(planoAntigo != null){
            planoAntigo.setAtivo(false);
            planoEstudoRepository.save(planoAntigo);
        }
    }

    private PlanoEstudo createNewPlan(UserDetailsImpl userDetails, Preferencia preferencia, List<ExercicioGpt> exerciciosCriados) {
        List<TipoExercicio> exerciciosContidos = new ArrayList<>();
        exerciciosContidos.add(TipoExercicio.GRAMATICA_COMPLEMENTAR);
        exerciciosContidos.add(TipoExercicio.GRAMATICA_ORDEM);
        exerciciosContidos.add(TipoExercicio.VOCABULARIO_PARES);

        PlanoEstudo planoEstudo = new PlanoEstudo(
                userDetails.getUser().getId(),
                preferencia.getId(),
                preferencia.getIdioma() + " " + preferencia.getNivel(),
                exerciciosCriados.size(),
                0,
                exerciciosContidos,
                LocalDate.now(),
                true,
                false
        );
        return planoEstudoRepository.save(planoEstudo);
    }

    private Integer getQtdExercicioPorTipo() {
        return ThreadLocalRandom.current().nextInt(1, 4);
    }

    public Boolean finalizarPlanoDiario(Integer id) {
        UserDetailsImpl userDetails = SecurityUtil.getCurrentLoggedUser();
        PlanoEstudo planoEstudo = planoEstudoRepository.findByIdPlanoEstudoAndIdUsuario(id, userDetails.getUser().getId());
        planoEstudo.setFinalizado(true);
        planoEstudoRepository.save(planoEstudo);
        return true;
    }

    public void finalizarExercicio(Integer id, Integer idExercicio) {
        UserDetailsImpl userDetails = SecurityUtil.getCurrentLoggedUser();
        PlanoEstudo planoEstudo = planoEstudoRepository.findByIdPlanoEstudoAndIdUsuario(id, userDetails.getUser().getId());

        ExercicioGramaticaComplementar compl = exercicioGramaticaComplementarRepo.findByUsuario(idExercicio, id);
        if (compl != null){
            compl.setFinalizado(true);
            exercicioGramaticaComplementarRepo.save(compl);
        }
        ExercicioGramaticaOrdem ordem = exercicioGramaticaOrdemRepository.findByUsuario(idExercicio, id);
        if (ordem != null){
            ordem.setFinalizado(true);
            exercicioGramaticaOrdemRepository.save(ordem);
        }
        ExercicioVocabularioPar vocPares = exercicioVocabularioParRepository.findByUsuario(idExercicio, id);
        if (vocPares != null){
            vocPares.setFinalizado(true);
            exercicioVocabularioParRepository.save(vocPares);
        }
        planoEstudo.setQtExerciciosFinalizados(planoEstudo.getQtExerciciosFinalizados() + 1);
        planoEstudoRepository.save(planoEstudo);
    }
}