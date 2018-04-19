package com.sahriar.springPagination.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


import java.io.Serializable;

/**
 * Created by toufiq on 4/19/18.
 */

@NoRepositoryBean
public interface MyBaseRepo<T, ID extends Serializable> extends JpaRepository<T, ID> {


}
