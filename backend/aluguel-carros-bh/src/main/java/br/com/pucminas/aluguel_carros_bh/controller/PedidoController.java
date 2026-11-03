package br.com.pucminas.aluguel_carros_bh.controller;

import br.com.pucminas.aluguel_carros_bh.dto.PedidoRequest;
import br.com.pucminas.aluguel_carros_bh.dto.PedidoResponse;
import br.com.pucminas.aluguel_carros_bh.security.Acesso;
import br.com.pucminas.aluguel_carros_bh.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(@Valid @RequestBody PedidoRequest req, @AuthenticationPrincipal Jwt jwt) {
        return pedidoService.criar(Acesso.id(jwt), req.automovelId());
    }

    @GetMapping("/meus")
    public List<PedidoResponse> meus(@AuthenticationPrincipal Jwt jwt) {
        return pedidoService.meus(Acesso.id(jwt));
    }

    @GetMapping
    public List<PedidoResponse> todos() {
        return pedidoService.todos();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return pedidoService.buscar(id, Acesso.id(jwt), Acesso.agente(jwt));
    }

    @PutMapping("/{id}")
    public PedidoResponse alterar(@PathVariable Long id, @Valid @RequestBody PedidoRequest req,
                                  @AuthenticationPrincipal Jwt jwt) {
        return pedidoService.alterar(id, Acesso.id(jwt), req.automovelId());
    }

    @PatchMapping("/{id}/cancelar")
    public PedidoResponse cancelar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return pedidoService.cancelar(id, Acesso.id(jwt));
    }
}