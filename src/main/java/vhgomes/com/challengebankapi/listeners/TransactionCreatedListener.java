package vhgomes.com.challengebankapi.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import vhgomes.com.challengebankapi.dtos.TransactionCreatedEvent;
import vhgomes.com.challengebankapi.models.User;
import vhgomes.com.challengebankapi.repository.UserRepository;
import vhgomes.com.challengebankapi.service.TransactionService;

import static vhgomes.com.challengebankapi.config.RabbitMQConfig.TRANSACTION_CREATED_QUEUE;

@Component
public class TransactionCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(TransactionCreatedListener.class);
    private final TransactionService transactionService;
    private final UserRepository userRepository;

    public TransactionCreatedListener(TransactionService transactionService, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = TRANSACTION_CREATED_QUEUE)
    public void listen(Message<TransactionCreatedEvent> message) {
        try {
            TransactionCreatedEvent event = message.getPayload();

            User sender = userRepository.findById(event.whoSent())
                    .orElseThrow(() -> new IllegalArgumentException("Sender not found with id: " + event.whoSent()));
            User receiver = userRepository.findById(event.whoReceive())
                    .orElseThrow(() -> new IllegalArgumentException("Receiver not found with id: " + event.whoReceive()));

            logger.info("Message consumed: {}", message);

            transactionService.createTransaction(event, sender, receiver);
        } catch (IllegalArgumentException e) {
            logger.error("User not found during transaction creation: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while processing the message: {}", e.getMessage(), e);
        }
    }

}
