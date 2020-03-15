package com.example.firebaseexample;

public class Student {
    private String FirstName;
    private String FamilyName;
    private String City;
    private String Gender;
    private String Age;

    // This is default constructor.

    public Student() {

    }

    public String getFirstName() {
        return FirstName;
    }

    public String getFamilyName() {
        return FamilyName;
    }

    public String getCity() {
        return City;
    }

    public String getGender() {
        return Gender;
    }

    public String getAge() {
        return Age;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setFamilyName(String familyName) {
        FamilyName = familyName;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setAge(String age) {
        Age = age;
    }
}
