package sessionBeans;

import java.util.List;

import javax.ejb.Local;

import entity.Card;

@Local
public interface CardsLocal {

	public boolean add(Card c) throws Exception;
	
	public void remove(Card c) throws Exception;
	
	public boolean checkCard(String accountNumber, int PIN) throws Exception;
	
	public List<Card> getList() throws Exception;
	
	
}
