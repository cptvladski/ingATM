package com.vlad.atm;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BillUtil {
    private static final List<Integer> acceptedBills = Arrays.asList(new Integer[]{100,50,20,10,5,2,1});

    public Optional<List<Integer>> split(int amount){
        if(amount < 50)
            return Optional.empty();
        List<Integer> split = new ArrayList<>();
        for (Integer bill:acceptedBills){
            while (amount >= bill){
                split.add(bill);
                amount -= bill;
            }
        }
        return  Optional.of(split);
    }
    public boolean checkBills(List<Integer> bills){
        List<Integer> badBills = bills.stream().filter(integer -> !acceptedBills.contains(integer)).collect(Collectors.toList());
        return badBills.size() == 0;
    }
}
