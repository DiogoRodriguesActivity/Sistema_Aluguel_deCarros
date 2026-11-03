package br.com.pucminas.aluguel_carros_bh.service;

import br.com.pucminas.aluguel_carros_bh.dto.LoginRequest;
import br.com.pucminas.aluguel_carros_bh.dto.LoginResponse;
import br.com.pucminas.aluguel_carros_bh.model.Agente;
import br.com.pucminas.aluguel_carros_bh.model.Cliente;
import br.com.pucminas.aluguel_carros_bh.model.Usuario;
import br.com.pucminas.aluguel_carros_bh.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiracao-minutos:120}")
    private long expiracaoMinutos;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        Usuario u = usuarioRepository.findByLogin(req.login())
                .filter(x -> passwordEncoder.matches(req.senha(), x.getSenha()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos"));

        String papel = (u instanceof Cliente) ? "CLIENTE" : "AGENTE";
        String nome = (u instanceof Cliente c) ? c.getNome() : ((Agente) u).getNome();

        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(u.getId()))
                .issuedAt(agora)
                .expiresAt(agora.plus(expiracaoMinutos, ChronoUnit.MINUTES))
                .claim("papel", papel)
                .build();

        String token = jwtEncoder
                .encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();

        return new LoginResponse(token, u.getId(), papel, nome);
    }
}