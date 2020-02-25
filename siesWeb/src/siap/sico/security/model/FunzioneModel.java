package siap.sico.security.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import f3b.model.GenericModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FunzioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5049797990645614059L;

	private BigDecimal mIdFunzione;
	private String mDescrizione;
	private String mAzioneContestoJava;
	private String mLabelFunzione;
	private String mCodTipoFunzione;
	private BigDecimal mOrdineVisualizzazione;
	private String mCodTipoVisualizzazione;

	private LinkedList mFunzioniAntenate;
	private ArrayList mFunzioniFiglie;

	// COSTRUTTORE DI DEFAULT
	public FunzioneModel() {
		this.mIdFunzione = null;
		this.mDescrizione = "";
		this.mAzioneContestoJava = "";
		this.mLabelFunzione = "";
		this.mCodTipoFunzione = "";
		this.mOrdineVisualizzazione = null;
		this.mCodTipoVisualizzazione = "";

		this.mFunzioniAntenate = new LinkedList();
		this.mFunzioniFiglie = new ArrayList();
	}

	// COSTRUTTORE
	public FunzioneModel(BigDecimal aIdFunzione) {
		this.mIdFunzione = aIdFunzione;
		this.mDescrizione = "";
		this.mAzioneContestoJava = "";
		this.mLabelFunzione = "";
		this.mCodTipoFunzione = "";
		this.mOrdineVisualizzazione = null;
		this.mCodTipoVisualizzazione = "";

		this.mFunzioniAntenate = new LinkedList();
		this.mFunzioniFiglie = new ArrayList();
	}

	// COSTRUTTORE MODEL
	public FunzioneModel(BigDecimal aIdFunzione, String aDescrizione, String aAzioneContestoJava,
			String aLabelFunzione, String aCodTipoFunzione, BigDecimal aOrdineVisualizzazione,
			String aCodTipoVisualizzazione) {
		this.mIdFunzione = aIdFunzione;
		this.mDescrizione = aDescrizione;
		this.mAzioneContestoJava = aAzioneContestoJava;
		this.mLabelFunzione = aLabelFunzione;
		this.mCodTipoFunzione = aCodTipoFunzione;
		this.mOrdineVisualizzazione = aOrdineVisualizzazione;
		this.mCodTipoVisualizzazione = aCodTipoVisualizzazione;

		this.mFunzioniAntenate = new LinkedList();
		this.mFunzioniFiglie = new ArrayList();
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdFunzione() {
		return mIdFunzione;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	public String getAzioneContestoJava() {
		return mAzioneContestoJava;
	}

	public String getLabelFunzione() {
		return mLabelFunzione;
	}

	public String getCodTipoFunzione() {
		return mCodTipoFunzione;
	}

	public BigDecimal getOrdineVisualizzazione() {
		return mOrdineVisualizzazione;
	}

	public String getCodTipoVisualizzazione() {
		return mCodTipoVisualizzazione;
	}

	public LinkedList getFunzioniAntenate() {
		return mFunzioniAntenate;
	}

	public ArrayList getFunzioniFiglie() {
		return mFunzioniFiglie;
	}

	public int getNumFigli() {
		return mFunzioniFiglie.size();
	}

	public FunzioneModel getFunzioneModel() {
		return new FunzioneModel(mIdFunzione, mDescrizione, mAzioneContestoJava, mLabelFunzione,
				mCodTipoFunzione, mOrdineVisualizzazione, mCodTipoVisualizzazione);
	}

	//
	// METODI SET()
	//
	public void setIdFunzione(BigDecimal aValore) {
		mIdFunzione = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	public void setAzioneContestoJava(String aValore) {
		mAzioneContestoJava = aValore;
	}

	public void setLabelFunzione(String aValore) {
		mLabelFunzione = aValore;
	}

	public void setCodTipoFunzione(String aValore) {
		mCodTipoFunzione = aValore;
	}

	public void setOrdineVisualizzazione(BigDecimal aValore) {
		mOrdineVisualizzazione = aValore;
	}

	public void setCodTipoVisualizzazione(String aValore) {
		mCodTipoVisualizzazione = aValore;
	}

	public void setFunzioniAntenate(LinkedList aValore) {
		mFunzioniAntenate = aValore;
	}

	public void setFunzioniFiglie(ArrayList aValore) {
		mFunzioniFiglie = aValore;
	}

	public void addAntenato(FunzioneModel aFunzionePadre) {
		mFunzioniAntenate.addLast(aFunzionePadre);
	}

	public boolean esisteFunzione(BigDecimal aIdFunzione) {
		if ((mFunzioniFiglie == null) || (mFunzioniFiglie.size() == 0))
			return false;

		FunzioneModel lFun = new FunzioneModel();
		lFun.setIdFunzione(aIdFunzione);

		Iterator lIt = mFunzioniFiglie.iterator();
		FunzioneModel lFunFiglia = null;

		// per ogni funzione figlia
		while (lIt.hasNext()) {
			lFunFiglia = (FunzioneModel) lIt.next();

			// verifica l'uguaglianza
			if (lFun.equals(lFunFiglia))
				return true;

			// verifica le funzioni figlie
			if (lFunFiglia.esisteFunzione(aIdFunzione))
				return true;
		}

		return false;
	}

	public String toString() {
		String lToString = this.mIdFunzione + " - " + this.mDescrizione + " - " + this.mAzioneContestoJava
				+ " - " + this.mLabelFunzione + " - " + this.mCodTipoFunzione + " - "
				+ this.mOrdineVisualizzazione + " - " + this.mCodTipoVisualizzazione;

		return lToString;
	}

	public boolean equals(Object aObj) {
		boolean lIsEquals = false;

		if (aObj != null && aObj instanceof FunzioneModel) {
			FunzioneModel lModel = (FunzioneModel) aObj;

			lIsEquals = mIdFunzione.equals(lModel.getIdFunzione());
		}

		return lIsEquals;
	}

}