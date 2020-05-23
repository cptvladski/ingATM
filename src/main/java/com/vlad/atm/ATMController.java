package com.vlad.atm;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@RestController
public class ATMController {
    private final Logger logger = LoggerFactory.getLogger(ATMController.class);
    private final AtomicInteger amountLeft = new AtomicInteger(10000);

    @Autowired
    private final BillUtil billUtil;
    @Autowired
    private final AccountRepository accountRepository;

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("missing parameter " + ex.getParameterType() + " " + ex.getParameterName());
    }

    @GetMapping(path = "/withdraw")
    public ResponseEntity<?> withdraw
            (
                    @RequestParam int accountNumber,
                    @RequestParam String PIN,
                    @RequestParam int amountWithdrawn
            ){
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No such account number");
        }
        if (!PIN.equals((account.getPIN()))) {
            logger.warn("Invalid access attempt to account number " + accountNumber);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong PIN");
        }

        if(account.getAmount() < amountWithdrawn)
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Insufficient funds");

        Optional<List<Integer>> bills = billUtil.split(amountWithdrawn);
        if(!bills.isPresent())
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid Amount");

        if(amountLeft.addAndGet(-amountWithdrawn) < 1000)
            logger.warn("Less than 1000 money left in ATM");
        logger.info(amountLeft + " money left in ATM");

        account.setAmount(account.getAmount() - amountWithdrawn);
        accountRepository.save(account);


        return ResponseEntity.ok(bills.get());
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
