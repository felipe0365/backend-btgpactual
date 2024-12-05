package br.com.felipemoreira.btgpactual.ordermsm.listener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record OrderItemEvent(
        @JsonProperty("produto") String product,
        @JsonProperty("quantidade") Integer quantity,
        @JsonProperty("preco") BigDecimal price
) {
}
