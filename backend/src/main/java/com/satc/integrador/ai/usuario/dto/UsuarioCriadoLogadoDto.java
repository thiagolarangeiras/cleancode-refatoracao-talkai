package com.satc.integrador.ai.usuario.dto;

import com.satc.integrador.ai.enums.Plano;

public record UsuarioCriadoLogadoDto(
        Integer id,
        String username,
        String email,
        String nomeCompleto,
        Plano plano,
        String jwtToken
) {}