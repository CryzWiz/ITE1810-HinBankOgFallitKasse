package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import entity.Account;
import entity.Transfer;

/**
 * Test client for our EJB project. A very simple client program with a simplified
 * GUI. 
 * We borrow our connection to the EJB remoteBeans from our JUnit test
 * We also use the JUnit test {@link BankTest} to generate some test-data
 * for us.
 * @author allan
 *
 */
public class TestEJB {
	/**
	 * Set up some test data through our {@link BankTest} test class
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{

		BankTest bt = new BankTest();
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Setting up the classes..");
		bt.setUp();
		System.out.println("Adding testpersons..");
		bt.addPerson();
		//bt.list();
		System.out.println("Adding testkonto's and card's..");
		bt.addKonto();
		//bt.listKonto();
		
		//bt.listKort();
		runTest(bt, consoleReader);
		
		
		//bt.listKortOgKontoInfo();
		//bt.testUttak();
		//bt.listTransfer();
	}
	/**
	 * Run the test program itself, and give options to see
	 * generated data
	 * @param bt
	 * @param consoleReader
	 * @throws Exception
	 */
	private static void runTest(BankTest bt, BufferedReader consoleReader) throws Exception {
		
		int choice = 1;
		while (choice != 6) {
			
            showGUI();
            choice = Integer.parseInt(consoleReader.readLine());
            if (choice == 1) {
            	System.out.println("Listing all Persons:");
            	bt.list();        
            } 
            else if (choice == 2) {
            	System.out.println("Listing all Konto's:");
            	bt.listKonto();
            }
            else if(choice == 3){
            	System.out.println("Listing all Kort's");
            	bt.listKortOgKontoInfo();
            }
            else if(choice == 4){
            	System.out.println("Listing all Transfer's:");
            	bt.listTransfer();
            }
            else if(choice == 5){
            	runATM(bt, consoleReader);
            	
            }
            else if(choice == 6){
            	System.out.println("Clearing all testdata...");
            	bt.removeTransfers();
        		bt.removeKort();
        		bt.removeKonto();
        		bt.remove();
        		bt.tearDown();
        		System.out.println(".... Done");
            	break;
            }
         }
		
	}
	/**
	 * Run the ATM client 
	 * @param bt
	 * @param consoleReader
	 * @throws Exception
	 */
	private static void runATM(BankTest bt, BufferedReader consoleReader) throws Exception {
		
		int atmChoice = 1;
		String cardNumber;
		String pin;
		Account activeKonto;
		
		System.out.println("Skriv inn kortnummeret:");
		cardNumber = consoleReader.readLine();
		System.out.println("Skriv inn PIN:");
		pin = consoleReader.readLine();

		if(cardNumber.length() == 9
				& cardNumber.substring(0,2).equals("51")){
			activeKonto = bt.getKonto(cardNumber, Integer.parseInt(pin));
		}
		else{
			activeKonto = null;
		}
		if(activeKonto != null){
			while(atmChoice != 7){
	    		showATMGUI(cardNumber);
	    		atmChoice = Integer.parseInt(consoleReader.readLine());
	    		
	    		if(atmChoice == 1){
	    			showSaldo(activeKonto);
	    		}
	    		else if(atmChoice == 2){
	    			opprettKonto(bt, consoleReader, activeKonto);
	    		}
	    		else if(atmChoice == 3){
	    			seTransaksjoner(bt, consoleReader, activeKonto); 			
	    		}
	    		else if(atmChoice == 4){
	    			settInnskudd(bt, consoleReader, activeKonto, cardNumber, pin);
	    		}
	    		else if(atmChoice == 5){
	    			settUttak(bt, consoleReader, activeKonto, cardNumber, pin); 
	    		}
	    		else if(atmChoice == 6){
	    			settOverfor(bt, consoleReader, activeKonto, cardNumber, pin);
	    		}
	    		else if(atmChoice == 7){
	    			System.out.println("Takk for besøket!  .... (Spytter ut kort)");
	    			System.out.println("\n\n\n\n");
	    			break;
	    		}
	    	}
		}
		else{
			System.out.println("Feil kombinasjon av PIN og Kort eller ugyldig kort... (Spytter ut kort)");
			System.out.println("\n\n\n\n");
		}
	}
	/**
	 * Show the user his/hers balance
	 * @param activeKonto
	 */
	private static void showSaldo(Account activeKonto) {
		System.out.println("Saldo: " + activeKonto.getSaldo());
	}
	/**
	 * Let the user make him/her self a new {@link Account}
	 * @param bt
	 * @param consoleReader
	 * @param activeKonto
	 * @throws Exception
	 */
	private static void opprettKonto(BankTest bt, BufferedReader consoleReader, Account activeKonto) throws Exception{
		System.out.println("Opprett konto..");
		
		System.out.println("Ønsket navn på kontoen:");
		String accountName = consoleReader.readLine();
		
		System.out.println("Ønsket PIN(4 siffer):");
		int PIN = Integer.parseInt(consoleReader.readLine());
		
		Account account = new Account(activeKonto.getKontoeier(), 0, false, accountName);
		Account r = bt.bank.CreateAccount(account, PIN);
		if(r != null){
			System.out.println("Suksess! Konto '" + r.getKontoNavn() 
			+ "' med kontonummer " + r.getKontonummer()
			+ " er opprettet.");
		}
		else{
			System.out.println("Ops--- Konto ble ikke opprettet.");
		}
	}
	/**
	 * Fetch all the {@link Transfer} to this {@link Account}
	 * @param bt
	 * @param consoleReader
	 * @param activeKonto
	 * @throws Exception
	 */
	private static void seTransaksjoner(BankTest bt, BufferedReader consoleReader, Account activeKonto) throws Exception{
		System.out.println("Transaksjoner for konto " + activeKonto.getKontonummer());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		System.out.println("Fra dato(format:dd/mm/yyyy):");
		Date fromDate = dateFormat.parse(consoleReader.readLine());
		System.out.println("Til dato(format:dd/mm/yyyy):");
		Date toDate = dateFormat.parse(consoleReader.readLine());

		List<Transfer> list = bt.bank.getTransfers(fromDate, toDate, activeKonto.getKontonummer());
		//List<Transfer> list = bt.bank.getAllTransfers(activeKonto.getKontonummer());
		if(list.size() > 0 ){
			for(Transfer t: list){
				System.out.println("Listing transferid " + t.getId() + ": " 
						+ "Type:" + t.getType() 
						+ " Konto:" + t.getKonto() 
						+ " Beløp:" + t.getBelop()
						+ " Detaljer:" + t.getDetaljer()
						+ " Saldo:" + t.getSaldo()
						+ " Timestamp:" + t.getTimestamp());
			}
		}
		else{
			System.out.println("Ingen overføringer funnet!");
		}
	}
	/**
	 * Make a deposit to this {@link Account}
	 * @param bt
	 * @param consoleReader
	 * @param activeKonto
	 * @param cardNumber
	 * @param pin
	 * @throws Exception
	 */
	private static void settInnskudd(BankTest bt, BufferedReader consoleReader, Account activeKonto, String cardNumber, String pin) 
			throws Exception{
		System.out.println("Innskudd..");
		System.out.println("Skriv inn summen du ønsker å sette inn:");
		double amount = Double.parseDouble(consoleReader.readLine());
		boolean r = bt.bank.Innskudd(activeKonto.getKontonummer(), amount);
		if(r){
			activeKonto = bt.getKonto(cardNumber, Integer.parseInt(pin));
			System.out.println("Innskudd er sukssess. Ny saldo er " + activeKonto.getSaldo());
		}else{
			System.out.println("Innskudd feilet!");
		}
	}
	/**
	 * Make a withdrawal from this {@link Account}
	 * @param bt
	 * @param consoleReader
	 * @param activeKonto
	 * @param cardNumber
	 * @param pin
	 * @throws Exception
	 */
	private static void settUttak(BankTest bt, BufferedReader consoleReader, Account activeKonto, String cardNumber, String pin) 
			throws Exception{
		System.out.println("Uttak..");
		System.out.println("Skriv inn summen du ønsker å ta ut(hele 100):");
		double amount = Double.parseDouble(consoleReader.readLine());
		boolean r = false;
		if(amount % 100 == 0){
			r = bt.bank.Uttak(activeKonto.getKontonummer(), amount);
		}

		if(r){
			activeKonto = bt.getKonto(cardNumber, Integer.parseInt(pin));
			System.out.println("Uttak er sukssess. Ny saldo er " + activeKonto.getSaldo());
		}else{
			System.out.println("Uttak feilet!");
		}
	}
	/**
	 * Transfer between {@link Accounts}
	 * @param bt
	 * @param consoleReader
	 * @param activeKonto
	 * @param cardNumber
	 * @param pin
	 * @throws Exception
	 */
	private static void settOverfor(BankTest bt, BufferedReader consoleReader, Account activeKonto, String cardNumber, String pin) 
			throws Exception{
		System.out.println("Overføring..");
		System.out.println("Skriv inn summen du ønsker å overføre:");
		double amount = Double.parseDouble(consoleReader.readLine());
		System.out.println("Skriv inn kontonummeret du ønsker å overføre til(må være ditt eget):");
		String toAccount = consoleReader.readLine();
		boolean r = bt.bank.Transfer(toAccount, activeKonto.getKontonummer(), amount);
		if(r){
			System.out.println("Overføring var vellykket!");
		}
		else{
			System.out.println("Overføring feilet..");
		}
	}
	/**
	 * Show test program GUI
	 */
	private static void showGUI() {
		System.out.println("\n\n");
		System.out.println("**********************");
	    System.out.println("Velkommen til Bank Test");
	    System.out.println("**********************");
	    System.out.print("Options \n1. List Person's\n2. List Konto's\n3. List Kort's\n4. List Transfer's\n5. Test Minibank\n6. Exit \nSkriv inn ditt valg: ");
		
	}
	/**
	 * Show ATM GUI
	 * @param cardNumber
	 */
	private static void showATMGUI(String cardNumber){
		System.out.println("\n\n");
		System.out.println("***************************************************");
	    System.out.println("Velkommen til minibank Test for kort " + cardNumber);
	    System.out.println("***************************************************");
	    System.out.print("Options \n1. Sjekk saldo\n2. Opprett konto\n3. Se Transaksjoner\n4. Innskudd\n5. Uttak\n6. Overfør mellom konti\n7. Exit \nSkriv inn ditt valg: ");
	}

}
