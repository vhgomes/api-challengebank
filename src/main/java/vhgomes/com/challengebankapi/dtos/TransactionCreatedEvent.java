package vhgomes.com.challengebankapi.dtos;

import vhgomes.com.challengebankapi.models.User;

import java.math.BigDecimal;

public record TransactionCreatedEvent(BigDecimal amount, Long whoSent, Long whoReceive) {
}
