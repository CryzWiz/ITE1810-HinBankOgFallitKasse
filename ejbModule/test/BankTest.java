package test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;
import javax.ejb.EJB;
import sessionBeans.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import entity.*;


/**
 * Our JUnit test. Run some small tests on our different methods that are
 * available through the remote sessionBean
 * @author allan
 *
 */
public class BankTest extends TestCase{

	Context context;
	@EJB
	BankServicesRemote bank;
	
	public void setUp() throws Exception {
		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
		p.put("BankDBDatasource","new://Resource?type=DataSource");
		p.put("BankDBDatasource.JdbcDriver","org.hsqldb.jdbcDriver");
		p.put("BankDBDatasource.JdbcUrl","jdbc:hsqldb:file:data/bankdb/hsqldb");
		p.put("Unmanaged_BankDBDataSource","new://Resource?type=DataSource");
		p.put("Unmanaged_BankDBDataSource.JdbcDriver","org.hsqldb.jdbcDriver");
		p.put("Unmanaged_BankDBDataSource.JdbcUrl","jdbc:hsqldb:file:data/bankdb/hsqldb");
		p.put("Unmanaged_BankDBDataSource.JtaManaged","false");
		context = new InitialContext(p);
		bank = (BankServicesRemote)context.lookup("BankServicesRemote");
	}
	
	@Override
	public void tearDown() throws Exception { };
	
	/**
	 * JUnit-Test
	 * Test adding new person
	 * @throws Exception
	 */
	public void test() throws Exception {
		List<Person> list = bank.listPerson();
		int assertsize = list.size();
		
		assertEquals("List.size()", assertsize, list.size());
		addPerson();
		list = bank.listPerson();
		assertEquals("List.size()", assertsize + 5, list.size());
		//list();
	}
	
	/**
	 * JUnit-Test
	 * Test adding new account and card
	 * @throws Exception
	 */
	public void testCreateAccountandCard() throws Exception{
		addPerson();
		addKonto();
		List<Account> accountlist = bank.listKonto();
		List<Card> cardlist = bank.listKort();
		int assertsizeAccountList = accountlist.size();
		int assertsizeCardList = cardlist.size();
		assertEquals("List.size()", assertsizeAccountList, accountlist.size());
		assertEquals("List.size()", assertsizeCardList, cardlist.size());
		Date date = new Date();
		Account account = new Account(4, "510000006", date, 2500, false, "testkonto");
		bank.CreateAccount(account, 1234);
		accountlist = bank.listKonto();
		cardlist = bank.listKort();
		assertEquals("List.size()", assertsizeAccountList + 1, accountlist.size());
		assertEquals("List.size()", assertsizeCardList + 1, cardlist.size());
		//listKonto();
		//listKort();
	}
	/**
	 * Add some {@link Person}'s
	 * @throws Exception
	 */
	public void addPerson() throws Exception{
		try {
			Calendar c = Calendar.getInstance();
			c.set(1988, 00, 00, 01, 01, 01);
			Person p = new Person((Date) c.getTime(), "15251" , "Gurgle Halsbrann" ,
			"Jørgen Fisefins terasse 3", "", "8515" , "Narvik", "Harry Potter" ,
			"1111" ,"En av de virkelig store Halsbrannene i historien" );
			bank.addPerson(p);
			c.set(1986, 01, 01, 01, 01, 01);
			p = new Person((Date)c.getTime(), "12345" , "Sylfrid Fuglesang" , "Øvre Undergang 22" ,
			"", "8515", "Narvik" , "Jupp" , "1111" , "Kvitrende glad og positiv");
			bank.addPerson(p);
			c.set(1990, 11, 11, 01, 01, 01);
			p = new Person((Date)c.getTime(), "33332" , "Snork Rumlepung" , "c/o Rosa Balle" ,
			"Snyltestien 1", "8514", "Narvik", "Money" ,"1111",
			"Uten sidestykke i nyere historie");
			bank.addPerson(p);
			c.set(1991, 9, 21, 01, 01, 01);
			p = new Person((Date)c.getTime(), "54321","Walter Pengesluk","Andeby undergrunn","",
			"8500","Narvik","Donald","1111","Nær venn av Kjell Inge Røkke");
			bank.addPerson(p);
			c.set(1983, 8, 18, 01, 01, 01);
			p = new Person((Date)c.getTime(), "34183","Allan Arnesen","Islandsbotnveien 49","",
			"9303","Silsand","passord","1111","HiN Student, og HiN Bank dev");
			bank.addPerson(p);
			}
			finally {
			}
		
	}
	/**
	 * Add some {@link Account}'s
	 * @throws Exception
	 */
	public void addKonto() throws Exception{
		List<Person> list = bank.listPerson();
		Account k;
		try{
			for (Person pers : list) {
				k = new Account(pers.getId());
				bank.addKonto(k);
			}		
		}
		finally{
			
		}
	}
	/**
	 * List all {@link Account}'s
	 * @throws Exception
	 */
	public void listKonto() throws Exception{
		List<Account> list = bank.listKonto();
		for (Account k : list) {
			System.out.println("Listing kontoid " + k.getId() + ": " 
					+ "knummer:" + k.getKontonummer() 
					+ " - keier:" + bank.getPerson(k.getKontoeier()).getNavn() 
					+ " -  ksaldo:" + k.getSaldo()
					+ " - kdato:" + k.getOpprettet());
		}
		
	}
	/**
	 * List all {@link Card}'s
	 * @throws Exception
	 */
	public void listKort() throws Exception{
		List<Card> list = bank.listKort();
		for (Card k : list) {
			System.out.println("Listing kortid " + k.getKortnummer() + ": " 
					+ "PIN:" + k.getPIN());
		}
		
	}
	/**
	 * List all {@link Person}'s
	 * @throws Exception
	 */
	public void list() throws Exception{
		List<Person> list = bank.listPerson();
		for (Person pers : list) {
			System.out.println("Listing id " + pers.getId() + ": " 
					+ "fdato: " + pers.getFodselsdato() 
					+ " - fnummer: " + pers.getFodselsnummer() 
					+ " - navn: " + pers.getNavn());
		}
		
	}
	/**
	 * Delete all {@link Person}'s
	 * @throws Exception
	 */
	public void remove() throws Exception {
		try {
			List<Person> list = bank.listPerson();
			for (Person pers : list) {
				if (pers.getId() > 0) {
					bank.removePerson(pers);
					System.out.println("Deleting id " + pers.getId() + ": " 
							+ "fdato: " + pers.getFodselsdato() 
							+ " - fnummer: " + pers.getFodselsnummer() 
							+ " - navn: " + pers.getNavn());
				}
				else
					System.out.println("Delete id " + pers.getId() + " failed ...: " 
							+ "fdato: " + pers.getFodselsdato() 
							+ " - fnummer: " + pers.getFodselsnummer() 
							+ " - navn: " + pers.getNavn());
			}
		}
		finally {
		}
	}
	/**
	 * Delete all {@link Account}'s
	 * @throws Exception
	 */
	public void removeKonto() throws Exception {
		try {
			List<Account> list = bank.listKonto();
			for (Account konto : list) {
				if (konto.getId() > 0) {
					bank.removeKonto(konto);
					System.out.println("Deleting kontoid " + konto.getId() + ": " 
							+ "knummer:" + konto.getKontonummer() 
							+ " - keier:" + konto.getKontoeier() 
							+ " -  ksaldo:" + konto.getSaldo());
				}
				else
					System.out.println("Delete kontoid " + konto.getId() + " failed ...:" 
							+ " knummer:" + konto.getKontonummer() 
							+ " - keier:" + konto.getKontoeier() 
							+ " -  ksaldo:" + konto.getSaldo());
			}
		}
		finally {
		}
	}
	/**
	 * Delete all {@link Card}'s
	 * @throws Exception
	 */
	public void removeKort() throws Exception {
		try {
			List<Card> list = bank.listKort();
			for (Card kort : list) {
				if (kort.getKortnummer() != null) {
					bank.removeKort(kort);
					System.out.println("Deleting kortid " + kort.getKortnummer() + ": " 
							+ "PIN:" + kort.getPIN());
				}
				else
					System.out.println("Delete kortid " + kort.getKortnummer() + " failed ...:" 
							+ " PIN:" + kort.getPIN());
			}
		}
		finally {
		}
	}
	/**
	 * List all {@link Card}'s and {@link Account}'s info
	 * @throws Exception
	 */
	public void listKortOgKontoInfo() throws Exception {
		List<Card> listKort = bank.listKort();
		for(Card kort: listKort){
			Account k = bank.getKonto(kort.getKortnummer(), kort.getPIN());
			if(k != null){
				System.out.println("Listing kortid " + kort.getKortnummer() + ": " 
						+ "Eier:" + bank.getPerson(k.getKontoeier()).getNavn()
						+" Saldo:" + k.getSaldo()
						+ " PIN: " + kort.getPIN());
			}
			else{
				System.out.println("Listing kortid " + kort.getKortnummer() + ": " 
						+ "Failed:");
			}
			
		}
		
	}
	
	/**
	 * Do some withdrawals from all the {@link Account}'s
	 * @throws Exception
	 */
	public void testUttak() throws Exception {
		List<Account> list = bank.listKonto();
		for (Account k : list) {
			boolean r = bank.Uttak(k.getKontonummer(), 2000.00);
			if(r){
				System.out.println("Tatt ut 2000kr fra konto: " + k.getKontonummer());
			}
		}
		
	}
	/**
	 * List all {@link Transfer}'s made
	 * @throws Exception
	 */
	public void listTransfer() throws Exception {
		List<Transfer> list = bank.listTransfer();
		for (Transfer t : list) {
			System.out.println("Listing transferid " + t.getId() + ": " 
					+ "Type:" + t.getType() 
					+ " Konto:" + t.getKonto() 
					+ " Beløp:" + t.getBelop()
					+ " Detaljer:" + t.getDetaljer()
					+ " Saldo:" + t.getSaldo()
					+ " Timestamp:" + t.getTimestamp());
		}
		
	}
	/**
	 * Get a specific {@link Account}
	 * @param Kontonummer
	 * @param PIN
	 * @return
	 * @throws Exception
	 */
	public Account getKonto(String Kontonummer, int PIN) throws Exception{
		return bank.getKonto(Kontonummer, PIN);
	}
	/**
	 * Delete all {@link Transfer}'s
	 * @throws Exception
	 */
	public void removeTransfers() throws Exception{
		List<Transfer> list = bank.listTransfer();
		for(Transfer t : list){
			bank.removeTransfer(t.getId());
			System.out.println("Deleting transfer with id " + t.getId());
		}
	}

	
}
