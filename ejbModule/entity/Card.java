package entity;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
/**
 * This class contains all the values regarding a {@link Account}'s atm-card
 * @author allan
 */
@Entity
@Table(name = "Card")
public class Card implements Serializable{
	private static final long serialVersionUID = 1L;

	@Column (name = "Kortnummer")
	@Id
	private String Kortnummer;
	
	@Column (name = "Opprettet")
	private Date Opprettet;
	
	@Column (name = "Aktive")
	private boolean Aktive;
	
	@Column (name = "PIN")
	private int PIN;
	
	public Card(){
		
	}
	
	public Card(String Kortnummer, Date Opprettet, boolean Aktive, int PIN){
		setKortnummer(Kortnummer);
		setOpprettet(Opprettet);
		setAktive(Aktive);
		setPIN(PIN);
	}
	
	public Card(String Kortnummer, int PIN){
		setKortnummer(Kortnummer);
		setPIN(PIN);
		setOpprettet(new Date());
		setAktive(true);
	}
	
	public String getKortnummer() {
		return Kortnummer;
	}

	public void setKortnummer(String kortnummer) {
		Kortnummer = kortnummer;
	}

	public Date getOpprettet() {
		return Opprettet;
	}

	public void setOpprettet(Date opprettet) {
		Opprettet = opprettet;
	}

	public boolean isAktive() {
		return Aktive;
	}

	public void setAktive(boolean aktive) {
		Aktive = aktive;
	}

	public int getPIN() {
		return PIN;
	}

	public void setPIN(int pin) {
		PIN = pin;
	}
}

