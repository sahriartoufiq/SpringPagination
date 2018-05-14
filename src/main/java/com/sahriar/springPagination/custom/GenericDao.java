package com.sahriar.springPagination.custom;

import java.util.ArrayList;
import java.util.List;

public class GenericDao<E> {
    private Class<E> entityClass;

    public GenericDao(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public List<E> findAll() {
        return new ArrayList<>();
    }

    public String getEntityname(){
        return entityClass.getName();
    }


}
