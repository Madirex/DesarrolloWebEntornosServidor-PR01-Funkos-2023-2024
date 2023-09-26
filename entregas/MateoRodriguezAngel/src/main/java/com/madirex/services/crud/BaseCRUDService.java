package com.madirex.services.crud;

import com.madirex.exceptions.FunkoException;
import com.madirex.models.Funko;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseCRUDService <I, E extends Throwable>{
    List<I> findAll() throws SQLException;

    Optional<I> findById(String id) throws SQLException;

    Optional<I> save(I item) throws SQLException, E;

    Optional<I> update(String id, I newI) throws SQLException, E;
}