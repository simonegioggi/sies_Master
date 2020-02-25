package siap.web;

import java.util.Vector;

@SuppressWarnings("rawtypes")
public class PaginazioneModel {

	private int mInizioRicerca;
	private int mFineRicerca;
	private int mNumeroPagina;
	private int mNumeroTotalePagine;

	private Vector mRisultatiRicerca;

	public PaginazioneModel() {
	}

	public int getInizioRicerca() {
		return mInizioRicerca;
	}

	public int getFineRicerca() {
		return mFineRicerca;
	}

	public int getNumeroPagina() {
		return mNumeroPagina;
	}

	public int getNumeroTotalePagine() {
		return mNumeroTotalePagine;
	}

	public Vector getRisultatiRicerca() {
		return mRisultatiRicerca;
	}

	public void setInizioRicerca(int aValore) {
		mInizioRicerca = aValore;
	}

	public void setFineRicerca(int aValore) {
		mFineRicerca = aValore;
	}

	public void setNumeroPagina(int aValore) {
		mNumeroPagina = aValore;
	}

	public void setNumeroTotalePagine(int aValore) {
		mNumeroTotalePagine = aValore;
	}

	public void setRisultatiRicerca(Vector aValore) {
		mRisultatiRicerca = aValore;
	}

	public String toString() {
		String lString = "";
		lString += mInizioRicerca + "-" + mFineRicerca + "-" + mNumeroPagina + "-" + mNumeroTotalePagine;

		return lString;
	}

}