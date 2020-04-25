package io.interactive.server.repository;

import io.interactive.server.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("select n from Account n where n.personsByPersonCode.name = :name")
    List<Account> findByName(@Param("name") String name);

    @Query("select s from Account s where s.personsByPersonCode.surname = :surname")
    List<Account> findBySurname(@Param("surname") String surname);

    @Query("select i from Account i where i.personsByPersonCode.idNumber = :id_number")
    List<Account> findByIdNumber(@Param("id_number") String id_number);
}
