package ru.sk.test.mt.data.entity;

import ru.sk.test.mt.data.entity.common.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
@Entity
@Table(name = "PERSON")
public class Person extends AbstractEntity<Long> {

    private String login;
    private String firstName;
    private String lastName;
    private Set<Account> accounts = new HashSet<>();;

    public Person() {
        //jpa
    }

    public Person(Long id) {
        super(id);
    }

    @Id
    @Column(name = "PERSON_ID", insertable = false, updatable = false)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "LOGIN")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Person " + lastName + " " + firstName + " has " + (accounts != null ? accounts.size() : 0) + " accounts";
    }
}
