package com.satc.integrador.ai.usuario;

import com.satc.integrador.ai.enums.Plano;

public class UsuarioBuilder {

    private Integer id;
    private String username;
    private String email;
    private String nomeCompleto;
    private String password;
    private Plano plano;

    public static UsuarioBuilder builder() {
        return new UsuarioBuilder();
    }

    public UsuarioBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public UsuarioBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UsuarioBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UsuarioBuilder nomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
        return this;
    }

    public UsuarioBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UsuarioBuilder plano(Plano plano) {
        this.plano = plano;
        return this;
    }

    public Usuario build() {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setNomeCompleto(nomeCompleto);
        usuario.setPassword(password);
        usuario.setPlano(plano);
        return usuario;
    }
}
