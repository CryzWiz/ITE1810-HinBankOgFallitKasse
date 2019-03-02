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

import entity.Account;

/**
 * Session Bean implementation class Accounts
 */
@Stateful(name="Accounts")
@LocalBean
public class Accounts implements AccountsLocal {
	@PersistenceContext (unitName="bankdb-unit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	
	/**
	 * Veldig enkel m�te � sette kontonummer i en serie p�. 
	 * Det sier seg selv at denne ville ikke holdt, men siden det ikke er noen
	 * oppgitte formler eller �nsker som sier hvordan kontonumrene
	 * skal g� i serie legger jeg det slik pr n�. Siden hver konto f�r 
	 * generert en id, og kontonummer blir {@link ORGKONTONUMMER} + id skal hvert
	 * kontonummer bli unikt. MEN jeg er rimelig sikker p� at det skal v�re
	 * en m�te � sette sekvensen du �nsker id generert automatisk av JPL
	 * men jeg har ikke funnet den enda. Skal oppdateres om jeg finner den.
	 * Ideen var ihvertfall at HiN Bank sine kontonumre skulle alle starte med 51,
	 * og v�re 9 siffer langt. Da har vi 99 millioner kontoer tilgjengelig. Burde 
	 * holde for denne oppgaven.
	 */
	
	/**
	 * Orginalt kontonummer -> Vi starter her og teller opp.
	 * Kunne muligens med fordel v�rt en String.
	 */
	private long ORGKONTONUMMER = 510000000;
    
	/**
     * Default constructor. 
     */
    public Accounts() {
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(Account a) throws Exception {
    	entityManager.persist(a);
	}
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Account a) throws Exception {
    	entityManager.remove(entityManager.merge(a));
	}
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String generateKontonummer(){
		try {
			return Long.toString(ORGKONTONUMMER + getList().size() + 1);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Account getAccount(String accountNumber) throws Exception {
		return entityManager.find(Account.class, accountNumber);
	}
	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Account> getList() throws Exception {
		Query query = entityManager.createQuery("SELECT a from Account as a");
		List<Account> list = query.getResultList();
		return list;
	}

}

