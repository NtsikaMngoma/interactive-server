package io.interactive.server.web.rest.controllers;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.interactive.server.domain.Account;
import io.interactive.server.domain.Transaction;
import io.interactive.server.repository.ITransactionRepository;
import io.interactive.server.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
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

public class TransactionController {

    private final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private static final String ENTITY_NAME = "Transaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ITransactionRepository _transactionRepository;

    @Autowired
    public TransactionController(ITransactionRepository transactionRepository) {
        _transactionRepository = transactionRepository;
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransactions(@Valid @RequestBody Transaction transaction) throws URISyntaxException {
        log.debug("REST request to save Account : {}", transaction);
        if (transaction.getCode() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an transaction code", ENTITY_NAME, "transaction exists");
        }
        Transaction result = _transactionRepository.save(transaction);
        return ResponseEntity.created(new URI("/v1/transactions/" + result.getCode()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getCode().toString()))
            .body(result);
    }

    @PutMapping("/transactions")
    public ResponseEntity<Transaction> updateTransactions(@Valid @RequestBody Transaction transaction) {
        log.debug("REST request to update Transaction : {}", transaction);
        if (transaction.getCode() == null) {
            throw new BadRequestAlertException("Invalid Account Number : {}", ENTITY_NAME, "account does not exist");
        }
        Transaction result = _transactionRepository.save(transaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transaction.getCode().toString()))
            .body(result);
    }

    @GetMapping("/transactions")
    public List<Transaction> getAllAccounts(){
        log.debug("REST request to get all Transactions");
        return _transactionRepository.findAll();
    }

    @GetMapping("/transactions/{code}")
    public ResponseEntity<Transaction> getAccount(@PathVariable Integer code) {
        log.debug("REST request to get Transaction : {}", code);
        Optional<Transaction> transaction = _transactionRepository.findById(code);
        return ResponseUtil.wrapOrNotFound(transaction);
    }

    @GetMapping("/transactions/acc_key")
    public List<Transaction> getAccountByNumber(String account_number) {
        log.debug("REST request to get Transaction by account_number : {}", account_number);
        return _transactionRepository.findByAccountNumber(account_number);
    }

    @GetMapping("/transactions/id_key")
    public List<Transaction> getTransactionsByIdNumber(String id_number){
        log.debug("REST request to get Transaction by id_number : {}", id_number);
        return _transactionRepository.findByIdNumber(id_number);
    }

    @GetMapping("/transactions/name")
    public List<Transaction> getTransactionByName(String name){
        log.debug("REST request to get Transaction by first_name : {}", name);
        return _transactionRepository.findByFirstName(name);
    }

    @GetMapping("/transactions/s_name")
    public List<Transaction> getTransactionBySurname(String surname){
        log.debug("REST request to get Transaction by surname : {}", surname);
        return _transactionRepository.findBySurname(surname);
    }
}
