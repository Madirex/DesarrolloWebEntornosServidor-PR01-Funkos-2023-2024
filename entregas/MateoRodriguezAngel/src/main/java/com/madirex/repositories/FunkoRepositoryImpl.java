package com.madirex.repositories;

import com.madirex.models.Funko;
import com.madirex.models.Model;
import com.madirex.services.database.DatabaseManager;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación de la interfaz FunkoRepository
 */
@RequiredArgsConstructor
public class FunkoRepositoryImpl implements FunkoRepository {
    private final DatabaseManager database;

    /**
     * Devuelve todos los elementos del repositorio
     *
     * @return Optional de la lista de elementos
     */
    @Override
    public List<Funko> findAll() throws SQLException {
        List<Funko> list = new ArrayList<>();
        var sql = "SELECT * FROM pokemon";
        var res = database.select(sql).orElseThrow();
        while (res.next()) {
            list.add(Funko.builder()
                    .cod(UUID.fromString(res.getString("cod")))
                    .name(res.getString("name"))
                    .model(Model.valueOf(res.getString("modelo")))
                    .price(res.getDouble("precio"))
                    .releaseDate(res.getDate("fecha_lanzamiento").toLocalDate())
                    .build());
        }
        database.close();
        return list;
    }

    /**
     * Busca un elemento en el repositorio por su id
     *
     * @param integer Id del elemento a buscar
     * @return Optional del elemento encontrado
     */
    @Override
    public Optional<Funko> findById(Integer integer) throws SQLException {
        Optional<Funko> optReturn = Optional.empty();
        var sql = "SELECT * FROM funko WHERE id = ?";
        var res = database.select(sql, integer).orElseThrow();
        if (res.next()) {
            optReturn = Optional.of(Funko.builder()
                    .cod(UUID.fromString(res.getString("cod")))
                    .name(res.getString("name"))
                    .model(Model.valueOf(res.getString("modelo")))
                    .price(res.getDouble("precio"))
                    .releaseDate(res.getDate("fecha_lanzamiento").toLocalDate())
                    .build());
        }
        database.close();
        return optReturn;
    }

    /**
     * Guarda un elemento en el repositorio
     *
     * @param entity Elemento a guardar
     * @return Optional del elemento guardado
     */
    @Override
    public Optional<Funko> save(Funko entity) throws SQLException {
        var sql = "INSERT INTO funko (cod, nombre, modelo, precio, fecha_lanzamiento, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        database.open();
        database.insertAndGetKey(sql, entity.getCod(), entity.getName(), entity.getModel(), entity.getPrice(),
                        entity.getReleaseDate(), LocalDateTime.now(), LocalDateTime.now())
                .orElseThrow();
        database.close();
        return Optional.of(entity);
    }

    /**
     * [Método deshabilitado]
     *
     * @param integer Id del elemento a actualizar
     * @param entity  Elemento con los nuevos datos
     * @return Optional del elemento actualizado
     */
    @Override
    public Optional<Funko> update(Integer integer, Funko entity) {
        return Optional.empty(); //TODO: DO
    }

    /**
     * [Método deshabilitado]
     *
     * @param integer Id del elemento a borrar
     * @return Optional del elemento borrado
     */
    @Override
    public Optional<Funko> delete(Integer integer) {
        return Optional.empty(); //TODO: DO
    }

    /**
     * Busca un elemento en el repositorio por su nombre
     *
     * @param name Nombre del elemento a buscar
     * @return Lista de elementos encontrados
     */
    @Override
    public List<Funko> findByName(String name) throws SQLException {
        return findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }
}
