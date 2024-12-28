package com.eclectics.bank.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


//    @PrePersist
//    public void generateId(){
//        if(this.id == null){
//            this.id = UUID.randomUUID();
//        }
//    }
}
