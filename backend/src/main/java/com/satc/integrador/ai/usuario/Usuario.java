package com.satc.integrador.ai.usuario;

import com.satc.integrador.ai.enums.Plano;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "password")
    private String password;

    @Column(name = "plano")
    private Plano plano;
}