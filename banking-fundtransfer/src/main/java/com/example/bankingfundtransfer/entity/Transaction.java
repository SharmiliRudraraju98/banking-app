package com.example.bankingfundtransfer.entity;
import jakarta.persistence.*;


@Entity
@Table(name = "transactions")
public class Transaction {

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
    private Double amount;

    @Column(nullable = true)
    private  String comment;

    @Column(nullable = false)
    private  String status;

    @Column(nullable = false)
    private String date;


    public  Transaction(){}

    public Transaction(Account originAccount, Account destinationAccount,Double amount,String comment,String status, String date){
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount  = amount;
        this.comment = comment;
        this.status = status;
        this.date=date;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
