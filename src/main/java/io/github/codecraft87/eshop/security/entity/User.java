package io.github.codecraft87.eshop.security.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    
    private boolean enabled = true;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
