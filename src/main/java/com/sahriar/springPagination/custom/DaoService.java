package com.sahriar.springPagination.custom;

import com.sahriar.springPagination.domain.User;
import com.sahriar.springPagination.domain.UserRole;

public class DaoService {

    @DataAccess(entity = User.class)
    private GenericDao userDao;

    @DataAccess(entity = UserRole.class)
    private GenericDao roleDao;
}
