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

import entity.Card;

/**
 * Session Bean implementation class Cards
 */
@Stateful(name="Cards")
@LocalBean
public class Cards implements CardsLocal {
	@PersistenceContext (unitName="bankdb-unit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	
    /**
     * Default constructor. 
     */
    public Cards() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean add(Card c){
		boolean result = false;
		try{
			entityManager.persist(c);
			result = true;
		}
		catch(Exception ex){
			result = false;
		}
		return result;
	}
	
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Card c) throws Exception{
    	entityManager.remove(entityManager.merge(c));
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Card> getList() throws Exception {
		Query query = entityManager.createQuery("SELECT c from Card as c");
		List<Card> list = query.getResultList();
		return list;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean checkCard(String accountNumber, int PIN) throws Exception {
		Card c = entityManager.find(Card.class, accountNumber);
		if(c.getPIN() == PIN){
			return true;
		}
		else{
			return false;
		}
	}

}

