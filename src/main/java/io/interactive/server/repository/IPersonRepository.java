package io.interactive.server.repository;

import io.interactive.server.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByIdNumber(String idNumber);

    @Query("select p from Person p where p.accountByCode = :account_number")
    List<Person> findPersonByAccountNumber(@Param("account_number") String account_number);

    @Query("select s from Person s where s.name = :name")
    List<Person> findByName(@Param("name") String name);

    @Query("select s from Person s where s.surname = :surname")
    List<Person> findBySurname(@Param("surname") String surname);

    @Query("select i from Person i where i.idNumber = :id_number")
    Page<Person> findAllByIdNumberNot(Pageable pageable, String id_number);

//    @Query("select A.outstandingBalance from Person p join Account A on p.code = A.personsByPersonCode.code where A.outstandingBalance = 0 and A.personsByPersonCode.code = :person_code")
//    BigDecimal findPaidAccount(@Param("person_code") Integer person_code);

    @Query(value = "delete p from Persons p join Accounts A on p.code = A.person_code where A.outstanding_balance = 0 and p.code = :code", nativeQuery = true)
    void closePaidAccount(@Param("code") Integer code);
}
