package vhgomes.com.challengebankapi.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vhgomes.com.challengebankapi.dtos.TransactionCreatedEvent;
import vhgomes.com.challengebankapi.models.Transaction;
import vhgomes.com.challengebankapi.models.User;
import vhgomes.com.challengebankapi.repository.TransactionRepository;
import vhgomes.com.challengebankapi.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createTransaction(TransactionCreatedEvent event, User sender, User receiver) {
        if (!verifyIfUserWhoSentHaveMoney(sender.getTotalSaldo(), event.amount())) {
            return ResponseEntity.badRequest().build();
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(event.amount());
        transaction.setWhoSended(String.valueOf(sender.getUserId()));
        transaction.setWhoReceived(String.valueOf(receiver.getUserId()));
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        updateUserBalances(sender, receiver, event.amount());

        return ResponseEntity.ok(transaction);

    }

    private void updateUserBalances(User sender, User receiver, BigDecimal amount) {
        try {
            sender.setTotalSaldo(sender.getTotalSaldo().subtract(amount));
            receiver.setTotalSaldo(receiver.getTotalSaldo().add(amount));

            userRepository.save(sender);
            userRepository.save(receiver);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean verifyIfUserWhoSentHaveMoney(BigDecimal saldo, BigDecimal amount) {
        return saldo.compareTo(amount) >= 0;
    }
}
