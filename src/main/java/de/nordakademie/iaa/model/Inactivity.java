package de.nordakademie.iaa.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="INACTIVE_MEMBERSHIP")
public class Inactivity implements Serializable {

	private static final long serialVersionUID = 664095487411372637L;

	@Id
	private Long number;
	
	@Basic(optional=false)
	private LocalDate cancellationDate;
	@Basic
	private LocalDate exitDate;
	
	@OneToOne
	@MapsId
	private Member refMember;
	
	public Member getRefMember() {
		return refMember;
	}
	public void setRefMember(Member refMember) {
		this.refMember = refMember;
	}
	public LocalDate getCancellationDate() {
		return cancellationDate;
	}
	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
	public LocalDate getExitDate() {
		return exitDate;
	}
	public void setExitDate(LocalDate exitDate) {
		this.exitDate = exitDate;
	}
}
