package sessionBeans;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import entity.*;

/**
 * Session Bean implementation class BankServices
 */
@Stateful (name="BankServices")
@LocalBean
@EJB(name="Cards",beanInterface=CardsLocal.class)
public class BankServices implements BankServicesRemote {
	
	@PersistenceContext (unitName="bankdb-unit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	/**
	 * Original account number. We start here and count up.
	 * Not used - Moved to local account bean
	 */
	//private long ORGKONTONUMMER = 510000000;
	
    /**
     * Default constructor. 
     */
    public BankServices() {
    }
	
	/**
	 * Get {@link Person} with the given id
	 * @param int id
	 * @throws Exception
	 * @returns found person
	 */
    @EJB(name="Persons",beanInterface=PersonsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public Person getPerson(int id) throws Exception{
		//return entityManager.find(Person.class, id);
    	Person p;
		try{
			Context context = new InitialContext();
			PersonsLocal personerSessionBean = (PersonsLocal)context.lookup("java:comp/env/Persons");
			p = personerSessionBean.getPersonById(id);
		}
		catch(Exception ex){
			p = null;
		}
		return p;

	}
	
	/**
	 * Add given {@link Account} k and create a {@link Card} for it
	 * Set account number and created date, and store to DB
	 * @param Account k
	 * @throws Exception
	 */
    @EJB(name="Account",beanInterface=AccountsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public void addKonto(Account a) throws Exception {
		
		Context context = new InitialContext();
		AccountsLocal accountSessionBean = (AccountsLocal)context.lookup("java:comp/env/Account");
		//CardsLocal cardsSessionBean = (CardsLocal)context.lookup("java:comp/env/Cards");
		
		Date date = new Date();
		a.setKontonummer(accountSessionBean.generateKontonummer());
		a.setOpprettet(date);
		a.setSaldo(generate4RandomNumbers());
		Card card = new Card(a.getKontonummer(), generate4RandomNumbers());
		a.setKortReg(addNewCard(card));
		accountSessionBean.add(a);
		
		/*
		int size = listKonto().size();
		// Setter kontonummer basert på antall kontoer
		String knummer = Long.toString(ORGKONTONUMMER + size);
		k.setKontonummer(knummer);
		// Setter dato for opprettelsen
		Date c = new Date();
		k.setOpprettet(c);
		k.setSaldo(generate4RandomNumbers());
		// Oppretter kort til kontoen (låner 4 random tall til pin)
		Kort kort = new Kort(k.getKontonummer(), generate4RandomNumbers());
		k.setKortReg(addKort(kort));
		// Lagre konto
		entityManager.persist(k);
		*/		
	}
    
    /**
     * Save the new card
     * @param card
     * @return	boolean true / false depending on outcome
     * @throws Exception
     */
    @EJB(name="Cards",beanInterface=CardsLocal.class)
    @TransactionAttribute (TransactionAttributeType.REQUIRED)
    public boolean addNewCard(Card card) throws Exception{
    	boolean result = false;
		try{
			Context context = new InitialContext();
	    	CardsLocal cardsSessionBean = (CardsLocal)context.lookup("java:comp/env/Cards");
	    	result = cardsSessionBean.add(card);
		}
		catch(Exception ex){
			result = false;
		}
		return result;
    }
	
	/**
	 * Generate 4 random numbers. To be used as PIN and balance during test
	 * @return Integer between 1000 - 9999
	 */
	public int generate4RandomNumbers(){
		Random r = new Random();
		int Low = 1000;
		int High = 10000;
		return r.nextInt(High-Low) + Low;
	}
	
	/**
	 * Remove given {@link Account} k from DB
	 * @param Account k
	 * @throws Exception
	 */
	@EJB(name="Accounts",beanInterface=AccountsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public void removeKonto(Account k) throws Exception {
		//entityManager.remove(entityManager.merge(k));
		Context context = new InitialContext();
		AccountsLocal kontoerSessionBean = (AccountsLocal)context.lookup("java:comp/env/Accounts");
		kontoerSessionBean.remove(k);
	}
	
	/**
	 * Remove given {@link Card}
	 * @param Card k
	 * @throws Exception
	 */
	@EJB(name="Cards",beanInterface=CardsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public void removeKort(Card c) throws Exception {
		Context context = new InitialContext();
		CardsLocal cardsSessionBean = (CardsLocal)context.lookup("java:comp/env/Cards");
		cardsSessionBean.remove(c);
		//entityManager.remove(entityManager.merge(k));
	}
	
	/**
	 * Fetch the {@link Account} affiliated with this account number/card number
	 * @param int PIN
	 * @param string account number/card number
	 * @return {@link Account}
	 */
	@EJB(name="Accounts",beanInterface=AccountsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public Account getKonto(String kontonummer, int PIN) throws Exception{
		Context context = new InitialContext();
		AccountsLocal accountSessionBean = (AccountsLocal)context.lookup("java:comp/env/Accounts");
		CardsLocal cardsSessionBean = (CardsLocal)context.lookup("java:comp/env/Cards");
		
		if(cardsSessionBean.checkCard(kontonummer, PIN)){
			return accountSessionBean.getAccount(kontonummer);
		}
		else{
			return null;
		}
		
		/*
		Card k = entityManager.find(Card.class, kontonummer);
		if(k.getPIN() == PIN & k != null){
			return entityManager.find(Account.class, kontonummer);
		}
		else{
			return null;
		}
		*/
	}

	/**
	 * Store the given Transfer log
	 * @param transfer
	 * @throws Exception
	 */
	@EJB(name="Transfers",beanInterface=TransfersLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public void addTransfer(Transfer transfer) throws Exception{
		Context context = new InitialContext();
		TransfersLocal transferSessionBean = (TransfersLocal)context.lookup("java:comp/env/Transfers");
		transferSessionBean.add(transfer);
	}
	
	/**
	 * Transfer money from one {@link Account} to another
	 * 
	 * @param String toAccount
	 * @param String fromAccount
	 * @param Double amount
	 * @return Boolean true/false depending on transfer being made or not
	 */
	@EJB(name="Accounts",beanInterface=AccountsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public boolean Transfer(String tilKonto, String fraKonto, double belop) throws Exception {
		boolean result = false;
		try{
			Context context = new InitialContext();
			AccountsLocal accountSessionBean = (AccountsLocal)context.lookup("java:comp/env/Accounts");
			
			Account fromAccount = accountSessionBean.getAccount(fraKonto);
			Account toAccount = accountSessionBean.getAccount(tilKonto);
			Date date = new Date();
			double balance = fromAccount.getSaldo();
			
			if(balance >= belop){
				
				fromAccount.setSaldo(balance-belop);
				toAccount.setSaldo(toAccount.getSaldo() + belop);
				
				fromAccount = accountSessionBean.getAccount(fraKonto);
				toAccount = accountSessionBean.getAccount(tilKonto);
				
				Transfer transfer = new Transfer("Uttak", fromAccount.getKontonummer(), belop, 
						"Overføring til " + toAccount.getKontonummer(), fromAccount.getSaldo(), date);
				addTransfer(transfer);
				transfer = new Transfer("Innskudd", toAccount.getKontonummer(), belop, 
						"Overføring fra " + fromAccount.getKontonummer(), toAccount.getSaldo(), date);
				addTransfer(transfer);
				
				result = true;
			}
			else{
				result = false;
			}
		}
		catch(Exception ex){
			result = false;
		}
		return result;

		/*
		Account kFrom = entityManager.find(Account.class, fraKonto);
		Account kTo = entityManager.find(Account.class, tilKonto);
		Date date = new Date();
		if(kFrom.getSaldo() >= belop){
			
			String detaljerTil = "Overføring til " + kTo.getKontonummer();
			Transfer transferFra = new Transfer("Uttak", kFrom.getKontonummer(), belop, date, detaljerTil);
			entityManager.persist(transferFra);
			
			String detaljerFra = "Overføring fra " + kFrom.getKontonummer();
			Transfer transferTil = new Transfer("Innskudd", kTo.getKontonummer(), belop, date, detaljerFra);
			entityManager.persist(transferTil);
			
			kFrom.setSaldo(kFrom.getSaldo() - belop);
			kTo.setSaldo(kTo.getSaldo() + belop);
			entityManager.flush();
			return true;
		}
		else {
			return false;
		}
		*/
	}
	
	/**
	 * Deposit money to given {@link Account}
	 * 
	 * @param String account number
	 * @param double amount
	 * @return boolean true \ false depending on outcome
	 */
	@SuppressWarnings("unused")
	@EJB(name="Accounts",beanInterface=AccountsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public boolean Innskudd(String kontonummer, double belop) throws Exception{
		boolean result = false;
		try{
			Context context = new InitialContext();
			AccountsLocal accountSessionBean = (AccountsLocal)context.lookup("java:comp/env/Accounts");
			Transfer transfer;
			Account account = accountSessionBean.getAccount(kontonummer);
			Date date = new Date();
			String detaljer = "Innskudd i minibank";
			account.setSaldo(account.getSaldo() + belop);
			account = accountSessionBean.getAccount(kontonummer);
			addTransfer(transfer = new Transfer("Innskudd", kontonummer, belop, detaljer, account.getSaldo(), date));
			result = true;
		}
		catch(Exception ex){
			result = false;
		}
		return result;
	}
	
	/**
	 * Withdraw money from the given {@link Account}
	 * 
	 * @param String account number/card number
	 * @param double amount
	 * @return boolean true / false depending on transfer being made or not
	 */
	@SuppressWarnings("unused")
	@EJB(name="Accounts",beanInterface=AccountsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public boolean Uttak(String kontonummer, double belop) throws Exception {
		boolean result = false;
		try{
			Context context = new InitialContext();
			AccountsLocal accountSessionBean = (AccountsLocal)context.lookup("java:comp/env/Accounts");
			Transfer transfer;
			Account account = accountSessionBean.getAccount(kontonummer);
			Date date = new Date();
			double balance = account.getSaldo();
			if(balance >= belop){
				account.setSaldo(balance - belop);
				String detaljer = "Uttak i minibank";
				account = accountSessionBean.getAccount(kontonummer);
				addTransfer(transfer = new Transfer("Uttak", kontonummer, belop, detaljer, account.getSaldo(), date));
				result = true;
			}
			else{
				result = false;
			}
		}
		catch(Exception ex){
			result = false;
		}
		return result;

		/*
		Account konto = entityManager.find(Account.class, kontonummer);
		Date date = new Date();
		if(konto.getSaldo() >= belop){
			String detaljer = "Uttak i minibank";
			Transfer transfer = new Transfer("Uttak", kontonummer, belop, date, detaljer);
			entityManager.persist(transfer);
			konto.setSaldo(konto.getSaldo() - belop);
			entityManager.flush();
			return true;
		}
		else {
			return false;
		}
		*/
	}
	
	/**
	 * Create a new account - This method is the one available for the
	 * user in the ATM.
	 * The only difference here is that we let the user pick PIN, and
	 * the balance is set to 0. Not a random number.
	 * 
	 * @param Account account
	 * @return boolean true / false depending on account was created or not
	 */
	@EJB(name="Accounts",beanInterface=AccountsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public Account CreateAccount(Account account, int PIN) throws Exception{
		Account result = null;
		try{
			Context context = new InitialContext();
			AccountsLocal accountSessionBean = (AccountsLocal)context.lookup("java:comp/env/Account");
			Date date = new Date();
			account.setKontonummer(accountSessionBean.generateKontonummer());
			account.setOpprettet(date);
			Card card = new Card(account.getKontonummer(), PIN);
			account.setKortReg(addNewCard(card));
			accountSessionBean.add(account);
			result = accountSessionBean.getAccount(card.getKortnummer());
		}
		catch(Exception ex){
			result = null;
		}
		
		return result;
		
	}
	
	/**
	 * Fetch all transfers for given account fromDate to toDate.
	 * @param Date fromDate
	 * @param Date toDate
	 * @param String accountNumber
	 * return List<Transfer> list of all transfers in given time-frame
	 */
	@EJB(name="Transfers",beanInterface=TransfersLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public List<Transfer> getTransfers(Date fromDate, Date toDate, String accountNumber) throws Exception{
		List<Transfer> result;
		try{
			Context context = new InitialContext();
			TransfersLocal transfersSessionBean = (TransfersLocal)context.lookup("java:comp/env/Transfers");
			result = transfersSessionBean.getAllTransfersInTimeframe(fromDate, toDate, accountNumber);
		}
		catch(Exception ex){
			result = null;
		}
		return result;
	}
	
	/**
	 * Fetch All transfers for given account
	 * @param String accountNumber
	 * @return List<Transfer> all transfers for this account
	 */
	@EJB(name="Transfers",beanInterface=TransfersLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public List<Transfer> getAllTransfers(String accountNumber) throws Exception{
		List<Transfer> result;
		try{
			Context context = new InitialContext();
			TransfersLocal transfersSessionBean = (TransfersLocal)context.lookup("java:comp/env/Transfers");
			result = transfersSessionBean.getAllTransfers(accountNumber);
		}
		catch(Exception ex){
			result = null;
		}
		return result;
	}
	
	
	
	
	
	
	
	
	//--------------------------------------------------------------------------------------------------
	// Only methods used during testing and code that is no longer used below this line. 
	// Methods that should not be here in a production environment, or test code
	// that now runs through a local sessionBean instead.
	// These are methods that you would not give to a ATM due to safety.
	// Like delete persons, list all accounts and cards. Delete transactions
	// and so on. 
	// And since they are only used for testing purposes, some may use POJO's directly
	// just for convenience.
	//---------------------------------------------------------------------------------------------------
	
	/**
	 * TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	 * Delete the given Transfer log entry
	 * @param long id
	 */
	//@EJB(name="Transfers",beanInterface=TransfersLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public void removeTransfer(long id) {
		entityManager.remove(entityManager.merge(entityManager.find(Transfer.class, id)));
	}
	
	/** TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	 * Fetch all the registered transfers in the accounts
	 * @throws Exception
	 * @return List<Transfer> all 
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public List<Transfer> listTransfer() throws Exception {
		Query query = entityManager.createQuery("SELECT t from Transfer as t");
		List<Transfer> list = query.getResultList();
		return list;
	}

	/** TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	 * Fetch all the {@link Card}'s registered in db
	 * @throws Exception
	 * @return {@link Card}'s as list
	 */
	@EJB(name="Cards",beanInterface=CardsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public List<Card> listKort() throws Exception {
		Context context = new InitialContext();
		CardsLocal cardsSessionBean = (CardsLocal)context.lookup("java:comp/env/Cards");
		return cardsSessionBean.getList();
		/*
		Query query = entityManager.createQuery("SELECT k from Kort as k");
		List<Card> list = query.getResultList();
		return list;
		*/
	}
	
	/** TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	 * Fetch all the {@link Account}'s registered in db
	 * @throws Exception
	 * @returns {@link Account}'s as list
	 */
	@EJB(name="Accounts",beanInterface=AccountsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public List<Account> listKonto() throws Exception {
		Context context = new InitialContext();
		AccountsLocal kontoerSessionBean = (AccountsLocal)context.lookup("java:comp/env/Accounts"); 
		return kontoerSessionBean.getList();
		/*
		Query query = entityManager.createQuery("SELECT k from Konto as k");
		List<Account> list = query.getResultList();
		return list;
		*/
	}
	
	/** TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	 * Create a new {@link Person}
	 * @param Person p
	 * @throws Exception
	 */
	@EJB(name="Persons",beanInterface=PersonsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public void addPerson(Person p) throws Exception {
		//entityManager.persist(p);
		Context context = new InitialContext();
		PersonsLocal personerSessionBean = (PersonsLocal)context.lookup("java:comp/env/Persons");
		personerSessionBean.add(p);
	}
	
	/** TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	 * Removes the {@link Person}
	 * @param Person p
	 * @throws Exception
	 */
	@EJB(name="Persons",beanInterface=PersonsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public void removePerson(Person p) throws Exception {
		//entityManager.remove(entityManager.merge(p));
		Context context = new InitialContext();
		PersonsLocal personerSessionBean = (PersonsLocal)context.lookup("java:comp/env/Persons");
		personerSessionBean.remove(p);
	}
	
	/** TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	 * Fetch all the registered {@link Person}'s
	 * @throws Exception
	 * @returns persons as list
	 */
	@EJB(name="Persons",beanInterface=PersonsLocal.class)
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public List<Person> listPerson() throws Exception {
		//Query query = entityManager.createQuery("SELECT p from Person as p");
		//List<Person> list = query.getResultList();
		Context context = new InitialContext();
		PersonsLocal personerSessionBean = (PersonsLocal)context.lookup("java:comp/env/Persons");
		List<Person> list = personerSessionBean.getList();
		return list;
	}
	
	/** NOT USED - LocalBean is used instead
	 * Add given {@link Card}
	 * @param Card k
	 * @throws Exception
	 */
	@TransactionAttribute (TransactionAttributeType.REQUIRED)
	public boolean addKort(Card k){
		boolean result = false;
		try{
			entityManager.persist(k);
			result = true;
		}
		catch(Exception ex){
			result = false;
		}
		return result;
	}
}

