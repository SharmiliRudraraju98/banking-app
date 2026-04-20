package com.example.bankingfundtransfer.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "origin_account_id", nullable = false)
    private Account originAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id", nullable = false)
    private Account destinationAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = true)
    private  String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(unique = true, nullable = false)
    private String idempotentKey;




    public  Transaction(){}

    public Transaction(Account originAccount, Account destinationAccount,BigDecimal amount,String comment,TransactionStatus status, LocalDateTime date, String idempotentKey){
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount  = amount;
        this.comment = comment;
        this.status = status;
        this.date=date;
        this.idempotentKey = idempotentKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public Account getOriginAccount() {
        return originAccount;
    }

    public void setOriginAccount(Account originAccount) {
        this.originAccount = originAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getIdempotentKey() {
        return idempotentKey;
    }

    public void setIdempotentKey(String idempotentKey) {
        this.idempotentKey = idempotentKey;
    }

    @Override
    public String toString(){
        return "Transaction{" +
                "id=" + id +
                ", originAccount='" + originAccount + '\'' +
                ", destinationAccount=" + destinationAccount +
                ", amount='" + amount +
                ", comment='" + comment + '\'' +
                ", status=" + status +
                ", date='" + date +
                ", idempotentKey='" + idempotentKey +
                '\'' +
                '}';
    }
}
