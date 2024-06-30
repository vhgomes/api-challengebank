package vhgomes.com.challengebankapi.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import vhgomes.com.challengebankapi.dtos.TransactionCreatedEvent;
import vhgomes.com.challengebankapi.models.Transaction;
import vhgomes.com.challengebankapi.models.User;
import vhgomes.com.challengebankapi.repository.TransactionRepository;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MongoTemplate mongoTemplate;

    public TransactionService(TransactionRepository transactionRepository, MongoTemplate mongoTemplate) {
        this.transactionRepository = transactionRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void createTransaction(TransactionCreatedEvent event) {
        if (userHasMoneyToSend(event.whoSent(), event.amount()) == Boolean.TRUE) {
            var entity = new Transaction();
            entity.setAmount(event.amount());
            entity.setWhoSended(event.whoSent());
            entity.setWhoReceived(event.whoReceive());
            transactionRepository.save(entity);
        }
    }

    private Boolean userHasMoneyToSend(User usuario, BigDecimal amount) {
        return usuario.getTotalSaldo().compareTo(amount) >= 0;
    }
}
