package br.com.felipemoreira.btgpactual.ordermsm.listener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OrderCreatedEvent(
        @JsonProperty("codigoPedido") Long orderCode,
        @JsonProperty("codigoCliente") Long clientCode,
        @JsonProperty("itens") List<OrderItemEvent> itens
) {
}
