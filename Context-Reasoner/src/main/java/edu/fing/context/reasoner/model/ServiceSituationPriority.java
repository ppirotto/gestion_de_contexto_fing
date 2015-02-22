package edu.fing.context.reasoner.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "SERVICE_SITUATION_PRIORITY", uniqueConstraints = { @UniqueConstraint(columnNames = { "SITUATION_ID", "SERVICE_ID" }) })
public class ServiceSituationPriority {

	private Long id;
	private int priority;
	private Situation situation;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Column(name = "PRIORITY", nullable = false)
	public int getPriority() {
		return priority;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "SITUATION_ID", nullable = false)
	public Situation getSituation() {
		return situation;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setSituation(Situation situation) {
		this.situation = situation;
	}

}
