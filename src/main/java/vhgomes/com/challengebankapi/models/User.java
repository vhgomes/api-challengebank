package vhgomes.com.challengebankapi.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Document(collection = "tb_users")
public class User {
    @MongoId
    private Long userId;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal totalSaldo;

    @JsonCreator
    public User(@JsonProperty("userId") Long userId,
                @JsonProperty("totalSaldo") BigDecimal totalSaldo) {
        this.userId = userId;
        this.totalSaldo = totalSaldo;
    }


}
