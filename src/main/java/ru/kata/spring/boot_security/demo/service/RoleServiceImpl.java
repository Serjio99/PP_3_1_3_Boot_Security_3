package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.dao.RoleDao;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleRepo;

    @Autowired
    public RoleServiceImpl(RoleDao roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public void addRole(Role role) {
        roleRepo.save(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRole(Long id) {
        return roleRepo.getReferenceById(id);
    }

    @Override
    public void editRoleById(Role role) {
        roleRepo.save(role);
    }

    @Override
    public void removeRoleById(Long id) {
        roleRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getListOfRoles() {
        return roleRepo.findAll();
    }
}
