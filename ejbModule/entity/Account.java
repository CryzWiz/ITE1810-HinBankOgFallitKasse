package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * This class holds all the values regarding a {@link Person}'s account.
 * Note that a {@link Person} can hold several {@link Account}'s and
 * {@link Card}'s
 * @author allan
 */
@Entity
@Table(name = "Konto")
public class Account implements Serializable{
	private static final long serialVersionUID = 2042525156073565290L;

	@Column (name = "Id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int Id;
	
	@Column (name = "Kontoeier")
	private int Kontoeier;
	
	@Id
	@Column (name = "Kontonummer", length = 9)
	private String Kontonummer;
	
	@Column (name = "Opprettet")
	private Date Opprettet;
	
	@Column (name = "Saldo")
	private double Saldo;
	
	@Column (name = "KortReg")
	private boolean KortReg;
	
	@Column (name = "KontoAktiv")
	private boolean KontoAktiv;
	
	@Column (name = "KontoNavn")
	private String KontoNavn;
	
	public Account(){
		
	}
	
	public Account(int Id, int Kontoeier, String Kontonummer, Date Opprettet, double Saldo, boolean KortReg, String KontoNavn){
		setId(Id);
		setKontoeier(Kontoeier);
		setKontonummer(Kontonummer);
		setOpprettet(Opprettet);
		setSaldo(Saldo);
		setKortReg(KortReg);
		setKontoAktiv();
		setKontoNavn(KontoNavn);
	}
	
	public Account(int Kontoeier, String Kontonummer, Date Opprettet, double Saldo, boolean KortReg, String KontoNavn){
		setKontoeier(Kontoeier);
		setKontonummer(Kontonummer);
		setOpprettet(Opprettet);
		setSaldo(Saldo);
		setKortReg(KortReg);
		setKontoAktiv();
		setKontoNavn(KontoNavn);
	}

	public Account(int Kontoeier, String Kontonummer, Date Opprettet, double Saldo, boolean KortReg){
		setKontoeier(Kontoeier);
		setKontonummer(Kontonummer);
		setOpprettet(Opprettet);
		setSaldo(Saldo);
		setKortReg(KortReg);
		setKontoAktiv();
	}
	
	public Account(int Kontoeier, double Saldo, boolean KortReg, String KontoNavn){
		setKontoeier(Kontoeier);
		setSaldo(Saldo);
		setKortReg(KortReg);
		setKontoAktiv();
		setKontoNavn(KontoNavn);
	}
	
	public Account(int Kontoeier, double Saldo, boolean KortReg){
		setKontoeier(Kontoeier);
		setSaldo(Saldo);
		setKortReg(KortReg);
		setKontoAktiv();
	}
	
	public Account(int Kontoeier){
		setKontoeier(Kontoeier);
		setKontoAktiv();
	}
	
	public String getKontoNavn() {
		return KontoNavn;
	}

	public void setKontoNavn(String kontoNavn) {
		KontoNavn = kontoNavn;
	}
	
	private void setKontoAktiv() {
		KontoAktiv = true;
	}

	@SuppressWarnings("unused")
	private void deactivateKonto(){
		KontoAktiv = false;
	}
	
	public int getKontoeier() {
		return Kontoeier;
	}

	public void setKontoeier(int kontoeier) {
		Kontoeier = kontoeier;
	}

	public String getKontonummer() {
		return Kontonummer;
	}

	public void setKontonummer(String kontonummer) {
		Kontonummer = kontonummer;
	}

	public Date getOpprettet() {
		return Opprettet;
	}

	public void setOpprettet(Date opprettet) {
		Opprettet = opprettet;
	}

	public double getSaldo() {
		return Saldo;
	}

	public void setSaldo(double saldo) {
		Saldo = saldo;
	}

	public void setKortReg(boolean kortreg) {
		KortReg = kortreg;
	}
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public boolean isKortReg() {
		return KortReg;
	}
}
