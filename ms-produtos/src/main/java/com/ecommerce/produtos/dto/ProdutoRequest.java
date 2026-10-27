package com.ecommerce.produtos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank String nome,
        String descricao,
        @NotNull @DecimalMin(value = "0.01") BigDecimal preco,
        @NotNull @Min(0) Integer estoque
) {
}
