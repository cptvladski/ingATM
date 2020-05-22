package com.vlad.atm;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Account {
    @Id
    private int accountNumber;
    private String PIN;
    private int amount;
}
