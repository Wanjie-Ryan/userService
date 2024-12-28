package com.eclectics.bank.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Data

public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(nullable = false, name = "gender")
    private Gender gender;

    @Column(nullable = false, unique = true, name = "national_id")
    private String nationalId;

    @Column(nullable = false, unique = true, name = "phone")
    private String phone;

    @Column(unique = true, name = "email")
    private String email;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "employer_name")
    private String employerName;

    @Column(name = "monthly_income")
    private Float monthlyIncome;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // no need to fetch customer each and everytime, fetch only when needed.
    private List<Address> addresses;









}
