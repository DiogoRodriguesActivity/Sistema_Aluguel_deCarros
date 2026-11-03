package br.com.pucminas.aluguel_carros_bh.controller;

import br.com.pucminas.aluguel_carros_bh.dto.*;
import br.com.pucminas.aluguel_carros_bh.security.Acesso;
import br.com.pucminas.aluguel_carros_bh.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@Valid @RequestBody ClienteCadastroRequest req) {
        return clienteService.criar(req);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        if (!Acesso.agente(jwt)) Acesso.exigirDono(jwt, id);
        return clienteService.buscar(id);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id,
                                     @Valid @RequestBody ClienteAtualizacaoRequest req,
                                     @AuthenticationPrincipal Jwt jwt) {
        Acesso.exigirDono(jwt, id);
        return clienteService.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Acesso.exigirDono(jwt, id);
        clienteService.excluir(id);
    }
}