package com.imckify.bakis.jackson.streaming;

public class Address {
    private String street;
    private String city;
    private int zipcode;

    public Address() {
    }

    public Address(String street, String city, int zipcode) {
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString(){
        return getStreet() + ", "+getCity()+", "+getZipcode();
    }
}