package com.satc.integrador.ai.usuario;

import com.github.dozermapper.core.Mapper;
import com.satc.integrador.ai.auth.AuthService;
import com.satc.integrador.ai.auth.SecurityUtil;
import com.satc.integrador.ai.auth.dto.RecoveryJwtTokenDto;
import com.satc.integrador.ai.auth.UserDetailsImpl;
import com.satc.integrador.ai.enums.Plano;
import com.satc.integrador.ai.usuario.dto.UsuarioCriadoLogadoDto;
import com.satc.integrador.ai.usuario.dto.UsuarioGetDto;
import com.satc.integrador.ai.usuario.dto.UsuarioPostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthService authService;

    @Mock
    private Mapper mapper;

    private Usuario usuarioBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioBase = new Usuario(1, "user", "user@email.com", "User", "senha", Plano.NORMAL);
    }

    private UserDetailsImpl mockUserDetails(Usuario usuario) {
        return new UserDetailsImpl(usuario);
    }

    @Test
    void testGetCurrentUserid() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentLoggedUser).thenReturn(mockUserDetails(usuarioBase));
            when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuarioBase));

            Integer id = usuarioService.getCurrentUserid();

            assertEquals(1, id);
            verify(usuarioRepository).findByUsername("user");
        }
    }

    @Test
    void testCheckIfAdminOrCurrentUser_Admin() {
        Usuario adm = new Usuario(2, "adm", "adm@mail.com", "ADM", "123", Plano.ADM);
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentLoggedUser).thenReturn(mockUserDetails(adm));
            when(usuarioRepository.findByUsername("adm")).thenReturn(Optional.of(adm));

            assertTrue(usuarioService.checkIfAdminOrCurrentUser(99));
        }
    }

    @Test
    void testCheckIfAdminOrCurrentUser_SameUser() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentLoggedUser).thenReturn(mockUserDetails(usuarioBase));
            when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuarioBase));

            assertTrue(usuarioService.checkIfAdminOrCurrentUser(1));
        }
    }

    @Test
    void testCheckIfAdminOrCurrentUser_NotAllowed() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentLoggedUser).thenReturn(mockUserDetails(usuarioBase));
            when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuarioBase));

            assertFalse(usuarioService.checkIfAdminOrCurrentUser(99));
        }
    }

    @Test
    void testPostLogin() {
        UsuarioPostDto dto = new UsuarioPostDto("user", "email", "senha", "Nome", Plano.NORMAL);
        Usuario mapped = new Usuario();
        when(mapper.map(dto, Usuario.class)).thenReturn(mapped);
        Usuario saved = new Usuario(1, "user", "email", "Nome", "hashed", Plano.NORMAL);
        when(usuarioRepository.save(any())).thenReturn(saved);
        UsuarioGetDto getDto = new UsuarioGetDto(1, "user", "email", "Nome", Plano.NORMAL);
        when(mapper.map(saved, UsuarioGetDto.class)).thenReturn(getDto);

        UsuarioGetDto result = usuarioService.postLogin(dto);

        assertEquals(getDto, result);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testPost() {
        UsuarioPostDto dto = new UsuarioPostDto("user", "email", "senha", "Nome", Plano.NORMAL);
        Usuario mapped = new Usuario();
        when(mapper.map(dto, Usuario.class)).thenReturn(mapped);
        Usuario saved = new Usuario(1, "user", "email", "Nome", "hashed", Plano.NORMAL);
        when(usuarioRepository.save(any())).thenReturn(saved);
        when(authService.authenticateUser("user", "senha")).thenReturn(new RecoveryJwtTokenDto("jwtToken"));

        UsuarioCriadoLogadoDto result = usuarioService.post(dto);

        assertNotNull(result);
        assertEquals("jwtToken", result.getJwtToken());
    }

    @Test
    void testGetCurrent() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentLoggedUser).thenReturn(mockUserDetails(usuarioBase));
            when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuarioBase));
            UsuarioGetDto dto = new UsuarioGetDto(1, "user", "user@email.com", "User", Plano.NORMAL);
            when(mapper.map(usuarioBase, UsuarioGetDto.class)).thenReturn(dto);

            UsuarioGetDto result = usuarioService.getCurrent();

            assertEquals(dto, result);
        }
    }

    @Test
    void testGetAll_AdminUser() {
        Usuario adm = new Usuario(2, "adm", "adm@mail.com", "ADM", "123", Plano.ADM);
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentLoggedUser).thenReturn(mockUserDetails(adm));
            when(usuarioRepository.findByUsername("adm")).thenReturn(Optional.of(adm));

            when(usuarioRepository.findAll(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(usuarioBase)));
            UsuarioGetDto dto = new UsuarioGetDto(1, "user", "user@email.com", "User", Plano.NORMAL);
            when(mapper.map(usuarioBase, UsuarioGetDto.class)).thenReturn(dto);

            List<UsuarioGetDto> result = usuarioService.getAll(0, 10);

            assertEquals(1, result.size());
            assertEquals(dto, result.get(0));
        }
    }

    @Test
    void testGetOne_AdminAccess() {
        Usuario adm = new Usuario(2, "adm", "adm@mail.com", "ADM", "123", Plano.ADM);
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentLoggedUser).thenReturn(mockUserDetails(adm));
            when(usuarioRepository.findByUsername("adm")).thenReturn(Optional.of(adm));
            when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioBase));
            UsuarioGetDto dto = new UsuarioGetDto(1, "user", "user@email.com", "User", Plano.NORMAL);
            when(mapper.map(usuarioBase, UsuarioGetDto.class)).thenReturn(dto);

            UsuarioGetDto result = usuarioService.getOne(1);

            assertEquals(dto, result);
        }
    }

    @Test
    void testPatch_Success() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentLoggedUser).thenReturn(mockUserDetails(usuarioBase));
            when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuarioBase));

            UsuarioPostDto dto = new UsuarioPostDto("user", "email", "senha", "Nome", Plano.NORMAL);
            Usuario mapped = new Usuario();
            when(mapper.map(dto, Usuario.class)).thenReturn(mapped);
            Usuario saved = new Usuario(1, "user", "email", "Nome", "senha", Plano.NORMAL);
            when(usuarioRepository.save(mapped)).thenReturn(saved);
            UsuarioGetDto expected = new UsuarioGetDto(1, "user", "email", "Nome", Plano.NORMAL);
            when(mapper.map(saved, UsuarioGetDto.class)).thenReturn(expected);

            UsuarioGetDto result = usuarioService.patch(1, dto);

            assertEquals(expected, result);
        }
    }

    @Test
    void testDelete_Success() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentLoggedUser).thenReturn(mockUserDetails(usuarioBase));
            when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuarioBase));

            usuarioService.delete(1);

            verify(usuarioRepository).deleteById(1);
        }
    }
}
