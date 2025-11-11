package com.satc.integrador.ai.planoestudo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satc.integrador.ai.auth.SecurityUtil;
import com.satc.integrador.ai.auth.UserDetailsImpl;
import com.satc.integrador.ai.chatgpt.IGptService;
import com.satc.integrador.ai.chatgpt.dto.ExercicioGpt;
import com.satc.integrador.ai.enums.TipoExercicio;
import com.satc.integrador.ai.exercicio.ExercicioGramaticaComplementarRepository;
import com.satc.integrador.ai.exercicio.ExercicioGramaticaOrdemRepository;
import com.satc.integrador.ai.exercicio.ExercicioVocabularioParRepository;
import com.satc.integrador.ai.preferencia.Preferencia;
import com.satc.integrador.ai.preferencia.PreferenciaRepository;
import com.satc.integrador.ai.usuario.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanoEstudoServiceTest {

    @Spy
    @InjectMocks
    private PlanoEstudoService planoEstudoService;

    @Mock
    private PlanoEstudoRepository planoEstudoRepository;

    @Mock
    private PreferenciaRepository preferenciaRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private IGptService gptService;

    @Mock
    private ExercicioGramaticaComplementarRepository exercicioGramaticaComplementarRepository;

    @Mock
    private ExercicioGramaticaOrdemRepository exercicioGramaticaOrdemRepository;

    @Mock
    private ExercicioVocabularioParRepository exercicioVocabularioParRepository;

    private MockedStatic<SecurityUtil> securityUtilMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        Usuario user = mock(Usuario.class);
        when(user.getId()).thenReturn(1);
        when(userDetails.getUser()).thenReturn(user);

        securityUtilMock = mockStatic(SecurityUtil.class);
        securityUtilMock.when(SecurityUtil::getCurrentLoggedUser).thenReturn(userDetails);
    }

    @AfterEach
    void tearDown() {
        if (securityUtilMock != null) {
            securityUtilMock.close();
        }
    }

    @Test
    void testGetAll() {
        PlanoEstudo plano1 = new PlanoEstudo(1, 1, "PT A1", 3, 3, Collections.emptyList(),
                LocalDate.now(), true, true);
        PlanoEstudo plano2 = new PlanoEstudo(1, 1, "PT A1", 3, 1, Collections.emptyList(),
                LocalDate.now(), true, false);

        when(planoEstudoRepository.findByIdUsuario(1)).thenReturn(Arrays.asList(plano1, plano2));

        var result = planoEstudoService.getAll();

        assertNotNull(result);
        assertEquals(2, result.getPlanos().size());
        assertEquals(50, result.getPorcentagemCompleta()); // 1/2 planos completos
    }

    @Test
    void testGetById() {
        PlanoEstudo plano = new PlanoEstudo(1,
                1,
                "PT A1",
                3,
                0,
                Collections.emptyList(),
                LocalDate.now(),
                true,
                false);
        when(planoEstudoRepository.findByIdPlanoEstudoAndIdUsuario(1, 1)).thenReturn(plano);

        PlanoEstudo result = planoEstudoService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getIdUsuario());
    }

    @Test
    void testGetCurrent() {
        PlanoEstudo plano = new PlanoEstudo(1, 1, "PT A1", 3, 0, Collections.emptyList(),
                LocalDate.now(), true, false);
        when(planoEstudoRepository.findByIdUsuarioAtivo(1)).thenReturn(plano);

        PlanoEstudo result = planoEstudoService.getCurrent();

        assertNotNull(result);
        assertTrue(result.getAtivo());
        verify(planoEstudoRepository, times(1)).findByIdUsuarioAtivo(1);
    }

    @Test
    void testFinalizarPlanoDiario() {
        PlanoEstudo plano = new PlanoEstudo(1, 1, "PT A1", 3, 0, Collections.emptyList(),
                LocalDate.now(), true, false);
        when(planoEstudoRepository.findByIdPlanoEstudoAndIdUsuario(1, 1)).thenReturn(plano);

        Boolean result = planoEstudoService.finalizarPlanoDiario(1);

        assertTrue(result);
        assertTrue(plano.getFinalizado());
        verify(planoEstudoRepository).save(plano);
    }

    @Test
    void testFinalizarExercicio() {
        PlanoEstudo plano = new PlanoEstudo(1, 1, "PT A1", 3, 0, Collections.emptyList(),
                LocalDate.now(), true, false);
        when(planoEstudoRepository.findByIdPlanoEstudoAndIdUsuario(1, 1)).thenReturn(plano);

        when(exercicioGramaticaComplementarRepository.findByUsuario(1, 1)).thenReturn(null);
        when(exercicioGramaticaOrdemRepository.findByUsuario(1, 1)).thenReturn(null);
        when(exercicioVocabularioParRepository.findByUsuario(1, 1)).thenReturn(null);

        planoEstudoService.finalizarExercicio(1, 1);

        assertEquals(1, plano.getQtExerciciosFinalizados());
        verify(planoEstudoRepository).save(plano);
    }
}
