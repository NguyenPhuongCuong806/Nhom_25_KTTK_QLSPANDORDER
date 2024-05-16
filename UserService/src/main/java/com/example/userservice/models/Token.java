package com.example.userservice.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "token")
@Getter @Setter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 1000)
    String token;
    Date tokenExpDate;
}
