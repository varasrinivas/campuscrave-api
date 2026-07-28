package com.campuscrave.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roll_number", nullable = false, unique = true)
    private String rollNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    /** Where the order gets collected. Block C is the canteen window. */
    @Column(name = "hostel_block")
    private String hostelBlock;

    protected Student() {
    }

    public Long getId() {
        return id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getHostelBlock() {
        return hostelBlock;
    }
}
