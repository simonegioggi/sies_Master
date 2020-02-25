package it.mig.sies.model;

import java.util.Date;

/**
 * SIES FASE 2 - Classe model relativa al periodo di libert� anticipata associata ad un provvedimento
 * dell'esecuzione
 * 
 * @author Federico Paparoni
 */
public class PeriodoLibertaAnticipata extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2098491058152033618L;

	private Date dataLibertaAnticipataInizio;
	private Date dataLibertaAnticipataFine;
	private String statoPermesso;
	private int numeroGiorni;

	/**
	 * @return the dataLibertaAnticipataInizio
	 */
	public Date getDataLibertaAnticipataInizio() {
		return dataLibertaAnticipataInizio;
	}

	/**
	 * @param dataLibertaAnticipataInizio
	 *            the dataLibertaAnticipataInizio to set
	 */
	public void setDataLibertaAnticipataInizio(Date dataLibertaAnticipataInizio) {
		this.dataLibertaAnticipataInizio = dataLibertaAnticipataInizio;
	}

	/**
	 * @return the dataLibertaAnticipataFine
	 */
	public Date getDataLibertaAnticipataFine() {
		return dataLibertaAnticipataFine;
	}

	/**
	 * @param dataLibertaAnticipataFine
	 *            the dataLibertaAnticipataFine to set
	 */
	public void setDataLibertaAnticipataFine(Date dataLibertaAnticipataFine) {
		this.dataLibertaAnticipataFine = dataLibertaAnticipataFine;
	}

	public String getStatoPermesso() {
		return statoPermesso;
	}

	public void setStatoPermesso(String statoPermesso) {
		this.statoPermesso = statoPermesso;
	}

	public int getNumeroGiorni() {
		return numeroGiorni;
	}

	public void setNumeroGiorni(int numeroGiorni) {
		this.numeroGiorni = numeroGiorni;
	}

}