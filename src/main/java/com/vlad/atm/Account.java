package com.vlad.atm;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Setter
@Getter
public class Account {
    @Id
    private int accountNumber;
    private String PIN;
    private int amount;

}
