package com.sahriar.springPagination.repository;

import com.sahriar.springPagination.domain.UserRole;

import java.util.List;

/**
 * Created by toufiq on 4/18/18.
 */
public interface UserRoleRepo extends MyBaseRepo<UserRole, Long> {

    List<UserRole> findByUser_Id(int userId);

}
