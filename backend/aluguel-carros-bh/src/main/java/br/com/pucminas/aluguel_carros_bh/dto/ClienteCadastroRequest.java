package br.com.pucminas.aluguel_carros_bh.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ClienteCadastroRequest(
        @NotBlank @Email String login,
        @NotBlank @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, uma letra e um número") String senha,
        @NotBlank String rg,
        @NotBlank @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "CPF inválido") String cpf,
        @NotBlank String nome,
        @NotBlank String endereco,
        String profissao,
        @Size(max = 3, message = "No máximo 3 rendimentos") List<@Valid RendimentoDTO> rendimentos
) {
}