package com.satc.integrador.ai.usuario;

import com.satc.integrador.ai.auth.AuthService;
import com.satc.integrador.ai.auth.dto.RecoveryJwtTokenDto;
import com.satc.integrador.ai.enums.Plano;
import com.satc.integrador.ai.usuario.dto.UsuarioCriadoLogadoDto;
import com.satc.integrador.ai.usuario.dto.UsuarioGetDto;
import com.satc.integrador.ai.usuario.dto.UsuarioPostDto;
import com.satc.integrador.ai.auth.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthService authService;

    public Integer getCurrentUserid() {
        return usuarioRepository.findByUsername(SecurityUtil.getCurrentUserSubject()).orElseThrow().getId();
    }

    public Boolean checkIfAdminOrCurrentUser(){
        return true;
    }

    public Boolean checkIfAdminOrCurrentUser(Integer id){
        String userName = SecurityUtil.getCurrentUserSubject();
        Usuario u = usuarioRepository.findByUsername(userName).get();
        if (u.getPlano() == Plano.ADM) {
            return true;
        }
        if (u.getId() == id){
            return true;
        }
        return false;
    }

    public UsuarioGetDto postLogin(UsuarioPostDto dto) {
        Usuario usuario = Usuario.convertDtoToEntity(dto);
        usuario.setPlano(Plano.NORMAL);
        usuario = usuarioRepository.save(usuario);
        return Usuario.convertEntityToDto(usuario);
    }

    public UsuarioCriadoLogadoDto post(UsuarioPostDto dto) {
        if (!checkIfAdminOrCurrentUser()){
            return null;
        }
        Usuario usuario = Usuario.convertDtoToEntity(dto);
        usuario = usuarioRepository.save(usuario);
        RecoveryJwtTokenDto jwtToken = authService.authenticateUser(dto.username(), dto.password());
        return new UsuarioCriadoLogadoDto(usuario.getId(), usuario.getUsername(), usuario.getEmail(), usuario.getNomeCompleto(), usuario.getPlano(), jwtToken.token());
    }

    public UsuarioGetDto getCurrent() {
        String userName = SecurityUtil.getCurrentUserSubject();
        return usuarioRepository.findByUsername(userName)
                .map(Usuario::convertEntityToDto)
                .get();
    }

    public List<UsuarioGetDto> getAll(int page, int count) {
        if (!checkIfAdminOrCurrentUser()){
            return null;
        }
        Pageable pageable = PageRequest.of(page, count);
        return usuarioRepository.findAll(pageable).stream()
                .map(Usuario::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public UsuarioGetDto getOne(Integer id) {
        if (checkIfAdminOrCurrentUser(id)){
            return usuarioRepository.findById(id)
                    .map(Usuario::convertEntityToDto)
                    .orElse(null);
        }
        return null;
    }

    public UsuarioGetDto patch(Integer id, UsuarioPostDto dto) {
        if (!checkIfAdminOrCurrentUser(id)){
            return null;
        }
        Usuario usuario = Usuario.convertDtoToEntity(dto);
        usuario.setId(id);
        usuario = usuarioRepository.save(usuario);
        return Usuario.convertEntityToDto(usuario);
    }

    public void delete(Integer id) {
        if (!checkIfAdminOrCurrentUser(id)){
            return;
        }
        usuarioRepository.deleteById(id);
    }
}