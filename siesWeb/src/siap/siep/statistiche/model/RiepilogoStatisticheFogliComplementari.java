package siap.siep.statistiche.model;

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class RiepilogoStatisticheFogliComplementari extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5446076624275154642L;
	
	private BigDecimal anno=null;
	private BigDecimal conteggio=null;
	private String descrizione=null;
	
	
	public BigDecimal getAnno() {
		return anno;
	}
	public void setAnno(BigDecimal anno) {
		this.anno = anno;
	}
	public BigDecimal getConteggio() {
		return conteggio;
	}
	public void setConteggio(BigDecimal conteggio) {
		this.conteggio = conteggio;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
