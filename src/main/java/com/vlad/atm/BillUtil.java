package com.vlad.atm;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BillUtil {
    public Optional<List<Integer>> split(int amount){
        if(amount < 50)
            return Optional.empty();
        return  Optional.of(new ArrayList<>());
    }
    public boolean checkBills(List<Integer> bills){
        return true;
    }
}
