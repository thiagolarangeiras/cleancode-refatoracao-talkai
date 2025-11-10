package com.satc.integrador.ai.preferencia;

import com.satc.integrador.ai.planoestudo.PlanoEstudoController;
import com.satc.integrador.ai.planoestudo.PlanoEstudoService;
import com.satc.integrador.ai.preferencia.dto.PreferenciaGetDto;
import com.satc.integrador.ai.preferencia.dto.PreferenciaPostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PreferenciaControllerTest {

    @InjectMocks
    private PreferenciaController preferenciaController;

    @Mock
    private PreferenciaService preferenciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOneFromUser() {
        PreferenciaGetDto dto = new PreferenciaGetDto(1, 1, "Inglês",
                List.of("GRAMATICA", "VOCABULARIO"),
                List.of("Tema1", "Tema2"),
                "Médio", "Intermediário",
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                30, true);

        when(preferenciaService.getOneFromUser(1)).thenReturn(dto);

        ResponseEntity<Object> response = preferenciaController.getOneFromUser(1);

        assertEquals(dto, response.getBody());
        verify(preferenciaService, times(1)).getOneFromUser(1);
    }

    @Test
    void testGetCurrent() {
        PreferenciaGetDto dto = new PreferenciaGetDto(2, 1, "Espanhol",
                List.of("GRAMATICA"),
                List.of("Tema3"),
                "Fácil", "Básico",
                List.of(DayOfWeek.FRIDAY),
                20, true);

        when(preferenciaService.getCurrent()).thenReturn(dto);

        ResponseEntity<Object> response = preferenciaController.getCurrent();

        assertEquals(dto, response.getBody());
        verify(preferenciaService, times(1)).getCurrent();
    }

    @Test
    void testGetAllFromUser() {
        List<PreferenciaGetDto> lista = List.of(
                new PreferenciaGetDto(1, 1, "Inglês", List.of("GRAMATICA"), List.of("Tema1"), "Médio", "Intermediário", List.of(DayOfWeek.MONDAY), 30, true),
                new PreferenciaGetDto(2, 1, "Espanhol", List.of("VOCABULARIO"), List.of("Tema2"), "Fácil", "Básico", List.of(DayOfWeek.TUESDAY), 20, true)
        );

        when(preferenciaService.getAllFromUser(0, 10)).thenReturn(lista);

        ResponseEntity<Object> response = preferenciaController.getAllFromUser(0, 10);

        assertEquals(lista, response.getBody());
        verify(preferenciaService, times(1)).getAllFromUser(0, 10);
    }

    @Test
    void testPost() {
        PreferenciaPostDto postDto = new PreferenciaPostDto(1, "Inglês", List.of("GRAMATICA"), List.of("Tema1"), "Médio", "Intermediário", List.of(DayOfWeek.MONDAY), 30);
        PreferenciaGetDto resultDto = new PreferenciaGetDto(1, 1, "Inglês", List.of("GRAMATICA"), List.of("Tema1"), "Médio", "Intermediário", List.of(DayOfWeek.MONDAY), 30, true);

        when(preferenciaService.post(postDto)).thenReturn(resultDto);

        ResponseEntity<Object> response = preferenciaController.post(postDto);

        assertEquals(resultDto, response.getBody());
        verify(preferenciaService, times(1)).post(postDto);
    }

    @Test
    void testPatch() {
        PreferenciaPostDto patchDto = new PreferenciaPostDto(1, "Espanhol", List.of("VOCABULARIO"), List.of("Tema2"), "Fácil", "Básico", List.of(DayOfWeek.TUESDAY), 20);
        PreferenciaGetDto resultDto = new PreferenciaGetDto(1, 1, "Espanhol", List.of("VOCABULARIO"), List.of("Tema2"), "Fácil", "Básico", List.of(DayOfWeek.TUESDAY), 20, true);

        when(preferenciaService.patch(1, patchDto)).thenReturn(resultDto);

        ResponseEntity<Object> response = preferenciaController.patch(1, patchDto);

        assertEquals(resultDto, response.getBody());
        verify(preferenciaService, times(1)).patch(1, patchDto);
    }

    @Test
    void testDelete() {
        PreferenciaGetDto resultDto = new PreferenciaGetDto(1, 1, "Espanhol", List.of("VOCABULARIO"), List.of("Tema2"), "Fácil", "Básico", List.of(DayOfWeek.TUESDAY), 20, true);
        when(preferenciaService.delete(1)).thenReturn(resultDto);

        ResponseEntity<Object> response = preferenciaController.delete(1);

        assertEquals(200, response.getStatusCodeValue());
        verify(preferenciaService, times(1)).delete(1);
    }
}
