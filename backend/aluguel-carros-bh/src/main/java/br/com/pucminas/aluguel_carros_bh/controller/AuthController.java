package br.com.pucminas.aluguel_carros_bh.controller;

import br.com.pucminas.aluguel_carros_bh.dto.LoginRequest;
import br.com.pucminas.aluguel_carros_bh.dto.LoginResponse;
import br.com.pucminas.aluguel_carros_bh.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}