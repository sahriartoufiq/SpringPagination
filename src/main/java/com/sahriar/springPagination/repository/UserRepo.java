package com.sahriar.springPagination.repository;

import com.sahriar.springPagination.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by toufiq on 4/18/18.
 */


public interface UserRepo extends MyBaseRepo<User, Long> {

    Page<User> findByName(String name, Pageable pageable);

    List<User> findByName(String name);

}
