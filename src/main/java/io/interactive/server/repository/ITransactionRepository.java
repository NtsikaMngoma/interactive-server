package io.interactive.server.repository;

import io.interactive.server.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("select a from Transaction a where a.accountsByAccountCode.accountNumber = :account_number")
    List<Transaction> findByAccountNumber(@Param("account_number") String account_number);

    @Query("select a from Transaction a where a.accountsByAccountCode.personsByPersonCode.idNumber = :id_number")
    List<Transaction> findByIdNumber(@Param("id_number") String id_number);

    @Query("select a from Transaction a where a.accountsByAccountCode.personsByPersonCode.name = :name")
    List<Transaction> findByFirstName(@Param("name") String name);

    @Query("select a from Transaction a where a.accountsByAccountCode.personsByPersonCode.surname = :surname")
    List<Transaction> findBySurname(@Param("surname") String surname);
}
