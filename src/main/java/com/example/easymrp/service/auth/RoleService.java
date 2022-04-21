package com.example.easymrp.service.auth;

import com.example.easymrp.exception.notfound.NotFoundException;
import com.example.easymrp.model.auth.Privilege;
import com.example.easymrp.model.auth.Role;
import com.example.easymrp.model.auth.User;
import com.example.easymrp.repository.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeService privilegeService;

    public Role find(Long id) {
        return roleRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(NotFoundException::new);
    }

    public Role getOrCreate(String name, boolean deletable, Set<String> privilegeNames) {
        Role role = roleRepository.findByName(name).orElse(null);
        if (role == null) {
            Set<Privilege> privileges = privilegeNames.stream().map(privilegeService::getOrCreate).collect(Collectors.toSet());
            role = new Role(name, deletable, privileges);
            role = roleRepository.save(role);
        }
        return role;
    }

    public Set<Role> listSystemRole() {
        return roleRepository.findAllByDeletableFalse();
    }

    public Set<Role> listAssigned(User user) {
        return roleRepository.findAllByUsersIn(Set.of(user));
    }

}
