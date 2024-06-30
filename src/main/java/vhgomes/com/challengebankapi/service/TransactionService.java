package vhgomes.com.challengebankapi.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vhgomes.com.challengebankapi.dtos.TransactionCreatedEvent;
import vhgomes.com.challengebankapi.models.Transaction;
import vhgomes.com.challengebankapi.repository.TransactionRepository;
import vhgomes.com.challengebankapi.repository.UserRepository;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;


    public TransactionService(TransactionRepository transactionRepository, MongoTemplate mongoTemplate, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createTransaction(TransactionCreatedEvent event) {
        System.out.println(event);

        var userWhoSent = userRepository.findById(event.whoSent());
        var userWhoReceived = userRepository.findById(event.whoReceive());

        if (userWhoSent == null && userWhoReceived == null) {
            return ResponseEntity.badRequest().build();
        }

        if (verifyIfUserWhoSentHaveMoney(userWhoSent.get().getTotalSaldo(), event.amount()) == true) {
            var entity = new Transaction();
            entity.setAmount(event.amount());
            entity.setWhoSended(userWhoSent.get().getUserId());
            entity.setWhoReceived(userWhoReceived.get().getUserId());
            transactionRepository.save(entity);

            userWhoSent.get().setTotalSaldo(userWhoSent.get().getTotalSaldo().subtract(event.amount()));
            userWhoReceived.get().setTotalSaldo(userWhoReceived.get().getTotalSaldo().add(event.amount()));
            userRepository.save(userWhoSent.get());
            userRepository.save(userWhoReceived.get());

            return ResponseEntity.ok(entity);
        }

        return ResponseEntity.badRequest().build();
    }

    private boolean verifyIfUserWhoSentHaveMoney(BigDecimal saldo, BigDecimal amount) {
        return saldo.compareTo(amount) >= 0;
    }
}
