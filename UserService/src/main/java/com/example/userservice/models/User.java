package com.example.userservice.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String password;
    String username;
    String email;
    LocalDate dob;

}
