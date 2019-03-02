package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
/**
 * This class holds all the values regarding a {@link Person}
 * Also knows as a customer of the bank.
 * - Borrowed from the training exercise
 *
 */
@Entity
@Table(name = "Person")
public class Person implements Serializable {
	private static final long serialVersionUID = -7691090994283729764L;
	
	@Column (name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	@Column (name = "Fodselsdato")
	private Date Fodselsdato;
	@Column (name = "Fodselsnummer", length = 5)
	private String Fodselsnummer;
	@Column (name = "Navn", length = 30)
	private String Navn;
	@Column (name = "Addresselinje_1", length = 30)
	private String Addresseline_1;
	@Column (name = "Addresselinje_2", length = 30)
	private String Addresselinje_2;
	@Column (name = "Postnummer", length = 4)
	private String Postnummer;
	@Column (name = "Poststed", length = 35)
	private String Poststed;
	@Column (name = "Passord")
	private String Passord;
	@Column (name = "PIN", length = 4)
	private String PIN;
	@Column ( name = "Kommentar", length = 200)
	private String Kommentar;
	
	/**
	 * Konstruktører for Person
	 */
	public Person(int id, Date Fodselsdato, String Fodselsnummer, String Navn,
			String Addresselinje_1, String Addresselinje_2, String Postnummer, 
			String Poststed, String Passord, String PIN, String Kommentar){
		setId(id);
		setFodselsdato(Fodselsdato);
		setNavn(Navn);
		setAddresselinje_1(Addresselinje_1);
		setAddresselinje_2(Addresselinje_2);
		setPostnummer(Postnummer);
		setPoststed(Poststed);
		setPassord(Passord);
		setPIN(PIN);
		setKommentar(Kommentar);
	}
	
	public Person(Date Fodselsdato, String Fodselsnummer, String Navn,
			String Addresselinje_1, String Addresselinje_2, String Postnummer, 
			String Poststed, String Passord, String PIN, String Kommentar){
		setFodselsdato(Fodselsdato);
		setFodselsnummer(Fodselsnummer);
		setNavn(Navn);
		setAddresselinje_1(Addresselinje_1);
		setAddresselinje_2(Addresselinje_2);
		setPostnummer(Postnummer);
		setPoststed(Poststed);
		setPassord(Passord);
		setPIN(PIN);
		setKommentar(Kommentar);
	}
	public Person(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFodselsdato() {
		return Fodselsdato;
	}

	public void setFodselsdato(Date fodselsdato) {
		Fodselsdato = fodselsdato;
	}

	public String getFodselsnummer() {
		return Fodselsnummer;
	}

	public void setFodselsnummer(String fodselsnummer) {
		Fodselsnummer = fodselsnummer;
	}

	public String getNavn() {
		return Navn;
	}

	public void setNavn(String navn) {
		Navn = navn;
	}

	public String getAddresselinje_1() {
		return Addresseline_1;
	}

	public void setAddresselinje_1(String addresseline_1) {
		Addresseline_1 = addresseline_1;
	}

	public String getAddresselinje_2() {
		return Addresselinje_2;
	}

	public void setAddresselinje_2(String addresselinje_2) {
		Addresselinje_2 = addresselinje_2;
	}

	public String getPostnummer() {
		return Postnummer;
	}

	public void setPostnummer(String postnummer) {
		Postnummer = postnummer;
	}

	public String getPoststed() {
		return Poststed;
	}

	public void setPoststed(String poststed) {
		Poststed = poststed;
	}

	public String getPassord() {
		return Passord;
	}

	public void setPassord(String passord) {
		Passord = passord;
	}

	public String getPIN() {
		return PIN;
	}

	public void setPIN(String pIN) {
		PIN = pIN;
	}

	public String getKommentar() {
		return Kommentar;
	}

	public void setKommentar(String kommentar) {
		Kommentar = kommentar;
	}
	
	
}

