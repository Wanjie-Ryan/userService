package com.eclectics.bank.dto;

import com.eclectics.bank.domain.Address;
import com.eclectics.bank.domain.Gender;
import java.time.LocalDate;
import java.util.List;

public class UserDto {
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationalId;
    private String phone;
    private String email;
    private String occupation;
    private String employerName;
    private Float monthlyIncome;
    private List<Address> addresses;

    // Getters
    public String getFullName() {
        return fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getEmployerName() {
        return employerName;
    }

    public Float getMonthlyIncome() {
        return monthlyIncome;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}