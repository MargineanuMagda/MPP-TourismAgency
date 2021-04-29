package repository;

import domain.Entity;
import domain.validators.ValidationException;

/**
 * CRUD operations repository interface
 * @param <E> -  type of entities saved in repository
 */
public interface Repository <Long,E extends Entity>{

    E findOne(Long id);
    Iterable<E> findAll();
    E save(E entity) throws ValidationException, RepoException;
    E delete(Long id) throws RepoException;
    E update(E entity) throws RepoException, ValidationException;
    int size();

}
