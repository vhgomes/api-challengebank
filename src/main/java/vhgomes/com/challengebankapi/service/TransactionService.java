package vhgomes.com.challengebankapi.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vhgomes.com.challengebankapi.dtos.TransactionCreatedEvent;
import vhgomes.com.challengebankapi.models.Transaction;
import vhgomes.com.challengebankapi.models.User;
import vhgomes.com.challengebankapi.repository.TransactionRepository;
import vhgomes.com.challengebankapi.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createTransaction(TransactionCreatedEvent event) {
        System.out.println(event);

        Optional<User> userWhoSent = userRepository.findById(event.whoSent());
        Optional<User> userWhoReceived = userRepository.findById(event.whoReceive());

        if (userWhoSent.isEmpty() || userWhoReceived.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User sender = userWhoSent.get();
        User receiver = userWhoReceived.get();

        if (verifyIfUserWhoSentHaveMoney(sender.getTotalSaldo(), event.amount()) == true) {
            Transaction transaction = new Transaction();
            transaction.setAmount(event.amount());
            transaction.setWhoSended(String.valueOf(sender.getUserId()));
            transaction.setWhoReceived(String.valueOf(receiver.getUserId()));

            transactionRepository.save(transaction);

            updateUserBalances(sender, receiver, event.amount());

            return ResponseEntity.ok(transaction);
        }

        return ResponseEntity.badRequest().build();
    }

    private void updateUserBalances(User sender, User receiver, BigDecimal amount) {
        sender.setTotalSaldo(sender.getTotalSaldo().subtract(amount));
        receiver.setTotalSaldo(receiver.getTotalSaldo().add(amount));

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    private boolean verifyIfUserWhoSentHaveMoney(BigDecimal saldo, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(saldo) >= 0) {
            return true;
        }
        return false;
    }
}
