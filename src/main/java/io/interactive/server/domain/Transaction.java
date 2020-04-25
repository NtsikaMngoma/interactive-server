package io.interactive.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "Transactions", schema = "dbo", catalog = "interactive")
public class Transaction implements Serializable {
    private Integer code;
    private Date transactionDate;
    private Date captureDate;
    private BigDecimal amount;
    private String description;
    private Account accountsByAccountCode;

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
    @Column(name = "transaction_date")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Basic
    @FutureOrPresent
    @Column(name = "capture_date", updatable = false)
    public Date getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(Date captureDate) {
        this.captureDate = captureDate;
    }

    @Basic
    @DecimalMin("1.00")
    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return code.equals(that.code) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(captureDate, that.captureDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, transactionDate, captureDate, amount, description);
    }

    @ManyToOne
    @JsonProperty("account")
    @JoinColumn(name = "account_code", referencedColumnName = "code", nullable = false)
    public Account getAccountsByAccountCode() {
        return accountsByAccountCode;
    }

    public void setAccountsByAccountCode(Account accountsByAccountCode) {
        this.accountsByAccountCode = accountsByAccountCode;
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "code=" + getCode() +
            ", date_captured='" + getCaptureDate() + "'" +
            ", transaction_date='" + getTransactionDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
