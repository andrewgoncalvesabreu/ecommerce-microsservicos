package com.ecommerce.produtos.service;

import com.ecommerce.produtos.dto.ProdutoRequest;
import com.ecommerce.produtos.model.Produto;
import com.ecommerce.produtos.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto criar(ProdutoRequest request) {
        Produto produto = new Produto();
        copiarDados(request, produto);
        return repository.save(produto);
    }

    public List<Produto> listar() {
        return repository.findAll();
    }

    public Produto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    public Produto atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscar(id);
        copiarDados(request, produto);
        return repository.save(produto);
    }

    public void excluir(Long id) {
        repository.delete(buscar(id));
    }

    private void copiarDados(ProdutoRequest request, Produto produto) {
        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setEstoque(request.estoque());
    }
}
