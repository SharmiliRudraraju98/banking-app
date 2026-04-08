package com.example.bankingfundtransfer.dto;

import java.math.BigDecimal;

public class AccountRequestDTO {
    private String accountNumber;
    private BigDecimal balance;
    private String accountType;
    private Long customerId;

    public AccountRequestDTO() {}

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}