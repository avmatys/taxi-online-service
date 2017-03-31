package by.bsuir.matys_rozin_zarudny.layer.persistence;

import java.util.List;

import by.bsuir.matys_rozin_zarudny.common.exceptions.EntityNotFoundException;

/**
 * A generic Data Access Object (DAO) interface for handling and managing event
 * related with data
 *
 * @param <K> primary key for entity
 * @param <V> type of entity to manipulate
 */
public interface EntityDao<K, V> {

    /**
     * Find a return object of type V with primary key K.
     * If entity with key K is not found return null.
     *
     * @param id primary key.
     * @return return object of type V with primary key K. 
     *         If entity with key K is not found return null.
     * @throws IllegalArgumentException if id is null.
     */
    public V findEntityById(final K id);

    /**
     * Persist an entity to the data layer.
     *
     * @param entity entity to persist
     * @throws IllegalArgumentException if entity is null.
     */
    public void persistEntity(final V entity);

    /**
     * Delete entity by id.
     *
     * @param id id of entity
     * @throws EntityNotFoundException entity not found exception.
     * @throws IllegalArgumentException if id is null.
     */
    public void deleteEntityById(final K id) throws EntityNotFoundException;

    /**
     * Update entity in JPA repository.
     *
     * @param entity entity to update.
     * @throws IllegalArgumentException if entity is null.
     */
    public void update(final V entity);

    /**
     * Return all entities for given class.
     * If no entities found, null is returned.
     *
     * @return all entities for given class. 
     *         If no entities, found null is returned.
     */
    public List<V> findAll();
}
