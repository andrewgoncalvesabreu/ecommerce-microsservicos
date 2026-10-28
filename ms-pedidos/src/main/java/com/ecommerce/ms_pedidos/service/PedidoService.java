package com.ecommerce.ms_pedidos.service;

import com.ecommerce.ms_pedidos.dto.PedidoRequest;
import com.ecommerce.ms_pedidos.model.Pedido;
import com.ecommerce.ms_pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final RestClient usuariosClient;
    private final RestClient produtosClient;

    public PedidoService(
            PedidoRepository pedidoRepository,
            @Value("${services.usuarios.url:http://localhost:8081}") String usuariosUrl,
            @Value("${services.produtos.url:http://localhost:8082}") String produtosUrl) {
        this.pedidoRepository = pedidoRepository;
        this.usuariosClient = RestClient.builder().baseUrl(usuariosUrl).build();
        this.produtosClient = RestClient.builder().baseUrl(produtosUrl).build();
    }

    public Pedido criarPedido(PedidoRequest request) {
        validarRequest(request);
        validarUsuario(request.getUsuarioId());
        request.getProdutosIds().forEach(this::validarProduto);

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(request.getUsuarioId());
        pedido.setProdutosIds(request.getProdutosIds());
        pedido.setStatus("PENDENTE");
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    public void deletarPedido(Long id) {
        pedidoRepository.delete(buscarPorId(id));
    }

    private void validarRequest(PedidoRequest request) {
        if (request == null || request.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ID do usuário é obrigatório");
        }
        List<Long> produtosIds = request.getProdutosIds();
        if (produtosIds == null || produtosIds.isEmpty() || produtosIds.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe pelo menos um ID de produto válido");
        }
        if (request.getUsuarioId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ID do usuário deve ser positivo");
        }
    }

    private void validarUsuario(Long usuarioId) {
        validarReferencia(() -> usuariosClient.get()
                .uri("/usuarios/{id}", usuarioId)
                .retrieve()
                .toBodilessEntity(), "Usuário não encontrado: " + usuarioId);
    }

    private void validarProduto(Long produtoId) {
        validarReferencia(() -> produtosClient.get()
                .uri("/produtos/{id}", produtoId)
                .retrieve()
                .toBodilessEntity(), "Produto não encontrado: " + produtoId);
    }

    private void validarReferencia(Runnable chamada, String mensagemNaoEncontrado) {
        try {
            chamada.run();
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagemNaoEncontrado);
            }
            if (ex.getStatusCode().is4xxClientError()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Referência inválida no serviço relacionado");
            }
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Um serviço necessário está indisponível");
        } catch (org.springframework.web.client.ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível conectar a um serviço necessário");
        }
    }
}
