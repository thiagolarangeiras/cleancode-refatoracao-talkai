package com.satc.integrador.ai.planoestudo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanoEstudoRepository extends JpaRepository<PlanoEstudo, Integer> {

    @Query(value = "select * from plano_estudo where id_usuario = ?1", nativeQuery = true)
    List<PlanoEstudo> findByIdUsuario(Integer idUsuario);

    @Query(value = "select * from plano_estudo where id_usuario = ?1 and ativo = true LIMIT 1", nativeQuery = true)
    PlanoEstudo findByIdUsuarioAtivo(Integer idUsuario);

    @Query(value = "select * from plano_estudo where id = ?1 and id_usuario = ?2 LIMIT 1", nativeQuery = true)
    PlanoEstudo findByIdPlanoEstudoAndIdUsuario(Integer id, Integer idUsuario);
}