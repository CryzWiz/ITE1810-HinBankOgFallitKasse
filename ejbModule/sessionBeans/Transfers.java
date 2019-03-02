package sessionBeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import entity.Transfer;

/**
 * Session Bean implementation class Transfers
 */
@Stateful(name="Transfers")
@LocalBean
public class Transfers implements TransfersLocal {

	@PersistenceContext (unitName="bankdb-unit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	
    /**
     * Default constructor. 
     */
    public Transfers() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(Transfer transfer) throws Exception {
		entityManager.persist(transfer);
	}

	@SuppressWarnings({ "unchecked" })
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Transfer> getAllTransfers(String accountNumber) throws Exception {
		Query query = entityManager.createQuery("SELECT t from Transfer as t");
		List<Transfer> list = query.getResultList();
		List<Transfer> result = new ArrayList<Transfer>();
		for(Transfer t: list){
			if(t.getKonto().equals(accountNumber)){
				result.add(t);
			}
		}
		return result;	
	}

	@SuppressWarnings({ "unchecked" })
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Transfer> getAllTransfersInTimeframe(Date fromDate, Date toDate, String accountNumber)
			throws Exception {
		Query query = entityManager.createQuery("SELECT t from Transfer as t");
		List<Transfer> list = query.getResultList();
		List<Transfer> result = new ArrayList<Transfer>();
		for(Transfer t: list){
			if(toDate.compareTo(t.getTimestamp()) > 0 
					&& fromDate.compareTo(t.getTimestamp()) < 0 
					&& t.getKonto().equals(accountNumber)) {
				result.add(t);
			}
		}
		return result;	
	}

}
