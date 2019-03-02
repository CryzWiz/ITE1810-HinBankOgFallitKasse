package sessionBeans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import entity.Person;

/**
 * Session Bean implementation class Personer
 */
@Stateful(name="Persons")
@LocalBean
public class Persons implements PersonsLocal {
	@PersistenceContext (unitName="bankdb-unit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	
    /**
     * Default constructor. 
     */
    public Persons() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(Person p) throws Exception {
		entityManager.persist(p);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Person p) throws Exception {
		entityManager.remove(entityManager.merge(p));	
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Person> getList() throws Exception {
		Query query = entityManager.createQuery("SELECT p from Person as p");
		List<Person> list = query.getResultList();
		return list;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Person getPersonById(int id) throws Exception {
		return entityManager.find(Person.class, id);
	}

	
}

