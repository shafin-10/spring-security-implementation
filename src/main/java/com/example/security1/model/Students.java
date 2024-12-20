package com.example.security1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Students {

    @Id
    private int id;
    private String name;

}
