package com.ecommerce.usuarios.service;

import com.ecommerce.usuarios.dto.UsuarioRequest;
import com.ecommerce.usuarios.model.Usuario;
import com.ecommerce.usuarios.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario criar(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        copiarDados(request, usuario);
        return repository.save(usuario);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public Usuario atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscar(id);
        copiarDados(request, usuario);
        return repository.save(usuario);
    }

    public void excluir(Long id) {
        repository.delete(buscar(id));
    }

    private void copiarDados(UsuarioRequest request, Usuario usuario) {
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha());
        usuario.setEndereco(request.endereco());
    }
}
