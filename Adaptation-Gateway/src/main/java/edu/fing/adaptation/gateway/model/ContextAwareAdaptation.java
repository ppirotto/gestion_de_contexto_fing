package edu.fing.adaptation.gateway.model;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import edu.fing.commons.constant.AdaptationType;

@Entity
@Table(name = "CONTEXT_AWARE_ADAPTATION")
public class ContextAwareAdaptation {

	public static final Comparator<ContextAwareAdaptation> ORDER_COMPARATOR = new Comparator<ContextAwareAdaptation>() {

		@Override
		public int compare(ContextAwareAdaptation adap1, ContextAwareAdaptation adap2) {
			return adap1.getOrder() - adap2.getOrder();
		}
	};

	private Long id;
	private AdaptationType adaptationType;
	private int order;
	private String data;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ADAPTATION_TYPE")
	public AdaptationType getAdaptationType() {
		return adaptationType;
	}

	@Column(name = "ADAPTATION_ORDER")
	public int getOrder() {
		return order;
	}

	@Lob
	@Column(name = "DATA", columnDefinition = "MEDIUMTEXT")
	public String getData() {
		return data;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAdaptationType(AdaptationType adaptationType) {
		this.adaptationType = adaptationType;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
