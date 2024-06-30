package vhgomes.com.challengebankapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tb_transactions")
public class Transaction {

    @MongoId
    private Long transactionId;

    private BigDecimal amount;

    private User whoSended;

    private User whoReceived;

    @CreatedDate
    private LocalDateTime createdAt;
}
