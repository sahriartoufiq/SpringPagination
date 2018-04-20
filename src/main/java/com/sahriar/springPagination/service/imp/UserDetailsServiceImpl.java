package com.sahriar.springPagination.service.imp;

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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by toufiq on 4/20/18.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        List<User> userList = userRepo.findByName(name);

        logger.debug(name);
        logger.debug(userList + "");
        User user = null;
        if (userList != null && userList.size() > 0) {
            user = (User) userList.get(0);

            logger.debug(user.getName()+ "-" + user.getPassword() + "-");

        } else {
            return null;
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
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authoritySet);
    }
}

