package io.interactive.server.web.rest.controllers;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.interactive.server.domain.Account;
import io.interactive.server.repository.IAccountRepository;
import io.interactive.server.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Transactional
@RestController
@RequestMapping("/v1")

public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    private static final String ENTITY_NAME = "Account";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IAccountRepository _accountRepository;

    @Autowired
    public AccountController(IAccountRepository accountRepository) {
        _accountRepository = accountRepository;
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) throws URISyntaxException {
        log.debug("REST request to save Account : {}", account);
        if (account.getCode() != null) {
            throw new BadRequestAlertException("A new account cannot already have an account number", ENTITY_NAME, "account exists");
        }
        Account result = _accountRepository.save(account);
        return ResponseEntity.created(new URI("/v1/accounts/" + result.getAccountNumber()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getCode().toString()))
            .body(result);
    }

    @PutMapping("/accounts")
    public ResponseEntity<Account> updateAccount(@Valid @RequestBody Account account) throws URISyntaxException {
        log.debug("REST request to update Account : {}", account);
        if (account.getCode() == null) {
            throw new BadRequestAlertException("Invalid Account Number : {}", ENTITY_NAME, "account does not exist");
        }
        Account result = _accountRepository.save(account);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, account.getCode().toString()))
            .body(result);
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts(){
        log.debug("REST request to get all Accounts");
        return _accountRepository.findAll();
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        log.debug("REST request to get Account : {}", accountNumber);
        Optional<Account> account = _accountRepository.findByAccountNumber(accountNumber);
        return ResponseUtil.wrapOrNotFound(account);
    }

    @GetMapping("/accounts/name")
    public List<Account> getAccountByFirstName(String name) {
        log.debug("REST request to get Account by Firstname Show Associated Code and Account Number");
        return _accountRepository.findByName(name);
    }

    @GetMapping("/accounts/s_name")
    public List<Account> getAccountBySurname(String surname) {
        log.debug("REST request to get Account by Surname Show Associated Code and Account Number");
        return _accountRepository.findBySurname(surname);
    }

    @GetMapping("/accounts/id_key")
    public List<Account> getAccountByIdNumber(String id_number) {
        log.debug("REST request to get Account by Surname Show Associated Code and Account Number");
        return _accountRepository.findByIdNumber(id_number);
    }

    @DeleteMapping("/close-account/{account_code}")
    public ResponseEntity<Void> closeBankAccount(@PathVariable Integer account_code){
        log.debug("REST request to close Account : {}", account_code);

        boolean validAccountBalance = _accountRepository.getOne(account_code).getOutstandingBalance().signum() == 0;

        if (validAccountBalance)
        {
            _accountRepository.deleteById(account_code);
        } else {
            throw new BadRequestAlertException("You still owe us money, account can only be closed when paid up!", ENTITY_NAME, "account in debt");
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, account_code.toString())).build();
    }
}
