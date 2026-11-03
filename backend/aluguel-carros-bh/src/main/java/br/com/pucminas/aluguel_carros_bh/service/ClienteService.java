package br.com.pucminas.aluguel_carros_bh.service;

import br.com.pucminas.aluguel_carros_bh.dto.*;
import br.com.pucminas.aluguel_carros_bh.model.Cliente;
import br.com.pucminas.aluguel_carros_bh.model.Rendimento;
import br.com.pucminas.aluguel_carros_bh.repository.ClienteRepository;
import br.com.pucminas.aluguel_carros_bh.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ClienteResponse criar(ClienteCadastroRequest req) {
        String cpf = req.cpf().replaceAll("\\D", "");

        if (usuarioRepository.existsByLogin(req.login())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (clienteRepository.existsByCpf(cpf)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }

        Cliente cliente = new Cliente();
        cliente.setLogin(req.login());
        cliente.setSenha(passwordEncoder.encode(req.senha()));
        cliente.setCpf(cpf);
        preencherDados(cliente, req.rg(), req.nome(), req.endereco(), req.profissao(), req.rendimentos());

        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteAtualizacaoRequest req) {
        Cliente cliente = buscarEntidade(id);
        preencherDados(cliente, req.rg(), req.nome(), req.endereco(), req.profissao(), req.rendimentos());
        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void excluir(Long id) {
        clienteRepository.delete(buscarEntidade(id));
    }

    private Cliente buscarEntidade(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    private void preencherDados(Cliente c, String rg, String nome, String endereco,
                                String profissao, List<RendimentoDTO> rendimentos) {
        c.setRg(rg);
        c.setNome(nome);
        c.setEndereco(endereco);
        c.setProfissao(profissao);

        c.getRendimentos().clear();
        if (rendimentos != null) {
            for (RendimentoDTO dto : rendimentos) {
                Rendimento r = new Rendimento();
                r.setCliente(c);
                r.setEntidadeEmpregadora(dto.entidadeEmpregadora());
                r.setValor(dto.valor());
                c.getRendimentos().add(r);
            }
        }
    }

    private ClienteResponse toResponse(Cliente c) {
        List<RendimentoDTO> rendimentos = c.getRendimentos().stream()
                .map(r -> new RendimentoDTO(r.getEntidadeEmpregadora(), r.getValor()))
                .toList();
        return new ClienteResponse(c.getId(), c.getLogin(), c.getRg(), c.getCpf(),
                c.getNome(), c.getEndereco(), c.getProfissao(), rendimentos);
    }
}