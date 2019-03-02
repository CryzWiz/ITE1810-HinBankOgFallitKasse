package sessionBeans;

import java.util.List;

import javax.ejb.Local;

import entity.Account;

@Local
public interface AccountsLocal {

	public void add(Account k) throws Exception;
	
	public void remove(Account k) throws Exception;
	
	public String generateKontonummer() throws Exception;
	
	public Account getAccount(String accountNumber) throws Exception;
	
	public List<Account> getList() throws Exception;
	
}
