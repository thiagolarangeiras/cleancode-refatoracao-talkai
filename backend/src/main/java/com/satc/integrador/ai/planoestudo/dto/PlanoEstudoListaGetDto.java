package com.satc.integrador.ai.planoestudo.dto;

import com.satc.integrador.ai.planoestudo.PlanoEstudo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanoEstudoListaGetDto {
    public Integer porcentagemCompleta;
    public List<PlanoEstudo> planos;
}