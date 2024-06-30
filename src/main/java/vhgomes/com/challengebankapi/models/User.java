package vhgomes.com.challengebankapi.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tb_users")
public class User {
    @MongoId
    private Long userId;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal totalSaldo;

    private List<Transaction> allTransactions;

}
