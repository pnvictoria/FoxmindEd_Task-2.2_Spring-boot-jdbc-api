package com.example.dao;

import com.example.exception.SchoolDAOException;
import com.example.model.HasId;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T extends HasId<K>, K> {

    Optional<T> findById(K id) throws SchoolDAOException;

    List<T> findAll() throws SchoolDAOException;

    T save(T entity) throws SchoolDAOException;

    void deleteById(K id) throws SchoolDAOException;

    List<T> saveAll(List<T> entities) throws SchoolDAOException;
}
