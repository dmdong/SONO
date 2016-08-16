package com.manhdong.sono.model;

import java.io.Serializable;

/**
 * Created by Saphiro on 6/22/2016.
 */
public class Person implements Serializable {

    String personName;
    String relationship;
    String phoneNumber;
    String emailAddress;

    public Person() {
        personName = "SONO";
        relationship = null;
        phoneNumber = null;
        emailAddress = "default@sono.com";
    }

    public Person(String emailAddress, String personName, String phoneNumber, String relationship) {
        this.emailAddress = emailAddress;
        this.personName = personName;
        this.phoneNumber = phoneNumber;
        this.relationship = relationship;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return personName+"/"+relationship+"/"+phoneNumber+"/"+emailAddress;
    }
}

