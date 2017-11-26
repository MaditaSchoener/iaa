package de.nordakademie.iaa.model.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Maik Voigt
 *
 * Abstakte Repository Klasse mit persist, find, update und delete Definitionen. Verwendet den EntityManager.
 */

abstract class AbstractRepository<TYPE> {

	@PersistenceContext
	private EntityManager entityManager;
	private final Class <TYPE> type;
	
	public AbstractRepository (Class <TYPE> type) {
		this.type = type;
	}
	
	public TYPE persist(final TYPE type) {
		entityManager.persist(type);
		return type;
	}
	public TYPE find(final Object id) {
		return entityManager.find(type, id);
	}
	public void delete (final TYPE object) {
		entityManager.remove(object);
	}
	public TYPE update (final TYPE type) throws IllegalEntityException {
		return entityManager.merge(type);
	}
	protected EntityManager entityManager () {
		return entityManager;
	}
}
