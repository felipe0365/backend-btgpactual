package br.com.felipemoreira.btgpactual.ordermsm.controller.dto;

import br.com.felipemoreira.btgpactual.ordermsm.entity.OrderEntity;

import java.math.BigDecimal;

public record OrderResponse(Long orderId,
                            Long customerId,
                            BigDecimal total) {
    public static OrderResponse fromEntity(OrderEntity entity) {
        return new OrderResponse(entity.getOrderId(), entity.getCustomerId(), entity.getTotal());
    }
}
