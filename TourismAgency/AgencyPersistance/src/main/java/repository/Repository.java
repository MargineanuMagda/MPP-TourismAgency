package repository;

import domain.Entity;
import domain.validators.ValidationException;

/**
 * CRUD operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */
public interface Repository <ID,E extends Entity<ID>>{

    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entity) throws ValidationException, RepoException;
    E delete(ID id) throws RepoException;
    E update(E entity) throws RepoException, ValidationException;
    int size();

}
