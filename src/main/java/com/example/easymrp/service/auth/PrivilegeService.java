package com.example.easymrp.service.auth;

import com.example.easymrp.exception.notfound.NotFoundException;
import com.example.easymrp.model.auth.Privilege;
import com.example.easymrp.repository.auth.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    
    private final PrivilegeRepository privilegeRepository;

    public Page<Privilege> list(Pageable pageable) {
        return privilegeRepository.findAll(pageable);
    }

    public Privilege find(Long id) {
        return privilegeRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Privilege findByName(String name) {
        return privilegeRepository.findByName(name).orElseThrow(NotFoundException::new);
    }

    public Privilege getOrCreate(String name) {
        Privilege privilege = privilegeRepository.findByName(name).orElse(null);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }
    
}
