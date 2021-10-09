package com.netty.trpc.test.service;

import java.io.Serializable;

import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Person implements Serializable {
    private static final long serialVersionUID = -3475626311941868983L;
    @Tag(1)
    private String firstName;
    @Tag(2)
    private String lastName;
    @Tag(3)
    private String card;
    @Tag(4)
    private String address;

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
