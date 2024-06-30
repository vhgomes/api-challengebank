package vhgomes.com.challengebankapi.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import vhgomes.com.challengebankapi.dtos.TransactionCreatedEvent;
import vhgomes.com.challengebankapi.service.TransactionService;

import static vhgomes.com.challengebankapi.config.RabbitMQConfig.TRANSACTION_CREATED_QUEUE;

@Component
public class TransactionCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(TransactionCreatedListener.class);
    private final TransactionService transactionService;

    public TransactionCreatedListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RabbitListener(queues = TRANSACTION_CREATED_QUEUE)
    public void listen(Message<TransactionCreatedEvent> message) {
        logger.info("Message consumed: {}", message);
        transactionService.createTransaction(message.getPayload());
    }

}
