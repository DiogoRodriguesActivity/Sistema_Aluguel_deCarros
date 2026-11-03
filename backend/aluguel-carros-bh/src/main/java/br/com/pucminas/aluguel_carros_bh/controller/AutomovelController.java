package br.com.pucminas.aluguel_carros_bh.controller;

import br.com.pucminas.aluguel_carros_bh.dto.AutomovelRequest;
import br.com.pucminas.aluguel_carros_bh.dto.AutomovelResponse;
import br.com.pucminas.aluguel_carros_bh.security.Acesso;
import br.com.pucminas.aluguel_carros_bh.service.AutomovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automoveis")
@RequiredArgsConstructor
public class AutomovelController {

    private final AutomovelService automovelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AutomovelResponse criar(@Valid @RequestBody AutomovelRequest req, @AuthenticationPrincipal Jwt jwt) {
        return automovelService.criar(req, Acesso.id(jwt));
    }

    @GetMapping
    public List<AutomovelResponse> listar() {
        return automovelService.listar();
    }

    @GetMapping("/{id}")
    public AutomovelResponse buscar(@PathVariable Long id) {
        return automovelService.buscar(id);
    }

    @PutMapping("/{id}")
    public AutomovelResponse atualizar(@PathVariable Long id, @Valid @RequestBody AutomovelRequest req) {
        return automovelService.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        automovelService.excluir(id);
    }
}