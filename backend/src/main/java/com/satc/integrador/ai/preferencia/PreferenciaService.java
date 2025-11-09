package com.satc.integrador.ai.preferencia;

import com.github.dozermapper.core.Mapper;
import com.satc.integrador.ai.preferencia.dto.PreferenciaGetDto;
import com.satc.integrador.ai.preferencia.dto.PreferenciaPostDto;
import com.satc.integrador.ai.usuario.Usuario;
import com.satc.integrador.ai.usuario.UsuarioService;
import com.satc.integrador.ai.usuario.dto.UsuarioGetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PreferenciaService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private PreferenciaRepository repo;

    @Autowired
    private UsuarioService usuarioService;

    public List<PreferenciaGetDto> getAllFromUser(Integer page, Integer count) {
        Pageable pageable = PageRequest.of(page, count);
        return repo.findByIdUsuario(usuarioService.getCurrentUserid(), pageable)
                .stream()
                .map(x -> mapper.map(x, PreferenciaGetDto.class))
                .toList();
    }

    public PreferenciaGetDto getOneFromUser(Integer id) {
        Preferencia p = repo.findByIdFromUser(id, usuarioService.getCurrentUserid());
        return mapper.map(p, PreferenciaGetDto.class);
    }

    public PreferenciaGetDto getCurrent() {
        Preferencia p = repo.findByIdUsuarioActive(usuarioService.getCurrentUserid());
        return mapper.map(p, PreferenciaGetDto.class);
    }

    public PreferenciaGetDto post(PreferenciaPostDto dto) {
        Integer idUser = usuarioService.getCurrentUserid();
        Preferencia pActive = repo.findByIdUsuarioActive(idUser);
        if (pActive != null){
            pActive.setAtivo(false);
            repo.save(pActive);
        }
        Preferencia preferencia = mapper.map(dto, Preferencia.class);
        preferencia.setIdUsuario(idUser);
        preferencia.setAtivo(true);
        preferencia = repo.save(preferencia);
        return mapper.map(preferencia, PreferenciaGetDto.class);
    }

    public PreferenciaGetDto patch(Integer id, PreferenciaPostDto dto) {
        Preferencia p = repo.findById(id).orElseThrow();
        Integer userId = usuarioService.getCurrentUserid();
        if (p.getIdUsuario() != userId){
            throw new AccessDeniedException("Usuario sem permissão");
        }
        p = mapper.map(dto, Preferencia.class);
        p.setId(id);
        p.setIdUsuario(userId);
        p = repo.save(p);
        return mapper.map(p, PreferenciaGetDto.class);
    }

    public PreferenciaGetDto delete(Integer id) {
        Preferencia p = repo.findById(id).orElseThrow();
        if (p.getIdUsuario() != usuarioService.getCurrentUserid()){
            throw new AccessDeniedException("Usuario sem permissão");
        }
        repo.delete(p);
        return mapper.map(p, PreferenciaGetDto.class);
    }

    public List<PreferenciaGetDto> getAllAdm(int page, int count) {
        Pageable pageable = PageRequest.of(page, count);
        return repo.findAll(pageable)
                .stream()
                .map(x -> mapper.map(x, PreferenciaGetDto.class))
                .collect(Collectors.toList());
    }

    public PreferenciaGetDto getOneAdm(Integer id) {
        Preferencia preferencia = repo.findById(id).orElseThrow();
        return mapper.map(preferencia, PreferenciaGetDto.class);
    }

    public PreferenciaGetDto postAdm(PreferenciaPostDto dto) {
        Preferencia preferencia = mapper.map(dto, Preferencia.class);
        preferencia = repo.save(preferencia);
        return mapper.map(preferencia, PreferenciaGetDto.class);
    }

    public PreferenciaGetDto patchAdm(Integer id, PreferenciaPostDto dto) {
        Preferencia preferencia = mapper.map(dto, Preferencia.class);
        preferencia.setId(id);
        preferencia = repo.save(preferencia);
        return mapper.map(preferencia, PreferenciaGetDto.class);
    }

    public void deleteAdm(Integer id) {
        repo.deleteById(id);
    }
}