package com.sahriar.springPagination.domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * Created by toufiq on 4/18/18.
 */

@Entity
@Table(name = "TBL_USER_ROLES")
public class UserRole extends Domain {


    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "USER_ROLE",  nullable = false)
    private String userRoles;

//    @OneToMany(cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY,
//            //      orphanRemoval = true,
//            mappedBy = "role")
//    private Set<Privilege> privileges;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Set<Privilege> getPrivileges() {
//        return privileges;
//    }
//
//    public void setPrivileges(Set<Privilege> privileges) {
//        this.privileges = privileges;
//    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }
}
