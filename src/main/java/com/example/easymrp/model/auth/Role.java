package com.example.easymrp.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Roles")
@Table(name = "roles")
@Getter @Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    private boolean deletable;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_privileges")
    private Set<Privilege> privileges;

    public Role() {
    }

    public Role(String name, boolean deletable, Set<Privilege> privileges) {
        this.name = name;
        this.deletable = deletable;
        this.privileges = privileges;
    }
}
