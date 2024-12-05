package br.com.felipemoreira.btgpactual.ordermsm.service;

import br.com.felipemoreira.btgpactual.ordermsm.controller.dto.OrderResponse;
import br.com.felipemoreira.btgpactual.ordermsm.entity.OrderEntity;
import br.com.felipemoreira.btgpactual.ordermsm.entity.OrderItem;
import br.com.felipemoreira.btgpactual.ordermsm.listener.dto.OrderCreatedEvent;
import br.com.felipemoreira.btgpactual.ordermsm.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.bson.Document;
import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event) {
        var entity = new OrderEntity();
        entity.setOrderId(event.orderCode());
        entity.setCustomerId(event.clientCode());
        entity.setTotal(getTotal(event));
        entity.setItens(event.itens().stream()
                .map(i -> new OrderItem(i.product(), i.quantity(), i.price()))
                .toList());

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrderByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );
        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);
        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens().stream()
                .filter(i -> i.quantity() != null)
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
