package com.example.easymrp.repository.auth;

import com.example.easymrp.model.auth.Role;
import com.example.easymrp.model.auth.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Set<Role> findAllByDeletableFalse();

    Set<Role> findAllByUsersIn(Set<User> users);
}
