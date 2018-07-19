package com.pebank.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Account from;

    @ManyToOne
    private Account to;

    private long amount;

    private Date dateAndTime;

    public Transaction() {
    }

    public Transaction(Account from, Account to, long amount, Date dateAndTime) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.dateAndTime = dateAndTime;
    }

    public Account getFromCustomer() {
        return from;
    }

    public void setFromCustomer(Account from) {
        this.from = from;
    }

    public Account getToCustomer() {
        return to;
    }

    public void setToCustomer(Account to) {
        this.to = to;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
