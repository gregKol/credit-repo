package pl.credit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "credit")

@Entity
@Table(name = "credit", schema = "creditdb")
public class Credit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "credit_id")
	private final int credit_id;

	@Transient
	static int numberSeq = 100;

	public Credit() {
		credit_id = numberSeq++;
	}

	public Long getId() {
		return id;
	}

	public int getCredit_id() {
		return credit_id;
	}

	public static int getNumberSeq() {
		return numberSeq;
	}

	@Override
	public String toString() {
		return "GenerateNumber [id=" + id + ", credit_id=" + credit_id + ", getId()=" + getId() + ", getCredit_id()=" + getCredit_id()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
