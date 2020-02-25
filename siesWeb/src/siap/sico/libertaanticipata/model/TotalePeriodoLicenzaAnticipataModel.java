package siap.sico.libertaanticipata.model;

import f3b.model.GenericModel;

public class TotalePeriodoLicenzaAnticipataModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5827652532993699088L;

	private int mTotaleGiorniDaConcedere;
	private int mTotaleGiorniConcessi;

	public TotalePeriodoLicenzaAnticipataModel() {
		mTotaleGiorniDaConcedere = 0;
		mTotaleGiorniConcessi = 0;
	}

	public int getTotaleGiorniDaConcedere() {
		return mTotaleGiorniDaConcedere;
	}

	public int getTotaleGiorniConcessi() {
		return mTotaleGiorniConcessi;
	}

	public void setTotaleGiorniDaConcedere(int aValore) {
		mTotaleGiorniDaConcedere = aValore;
	}

	public void setTotaleGiorniConcessi(int aValore) {
		mTotaleGiorniConcessi = aValore;
	}

}