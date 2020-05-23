package com.vlad.atm;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DepositTransaction {
    private int accountNumber;
    @NotNull
    @Pattern(regexp = "[0-9]{4}")
    private String pin;
    @NotNull
    private List<Integer> bills;
}
