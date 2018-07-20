package com.pebank.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Customer implements Serializable{

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String address;

    private LocalDate birthday;

    public Customer() {
    }

    public Customer(String name, String address, LocalDate birthday) {
        this.name = name;
        this.address = address;
        this.birthday = birthday;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
