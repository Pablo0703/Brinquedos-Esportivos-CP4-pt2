package com.fiap.brinquedos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrinquedoDTO {
    private Long id;
    private String nome;
    private String tipo;
    private String classificacao;
    private String tamanho;
    private Double preco;
}

