package br.com.pucminas.aluguel_carros_bh.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

public final class Acesso {

    private Acesso() {
    }

    public static Long id(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }

    public static boolean agente(Jwt jwt) {
        return "AGENTE".equals(jwt.getClaimAsString("papel"));
    }

    public static void exigirDono(Jwt jwt, Long idRecurso) {
        if (!idRecurso.equals(id(jwt))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }
    }
}