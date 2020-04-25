package io.interactive.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "Persons", schema = "dbo", catalog = "interactive")
public class Person implements Serializable {
    private Integer code;
    private String name;
    private String surname;
    private String idNumber;
    private Collection<Account> AccountByCode;

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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "id_number", unique = true)
    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person that = (Person) o;
        return code.equals(that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(idNumber, that.idNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, surname, idNumber);
    }

    @OneToMany(mappedBy = "personsByPersonCode", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public Collection<Account> getAccountByCode() {
        return AccountByCode;
    }

    public void setAccountByCode(Collection<Account> AccountByCode) {
        this.AccountByCode = AccountByCode;
    }

    @Override
    public String toString() {
        return "Person{" +
            "first_name='" + name + '\'' +
            "surname='" + surname + '\'' +
            "id_number='" + idNumber + '\'' +
            "}";
    }
}
