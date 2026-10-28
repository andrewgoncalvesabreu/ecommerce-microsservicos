package com.ecommerce.ms_pedidos.dto;


import java.util.List;

public class PedidoRequest {

        private Long usuarioId;
        private List<Long> produtosIds;

        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

        public List<Long> getProdutosIds() { return produtosIds; }
        public void setProdutosIds(List<Long> produtosIds) { this.produtosIds = produtosIds; }
    }


