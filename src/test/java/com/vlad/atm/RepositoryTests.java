package com.vlad.atm;


import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.vlad.atm.data.Account;
import com.vlad.atm.data.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DatabaseSetup("createAccount.xml")
    void find_by_accunt_number_works_correctly() {
        Account account = accountRepository.findByAccountNumber(1234);
        assertThat(account).isNotNull();
    }

    @Test
    void save_followed_by_insert_works_correctly(){
        accountRepository.save(new Account(2345,"1111",300));
        Account account = accountRepository.findByAccountNumber(2345);
        assertThat(account).isNotNull();
    }

}