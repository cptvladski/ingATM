package com.vlad.atm;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@RestController
@Validated
public class ATMController {
    private static class UnauthorizedException extends Exception{}

    private final Logger logger = LoggerFactory.getLogger(ATMController.class);
    private final AtomicInteger amountLeft = new AtomicInteger(10000);

    @Autowired
    private final BillProcessingTool billProcessingTool;

    @Autowired
    private final AccountRepository accountRepository;


    private Account authorizeAccount(int accountNumber, String PIN) throws UnauthorizedException{
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new UnauthorizedException();
        }
        if (!PIN.equals((account.getPIN()))) {
            logger.warn("Invalid access attempt to account number " + accountNumber);
            throw new UnauthorizedException();
        }
        return account;

    }

    @GetMapping(path = "/withdraw")
    public ResponseEntity<?> withdraw
            (
                    @RequestParam int accountNumber,
                    @RequestParam @Pattern(regexp = "[0-9]{4}")String PIN,
                    @RequestParam @Min(0) int amountWithdrawn
            ) throws UnauthorizedException {

        Account account = this.authorizeAccount(accountNumber,PIN);
        if(account.getAmount() < amountWithdrawn)
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Insufficient funds");

        return billProcessingTool.split(amountWithdrawn)
                .map(bills -> {
                    if(amountLeft.addAndGet(-amountWithdrawn) < 1000)
                        logger.warn("Less than 1000 money left in ATM");
                    logger.info(amountLeft + " money left in ATM");

                    account.setAmount(account.getAmount() - amountWithdrawn);
                    accountRepository.save(account);
                    return ResponseEntity.ok(bills.toString());
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid Amount (too low)"));




    }

    @PutMapping(path = "/deposit",consumes = "application/json")
    public ResponseEntity<?> deposit
            (
                    @RequestBody @Valid DepositTransaction depositTransaction
            ) throws UnauthorizedException {
        Account account = this.authorizeAccount(depositTransaction.getAccountNumber(),depositTransaction.getPin());
        return billProcessingTool.checkBills(depositTransaction.getBills())
                .map(amountDeposited -> {
                    account.setAmount(account.getAmount() + amountDeposited);
                    accountRepository.save(account);
                    amountLeft.addAndGet(amountDeposited);
                    return ResponseEntity.ok("" + amountDeposited);
                })
                .orElseGet(() -> {
                    logger.warn("Fake bill detected on deposit by accountNumber" + depositTransaction.getAccountNumber());
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid bills");
                });
    }

    @GetMapping(path = "/consult")
    public ResponseEntity<?> consult
            (
                    @RequestParam int accountNumber,
                    @RequestParam @Pattern(regexp = "[0-9]{4}") String PIN
            ) throws UnauthorizedException {
        Account account = this.authorizeAccount(accountNumber,PIN);
        return ResponseEntity.ok(account.getAmount());
    }



    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    String handleConstraintViolationException(ConstraintViolationException e) {
        return "not valid due to validation error: " + e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return "not valid due to validation error: " + e.getMessage();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    String handleUnauthorizedException(UnauthorizedException e) {
        return "Unauthorized";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                "missing parameter " + ex.getParameterType() + " " + ex.getParameterName()
        );
    }
}
