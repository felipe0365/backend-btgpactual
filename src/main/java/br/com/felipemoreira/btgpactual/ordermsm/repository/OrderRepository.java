package br.com.felipemoreira.btgpactual.ordermsm.repository;

import br.com.felipemoreira.btgpactual.ordermsm.controller.dto.OrderResponse;
import br.com.felipemoreira.btgpactual.ordermsm.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
    Page<OrderEntity> findAllByCustomerId(Long customerId, PageRequest pageRequest);
}
