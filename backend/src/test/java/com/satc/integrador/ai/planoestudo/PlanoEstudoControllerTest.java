package com.satc.integrador.ai.planoestudo;

import com.satc.integrador.ai.planoestudo.dto.PlanoEstudoListaGetDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PlanoEstudoControllerTest {

    @Mock
    private PlanoEstudoService planoEstudoService;

    @InjectMocks
    private PlanoEstudoController planoEstudoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateNewPlan() {
        PlanoEstudo plano = new PlanoEstudo(1, 1, "Plano Teste", 5, 0, List.of(), LocalDate.now(), true, false);
        when(planoEstudoService.handleNewPlan()).thenReturn(plano);

        ResponseEntity<PlanoEstudo> response = planoEstudoController.generateNewPlan();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plano, response.getBody());
        verify(planoEstudoService, times(1)).handleNewPlan();
    }

    @Test
    void testGetCurrent() {
        PlanoEstudo plano = new PlanoEstudo(1, 1, "Plano Atual", 5, 0, List.of(), LocalDate.now(), true, false);
        when(planoEstudoService.getCurrent()).thenReturn(plano);

        ResponseEntity<PlanoEstudo> response = planoEstudoController.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plano, response.getBody());
        verify(planoEstudoService, times(1)).getCurrent();
    }

    @Test
    void testGetById() {
        PlanoEstudo plano = new PlanoEstudo(1, 1, "Plano por Id", 5, 0, List.of(), LocalDate.now(), true, false);
        when(planoEstudoService.getById(1)).thenReturn(plano);

        ResponseEntity<PlanoEstudo> response = planoEstudoController.getByid(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plano, response.getBody());
        verify(planoEstudoService, times(1)).getById(1);
    }

    @Test
    void testGetAll() {
        PlanoEstudoListaGetDto dto = new PlanoEstudoListaGetDto();
        when(planoEstudoService.getAll()).thenReturn(dto);

        ResponseEntity<PlanoEstudoListaGetDto> response = planoEstudoController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(planoEstudoService, times(1)).getAll();
    }

    @Test
    void testFinalizarExercicio() {
        doNothing().when(planoEstudoService).finalizarExercicio(1, 10);

        ResponseEntity<ResponseEntity> response = planoEstudoController.finalizarExercicio(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(planoEstudoService, times(1)).finalizarExercicio(1, 10);
    }

    @Test
    void testFinalizarPlanoDiario() {
        when(planoEstudoService.finalizarPlanoDiario(1)).thenReturn(true);

        ResponseEntity<Boolean> response = planoEstudoController.finalizarPlanoDiario(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(planoEstudoService, times(1)).finalizarPlanoDiario(1);
    }
}
