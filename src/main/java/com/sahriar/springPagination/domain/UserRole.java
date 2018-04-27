package com.sahriar.springPagination.domain;

import javax.persistence.*;

/**
 * Created by toufiq on 4/18/18.
 */

@Entity
@Table(name = "TBL_USER_ROLES")
public class UserRole extends Domain{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "USER_ROLE",  nullable = false)
    private String userRoles;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }
}
