package com.satc.integrador.ai.preferencia;

import com.github.dozermapper.core.Mapper;
import com.satc.integrador.ai.preferencia.dto.PreferenciaGetDto;
import com.satc.integrador.ai.preferencia.dto.PreferenciaPostDto;
import com.satc.integrador.ai.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PreferenciaServiceTest {

    @InjectMocks
    private PreferenciaService preferenciaService;

    @Mock
    private PreferenciaRepository repo;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOneFromUser() {
        when(usuarioService.getCurrentUserid()).thenReturn(1);
        Preferencia pref = new Preferencia();
        pref.setId(1);
        when(repo.findByIdFromUser(1, 1)).thenReturn(pref);
        PreferenciaGetDto dto = new PreferenciaGetDto(1,1,"Inglês",List.of(),List.of(),"Médio","Intermediário",List.of(DayOfWeek.MONDAY),30,true);
        when(mapper.map(pref, PreferenciaGetDto.class)).thenReturn(dto);

        PreferenciaGetDto result = preferenciaService.getOneFromUser(1);

        assertEquals(dto, result);
    }

    @Test
    void testGetCurrent() {
        when(usuarioService.getCurrentUserid()).thenReturn(1);
        Preferencia pref = new Preferencia();
        pref.setId(1);
        when(repo.findByIdUsuarioActive(1)).thenReturn(pref);
        PreferenciaGetDto dto = new PreferenciaGetDto(1,1,"Inglês",List.of(),List.of(),"Médio","Intermediário",List.of(DayOfWeek.MONDAY),30,true);
        when(mapper.map(pref, PreferenciaGetDto.class)).thenReturn(dto);

        PreferenciaGetDto result = preferenciaService.getCurrent();

        assertEquals(dto, result);
    }

    @Test
    void testPostDeativaAnterior() {
        when(usuarioService.getCurrentUserid()).thenReturn(1);
        Preferencia pActive = new Preferencia();
        pActive.setAtivo(true);
        when(repo.findByIdUsuarioActive(1)).thenReturn(pActive);

        PreferenciaPostDto postDto = new PreferenciaPostDto(1,"Inglês",List.of(),List.of(),"Médio","Intermediário",List.of(DayOfWeek.MONDAY),30);
        Preferencia newPref = new Preferencia();
        newPref.setId(2);
        when(mapper.map(postDto, Preferencia.class)).thenReturn(newPref);
        PreferenciaGetDto dto = new PreferenciaGetDto(2,1,"Inglês",List.of(),List.of(),"Médio","Intermediário",List.of(DayOfWeek.MONDAY),30,true);
        when(mapper.map(newPref, PreferenciaGetDto.class)).thenReturn(dto);
        when(repo.save(any())).thenReturn(newPref);

        PreferenciaGetDto result = preferenciaService.post(postDto);

        assertEquals(dto, result);
        assertFalse(pActive.getAtivo());
        verify(repo).save(pActive);
        verify(repo).save(newPref);
    }

    @Test
    void testPatchSuccess() {
        when(usuarioService.getCurrentUserid()).thenReturn(1);
        Preferencia existing = new Preferencia();
        existing.setId(1);
        existing.setIdUsuario(1);
        when(repo.findById(1)).thenReturn(Optional.of(existing));

        PreferenciaPostDto patchDto = new PreferenciaPostDto(1,"Espanhol",List.of(),List.of(),"Fácil","Básico",List.of(DayOfWeek.TUESDAY),20);
        Preferencia mapped = new Preferencia();
        when(mapper.map(patchDto, Preferencia.class)).thenReturn(mapped);
        Preferencia saved = new Preferencia();
        saved.setId(1);
        when(repo.save(mapped)).thenReturn(saved);
        PreferenciaGetDto dto = new PreferenciaGetDto(1,1,"Espanhol",List.of(),List.of(),"Fácil","Básico",List.of(DayOfWeek.TUESDAY),20,true);
        when(mapper.map(saved, PreferenciaGetDto.class)).thenReturn(dto);

        PreferenciaGetDto result = preferenciaService.patch(1, patchDto);

        assertEquals(dto, result);
    }

    @Test
    void testPatchAccessDenied() {
        when(usuarioService.getCurrentUserid()).thenReturn(1);
        Preferencia existing = new Preferencia();
        existing.setId(1);
        existing.setIdUsuario(2); // outro usuário
        when(repo.findById(1)).thenReturn(Optional.of(existing));

        PreferenciaPostDto patchDto = new PreferenciaPostDto(1,"Espanhol",List.of(),List.of(),"Fácil","Básico",List.of(DayOfWeek.TUESDAY),20);

        assertThrows(AccessDeniedException.class, () -> preferenciaService.patch(1, patchDto));
    }

    @Test
    void testDeleteSuccess() {
        when(usuarioService.getCurrentUserid()).thenReturn(1);
        Preferencia existing = new Preferencia();
        existing.setId(1);
        existing.setIdUsuario(1);
        when(repo.findById(1)).thenReturn(Optional.of(existing));
        PreferenciaGetDto dto = new PreferenciaGetDto(1,1,"Inglês",List.of(),List.of(),"Médio","Intermediário",List.of(DayOfWeek.MONDAY),30,true);
        when(mapper.map(existing, PreferenciaGetDto.class)).thenReturn(dto);

        PreferenciaGetDto result = preferenciaService.delete(1);

        assertEquals(dto, result);
        verify(repo).delete(existing);
    }

    @Test
    void testDeleteAccessDenied() {
        when(usuarioService.getCurrentUserid()).thenReturn(1);
        Preferencia existing = new Preferencia();
        existing.setId(1);
        existing.setIdUsuario(2);
        when(repo.findById(1)).thenReturn(Optional.of(existing));

        assertThrows(AccessDeniedException.class, () -> preferenciaService.delete(1));
    }
}
