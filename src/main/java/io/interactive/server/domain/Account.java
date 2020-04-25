package io.interactive.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

@Entity
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "Accounts", schema = "dbo", catalog = "interactive")
public class Account implements Serializable {
    private Integer code;
    private String accountNumber;
    private BigDecimal outstandingBalance;
    private Person personsByPersonCode;
    private Collection<Transaction> transactionsByCode;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code")
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Basic
    @NotNull
    @Column(name = "account_number", unique = true)
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Basic
    @NotNull
    @Column(name = "outstanding_balance", updatable = false)
    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account that = (Account) o;
        return code.equals(that.code) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(outstandingBalance, that.outstandingBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, accountNumber, outstandingBalance);
    }

    @ManyToOne
    @JsonProperty("persons")
    @JoinColumn(name = "person_code", referencedColumnName = "code", nullable = false)
    public Person getPersonsByPersonCode() {
        return personsByPersonCode;
    }

    public void setPersonsByPersonCode(Person personsByPersonCode) {
        this.personsByPersonCode = personsByPersonCode;
    }

    @JsonProperty("transactions")
    @JsonIgnore
    @OneToMany(mappedBy = "accountsByAccountCode", cascade = CascadeType.ALL)
    public Collection<Transaction> getTransactionsByCode() {
        return transactionsByCode;
    }

    public void setTransactionsByCode(Collection<Transaction> transactionsByCode) {
        this.transactionsByCode = transactionsByCode;
    }

    @Override
    public String toString() {
        return "Account{" +
            "id=" + getCode() +
            ", account_number='" + getAccountNumber() + "'" +
            ", outstanding_balance=" + getOutstandingBalance() +
            "}";
    }
}
