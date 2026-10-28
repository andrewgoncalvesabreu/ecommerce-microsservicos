package com.ecommerce.ms_pedidos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @ElementCollection
    @CollectionTable(name = "pedido_produtos", joinColumns = @JoinColumn(name = "pedido_id"))
    @Column(name = "produto_id")
    private List<Long> produtosIds;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    private String status; // Ex: "PENDENTE", "PAGO", "CANCELADO"

    // Construtores, Getters e Setters
    public Pedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public List<Long> getProdutosIds() { return produtosIds; }
    public void setProdutosIds(List<Long> produtosIds) { this.produtosIds = produtosIds; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
