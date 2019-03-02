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
 * This class holds all the values regarding a {@link Transfer}
 * between {@link Account}'s or withdraws/deposits.
 * @author allan
 */
@Entity
@Table(name = "Transfer")
public class Transfer implements Serializable{
	private static final long serialVersionUID = 1L;

	@Column (name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@Column( name = "Type")
	private String Type;
	
	@Column (name = "Konto")
	private String Konto;
	
	@Column (name = "tilKonto")
	private String tilKonto;
	
	@Column (name = "Belop")
	private double Belop;
	
	@Column (name = "Detaljer")
	private String Detaljer;
	
	@Column (name = "Saldo")
	private double Saldo;
	
	@Column (name = "Timestamp")
	private Date Timestamp;
	
	public Transfer(){
	}
	
	public Transfer(long id, String Type, String Konto, String tilKonto, 
			double Belop, String Detaljer, double saldo, Date Timestamp){
		setId(id);
		setType(Type);
		setKonto(Konto);
		setTilKonto(tilKonto);
		setBelop(Belop);
		setDetaljer(Detaljer);
		setSaldo(saldo);
		setTimestamp(Timestamp);
	}

	public Transfer(String Type, String Konto, String tilKonto, double belop, String Detaljer, double saldo, Date Timestamp){
		setType(Type);
		setKonto(Konto);
		setTilKonto(tilKonto);
		setBelop(belop);
		setDetaljer(Detaljer);
		setSaldo(saldo);
		setTimestamp(Timestamp);
	}
	
	public Transfer(String Type, String Konto, String tilKonto, double belop, Date Timestamp){
		setType(Type);
		setKonto(Konto);
		setTilKonto(tilKonto);
		setBelop(belop);
		setTimestamp(Timestamp);
	}
	
	public Transfer(String Type, String Konto, double belop, String Detaljer, double saldo, Date Timestamp){
		setType(Type);
		setKonto(Konto);
		setBelop(belop);
		setTimestamp(Timestamp);
		setSaldo(saldo);
		setDetaljer(Detaljer);
	}
	
	public double getSaldo() {
		return Saldo;
	}

	public void setSaldo(double saldo) {
		Saldo = saldo;
	}

	public double getBelop() {
		return Belop;
	}

	public void setBelop(double belop) {
		Belop = belop;
	}
	
	public Date getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(Date timestamp) {
		Timestamp = timestamp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getKonto() {
		return Konto;
	}

	public void setKonto(String konto) {
		Konto = konto;
	}

	public String getTilKonto() {
		return tilKonto;
	}

	public void setTilKonto(String tilKonto) {
		this.tilKonto = tilKonto;
	}

	public String getDetaljer() {
		return Detaljer;
	}

	public void setDetaljer(String Detaljer) {
		this.Detaljer = Detaljer;
	}
}

