package com.example.dao.impl;

import com.example.dao.CrudDAO;
import com.example.exception.SchoolDAOException;
import com.example.model.HasId;

public interface AbstractCrudDAOImpl<T extends HasId<K>, K> extends CrudDAO<T, K> {

    T create(T entity) throws SchoolDAOException;

    T update(T entity) throws SchoolDAOException;

    default T save(T entity) throws SchoolDAOException {
        return entity.getId() == null ? create(entity) : update(entity);
    }
}
