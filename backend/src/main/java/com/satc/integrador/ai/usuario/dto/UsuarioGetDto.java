package com.satc.integrador.ai.usuario.dto;

import com.satc.integrador.ai.enums.Plano;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioGetDto {
    private Integer id;
    private String username;
    private String email;
    private String nomeCompleto;
    private Plano plan;
}