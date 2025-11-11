package com.satc.integrador.ai.usuario;

import com.satc.integrador.ai.enums.Plano;
import com.satc.integrador.ai.usuario.dto.UsuarioGetDto;
import com.satc.integrador.ai.usuario.dto.UsuarioPostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOne() {
        UsuarioGetDto usuario = new UsuarioGetDto(1, "user1", "user1@email.com", "User Um", Plano.NORMAL);
        when(usuarioService.getOne(1)).thenReturn(usuario);

        ResponseEntity<Object> response = usuarioController.getOne(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuario, response.getBody());
        verify(usuarioService).getOne(1);
    }

    @Test
    void testGetAllWithPagination() {
        List<UsuarioGetDto> usuarios = List.of(
                new UsuarioGetDto(1, "user1", "user1@email.com", "User Um", Plano.NORMAL)
        );
        when(usuarioService.getAll(0, 10)).thenReturn(usuarios);

        ResponseEntity<Object> response = usuarioController.getAll(0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarios, response.getBody());
        verify(usuarioService).getAll(0, 10);
    }

    @Test
    void testGetAllWithoutPagination() {
        UsuarioGetDto current = new UsuarioGetDto(2, "user2", "user2@email.com", "User Dois", Plano.NORMAL);
        when(usuarioService.getCurrent()).thenReturn(current);

        ResponseEntity<Object> response = usuarioController.getAll(null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(current, response.getBody());
        verify(usuarioService).getCurrent();
    }

    @Test
    void testPatch() {
        UsuarioPostDto dto = new UsuarioPostDto("NovoNome", "novo@email.com", "novaSenha123", "Nome completo", Plano.NORMAL);
        UsuarioGetDto updated = new UsuarioGetDto(1, "NovoNome", "novo@email.com", "Nome Atualizado", Plano.NORMAL);
        when(usuarioService.patch(1, dto)).thenReturn(updated);

        ResponseEntity<Object> response = usuarioController.patch(1, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updated, response.getBody());
        verify(usuarioService).patch(1, dto);
    }

    @Test
    void testDelete() {
        ResponseEntity<Object> response = usuarioController.delete(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
        verify(usuarioService).delete(1);
    }
}
