package io.github.codecraft87.eshop.basket.messaging.outbox;

import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketOutboxService {

  private final BasketOutboxRepository outboxRepository;

  @Transactional
  public Long saveBasketOutboxEvent(BasketOutboxMessage outboxEvent) {
    log.info("Saving basket out box event {} ", outboxEvent);
    return outboxRepository.save(outboxEvent).getId();
  }

  @Transactional(readOnly = true)
  public List<BasketOutboxMessage> getPendingEvents() {
    return outboxRepository.findByStatusInOrderByCreatedAt(
        List.of(BasketOutboxEventStatus.NEW, BasketOutboxEventStatus.FAILED));
  }

  @Transactional
  public void save(List<BasketOutboxMessage> events) {
    outboxRepository.saveAll(events);
  }
}
