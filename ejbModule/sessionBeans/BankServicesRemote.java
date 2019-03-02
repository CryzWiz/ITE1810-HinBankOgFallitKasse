package sessionBeans;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import entity.*;

@Remote
public interface BankServicesRemote {
	// Person class
	public void addPerson(Person p) throws Exception;
	public void removePerson(Person p) throws Exception;
	public Person getPerson(int id) throws Exception;
	
	// Account class
	public void addKonto(Account k) throws Exception;
	public void removeKonto(Account k) throws Exception;
	public Account getKonto(String Kortnummer, int PIN) throws Exception;
	
	// Card class
	public boolean addKort(Card k) throws Exception;
	public void removeKort(Card k) throws Exception;
	
	// Bank services
	public boolean Uttak(String kontonummer, double belop) throws Exception;
	public boolean Transfer(String tilKonto, String fraKonto, double belop) throws Exception;
	public boolean Innskudd(String kontonummer, double belop) throws Exception;
	public Account CreateAccount(Account account, int PIN) throws Exception;
	public List<Transfer> getTransfers(Date fromDate, Date toDate, String accountNumber) throws Exception;
	public List<Transfer> getAllTransfers(String accountNumber) throws Exception;
	
	
	
	// TESTING METHODS - Should not be in a production environment.
	// Says itself that you should not give a client the possibility
	// to retrieve complete lists with accounts, cards or persons
	// registered in the bank, nor the possibility to delete transactions.
	public List<Transfer> listTransfer() throws Exception; // TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	public void removeTransfer(long id); // TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	public List<Card> listKort() throws Exception;  // TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	public List<Person> listPerson() throws Exception;  // TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
	public List<Account> listKonto() throws Exception;  // TESTING METHOD - SHOULD NOT BE IN PRODUCTIONCODE
}
