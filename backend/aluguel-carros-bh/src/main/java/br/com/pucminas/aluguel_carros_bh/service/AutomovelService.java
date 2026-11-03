package br.com.pucminas.aluguel_carros_bh.service;

import br.com.pucminas.aluguel_carros_bh.dto.AutomovelRequest;
import br.com.pucminas.aluguel_carros_bh.dto.AutomovelResponse;
import br.com.pucminas.aluguel_carros_bh.model.Automovel;
import br.com.pucminas.aluguel_carros_bh.repository.AutomovelRepository;
import br.com.pucminas.aluguel_carros_bh.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutomovelService {

    private final AutomovelRepository automovelRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public AutomovelResponse criar(AutomovelRequest req, Long proprietarioId) {
        if (automovelRepository.existsByPlaca(req.placa()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Placa já cadastrada");
        if (automovelRepository.existsByMatricula(req.matricula()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula já cadastrada");

        Automovel a = new Automovel();
        preencher(a, req);
        a.setProprietario(usuarioRepository.getReferenceById(proprietarioId));
        return AutomovelResponse.de(automovelRepository.save(a));
    }

    @Transactional(readOnly = true)
    public List<AutomovelResponse> listar() {
        return automovelRepository.findAll().stream().map(AutomovelResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public AutomovelResponse buscar(Long id) {
        return AutomovelResponse.de(buscarEntidade(id));
    }

    @Transactional
    public AutomovelResponse atualizar(Long id, AutomovelRequest req) {
        Automovel a = buscarEntidade(id);
        if (!a.getPlaca().equals(req.placa()) && automovelRepository.existsByPlaca(req.placa()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Placa já cadastrada");
        if (!a.getMatricula().equals(req.matricula()) && automovelRepository.existsByMatricula(req.matricula()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula já cadastrada");
        preencher(a, req);
        return AutomovelResponse.de(automovelRepository.save(a));
    }

    @Transactional
    public void excluir(Long id) {
        Automovel a = buscarEntidade(id);
        try {
            automovelRepository.delete(a);
            automovelRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Automóvel possui pedidos e não pode ser excluído");
        }
    }

    private Automovel buscarEntidade(Long id) {
        return automovelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Automóvel não encontrado"));
    }

    private void preencher(Automovel a, AutomovelRequest req) {
        a.setMatricula(req.matricula());
        a.setAno(req.ano());
        a.setMarca(req.marca());
        a.setModelo(req.modelo());
        a.setPlaca(req.placa());
    }
}