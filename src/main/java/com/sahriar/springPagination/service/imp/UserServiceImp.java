package com.sahriar.springPagination.service.imp;

import com.sahriar.springPagination.domain.User;
import com.sahriar.springPagination.domain.UserRole;
import com.sahriar.springPagination.repository.UserRepo;
import com.sahriar.springPagination.repository.UserRoleRepo;
import com.sahriar.springPagination.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by toufiq on 4/18/18.
 */
@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserRoleRepo userRoleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        UserRole userRole = null;
        for (String role : user.getUserRoles()) {
            userRole = new UserRole();
            userRole.setUser(user);
            userRole.setUserRoles(role);
            userRoleRepo.save(userRole);
        }
    }

    @Transactional(readOnly = true)
    public Page<User> findAllPageable(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findByNamePageable(String name, Pageable pageable) {
        return userRepo.findByName(name, pageable);
    }
}
