package siap.sius.statistiche.model;

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class ProcAggregatiCognomeModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 6638719265969049675L;
	private String mIniziale = null;
	private BigDecimal mTotale = null;

	public String getIniziale() {
		return this.mIniziale;
	}

	public void setIniziale(String aIniziale) {
		this.mIniziale = aIniziale;
	}

	public BigDecimal getTotale() {
		return this.mTotale;
	}

	public void setTotale(BigDecimal aTotale) {
		this.mTotale = aTotale;
	}

}
