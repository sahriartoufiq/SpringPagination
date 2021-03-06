package com.sahriar.springPagination.service.imp;

import com.sahriar.springPagination.domain.CustomUser;
import com.sahriar.springPagination.domain.User;
import com.sahriar.springPagination.domain.UserRole;
import com.sahriar.springPagination.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by toufiq on 4/20/18.
 */

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(name);


        if(user == null){
            throw new UsernameNotFoundException(name);
        }
        Set<GrantedAuthority> grantedAuthoritySet = buildGrantedAuthoritySet(user.getUserRoleSet());

        return buildUserDetailsAuth(user, grantedAuthoritySet);
    }

    private Set<GrantedAuthority> buildGrantedAuthoritySet(Set<UserRole> userRoleSet) {
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        for (UserRole userRole : userRoleSet) {
            grantedAuthoritySet.add(new SimpleGrantedAuthority(userRole.getUserRoles()));
        }
        return grantedAuthoritySet;
    }

    private UserDetails buildUserDetailsAuth(User user, Set<GrantedAuthority> authoritySet) {
        CustomUser customUser = new CustomUser(user.getUserName(), user.getEncodedPassword(), authoritySet);
        customUser.setDistrict("Dhaka");
        return customUser;
       // return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getEncodedPassword(), authoritySet);
    }
}

