package vhgomes.com.challengebankapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vhgomes.com.challengebankapi.models.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
