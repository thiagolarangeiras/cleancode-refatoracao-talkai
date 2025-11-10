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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.github.dozermapper.core.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private Mapper mapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthService authService;

    public Integer getCurrentUserid() {
        return usuarioRepository.findByUsername(SecurityUtil.getCurrentLoggedUser().getUsername()).orElseThrow().getId();
    }

    public Boolean checkIfAdminOrCurrentUser(){
        return true;
    }

    public Boolean checkIfAdminOrCurrentUser(Integer id){
        String userName = SecurityUtil.getCurrentLoggedUser().getUsername();
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
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setPlano(Plano.NORMAL);
        usuario.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        usuario = usuarioRepository.save(usuario);
        return mapper.map(usuario, UsuarioGetDto.class);
    }

    public UsuarioCriadoLogadoDto post(UsuarioPostDto dto) {
        if (!checkIfAdminOrCurrentUser()){
            return null;
        }
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        usuario = usuarioRepository.save(usuario);
        RecoveryJwtTokenDto jwtToken = authService.authenticateUser(dto.getUsername(), dto.getPassword());
        return new UsuarioCriadoLogadoDto(usuario.getId(), usuario.getUsername(), usuario.getEmail(), usuario.getNomeCompleto(), usuario.getPlano(), jwtToken.token());
    }

    public UsuarioGetDto getCurrent() {
        String userName = SecurityUtil.getCurrentLoggedUser().getUsername();
        return usuarioRepository.findByUsername(userName)
                .map(x -> mapper.map(x, UsuarioGetDto.class))
                .get();
    }

    public List<UsuarioGetDto> getAll(int page, int count) {
        if (!checkIfAdminOrCurrentUser()){
            return null;
        }
        Pageable pageable = PageRequest.of(page, count);
        return usuarioRepository.findAll(pageable).stream()
                .map(x -> mapper.map(x, UsuarioGetDto.class))
                .collect(Collectors.toList());
    }

    public UsuarioGetDto getOne(Integer id) {
        if (checkIfAdminOrCurrentUser(id)){
            return usuarioRepository.findById(id)
                    .map(x -> mapper.map(x, UsuarioGetDto.class))
                    .orElse(null);
        }
        return null;
    }

    public UsuarioGetDto patch(Integer id, UsuarioPostDto dto) {
        if (!checkIfAdminOrCurrentUser(id)){
            return null;
        }
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setId(id);
        usuario = usuarioRepository.save(usuario);
        return mapper.map(usuario, UsuarioGetDto.class);
    }

    public void delete(Integer id) {
        if (!checkIfAdminOrCurrentUser(id)){
            return;
        }
        usuarioRepository.deleteById(id);
    }
}