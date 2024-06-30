package vhgomes.com.challengebankapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vhgomes.com.challengebankapi.models.User;

public interface UserRepository extends MongoRepository<User, Long> {
}
