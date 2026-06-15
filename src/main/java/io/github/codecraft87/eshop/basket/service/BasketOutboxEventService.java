package io.github.codecraft87.eshop.basket.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.basket.entity.BasketOutboxEvent;
import io.github.codecraft87.eshop.basket.enums.BasketEventStatus;
import io.github.codecraft87.eshop.basket.repository.BasketOutboxEventRepository;
import io.github.codecraft87.eshop.messaging.config.ExchangeConstants;
import io.github.codecraft87.eshop.messaging.config.RoutingKeyConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketOutboxEventService {

    private final BasketOutboxEventRepository outboxRepository;
   
    private final RabbitTemplate rabbitTemplate;
    
    @Transactional
    public Long saveBasketOutboxEvent(BasketOutboxEvent outboxEvent) {
        log.info("Saving basket out box event {} ",outboxEvent);
        return outboxRepository.save(outboxEvent).getId();
    }

    @Transactional
    public void publishPendingEvents() {
        log.info("Publishing pending events");
        List<BasketOutboxEvent> events = outboxRepository
                                .findByStatusInOrderByCreatedAt(
                                        List.of(
                                                BasketEventStatus.NEW,
                                                BasketEventStatus.FAILED));
        log.info("Found Pending events to publish {} ", events.size());
        
        for (BasketOutboxEvent event : events) {

            try {
                log.info("Publishing event {} ", event.getEventId().toString());
                rabbitTemplate.convertAndSend(
                        ExchangeConstants.ESHOP_EXCHANGE,
                        RoutingKeyConstants.BASKET_CHECKOUT,
                        event.getPayload());
                log.info("event published and marked as published");
                event.markPublished();

            } catch (Exception ex) {
                log.error("Event published failed ", ex);
                event.markFailed(ex.getMessage());
            }
        }
        if(events.size()>0) {
            outboxRepository.saveAll(events);
            log.info("events saved");
        }
    }
}
