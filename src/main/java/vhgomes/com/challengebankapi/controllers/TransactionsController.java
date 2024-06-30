package vhgomes.com.challengebankapi.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vhgomes.com.challengebankapi.config.RabbitMQConfig;
import vhgomes.com.challengebankapi.dtos.TransactionCreatedEvent;
import vhgomes.com.challengebankapi.service.TransactionService;

@Controller
public class TransactionsController {
    private final TransactionService transactionService;
    private final RabbitTemplate rabbitTemplate;

    public TransactionsController(TransactionService transactionService, RabbitTemplate rabbitTemplate) {
        this.transactionService = transactionService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionCreatedEvent transactionCreatedEvent) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TRANSACTION_CREATED_QUEUE, transactionCreatedEvent);
        return ResponseEntity.ok().build();
    }

}
