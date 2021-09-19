package com.netty.trpc.test.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Person implements Serializable {
    private static final long serialVersionUID = -3475626311941868983L;
    private String firstName;
    private String lastName;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        return this.firstName.hashCode() ^ this.lastName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) return false;
        Person p = (Person) obj;
        return this.firstName.equals(p.firstName) && this.lastName.equals(p.lastName);
    }
}
