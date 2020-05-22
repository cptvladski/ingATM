package com.vlad.atm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public class ATMController {
    private final Logger logger = LoggerFactory.getLogger(ATMController.class);

    @GetMapping(path = "/withdraw")
    public ResponseEntity<?> withdraw
            (
                    @RequestParam int accountNumber,
                    @RequestParam String PIN,
                    @RequestParam int amount
            ){
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/deposit")
    public ResponseEntity<?> deposit
            (
                    @RequestBody DepositTransaction depositTransaction
            ){
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/consult")
    public ResponseEntity<?> consult
            (
                    @RequestParam String accountNumber,
                    @RequestParam String PIN
            ){
        return ResponseEntity.ok().build();
    }
}
