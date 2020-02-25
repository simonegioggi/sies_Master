package siap.sige.tenore.model;

import java.util.Vector;

import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.sentenza.model.SentenzaSigeModel;

/**
 * <p>
 * Title: TenoreSigeEstesoModel
 * </p>
 * <p>
 * Description: Questo Classe Model raggruppa in un unico aggregato i dati collegati ad un oggetto SIGE, che
 * sono: TenoreSige, Sentenza, Reato.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 5.0
 */
@SuppressWarnings("rawtypes")
public class TenoreSigeEstesoModel extends TenoreSigeModel implements Comparable {

	/**
	 * [SG] 20190312: aggiunto id versione seriale e cambiata estensione del model
	 */
	private static final long serialVersionUID = 7679463605175316721L;

	private TenoreSigeModel mTenoreSige;
	private SentenzaModel mSentenza;
	private ReatoModel mReato;
	private SentenzaSigeModel mSentenzaSige; // 20/01/2010

	// Modifica del 23/11/2016 MEV_15_S4
	// Vettore contenente le Sentenze legate al Tenore Sige (Oggetto)
	private Vector<SentenzaSigeModel> mSentenzaSigeVector = new Vector<>();

	// COSTRUTTORE DI DEFAULT
	public TenoreSigeEstesoModel() {
		mTenoreSige = null;
		mSentenza = null;
		mReato = null;
		mSentenzaSige = null;
	}

	public TenoreSigeEstesoModel(TenoreSigeModel aTenoreSige) {
		this();
		if (aTenoreSige != null)
			mTenoreSige = new TenoreSigeModel(aTenoreSige);

		if (aTenoreSige.getIdSentenza() != null) {
			mSentenza = new SentenzaModel();
			mSentenza.setIdSentenza(aTenoreSige.getIdSentenza());
		}

		if (aTenoreSige.getIdReato() != null) {
			mReato = new ReatoModel();
			mReato.setIdReato(aTenoreSige.getIdReato());
		}
		// 20/01/2010
		if (aTenoreSige.getFasIdFascicoloSige() != null && aTenoreSige.getIdSentenza() != null) {
			mSentenzaSige = new SentenzaSigeModel();
			mSentenzaSige.setIdSentenza(aTenoreSige.getIdSentenza());
			mSentenzaSige.setFasIdFascicoloSige(aTenoreSige.getFasIdFascicoloSige());
		}

	}

	public TenoreSigeEstesoModel(TenoreSigeModel aTenoreSige, SentenzaModel aSentenza) {
		this(aTenoreSige);
		if (aSentenza != null)
			mSentenza = new SentenzaModel(aSentenza);
	}

	public TenoreSigeEstesoModel(TenoreSigeModel aTenoreSige, SentenzaModel aSentenza, ReatoModel aReato) {
		this(aTenoreSige, aSentenza);
		if (aReato != null)
			mReato = new ReatoModel(aReato);
	}

	// COSTRUTTORE DI COPIA
	public TenoreSigeEstesoModel(TenoreSigeEstesoModel aModel) {
		this(aModel.getTenoreSige(), aModel.getSentenza(), aModel.getReato());
	}

	// METODI GET()
	//
	public TenoreSigeModel getTenoreSige() {
		return mTenoreSige;
	}

	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public ReatoModel getReato() {
		return mReato;
	}

	public SentenzaSigeModel getSentenzaSige() {
		return mSentenzaSige;
	}

	// METODI SET()
	//
	public void setTenoreSige(TenoreSigeModel aValore) {
		mTenoreSige = aValore;
	}

	public void setSentenza(SentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setReato(ReatoModel aValore) {
		mReato = aValore;
	}

	public void setSentenzaSige(SentenzaSigeModel aValore) {
		mSentenzaSige = aValore;
	} // 20/01/2010

	public String toString() {
		String lRet = getClass().getName() + "\n";
		if (mTenoreSige != null)
			lRet += mTenoreSige.toString() + "\n";
		if (mSentenza != null)
			lRet += mSentenza.toString() + "\n";
		if (mReato != null)
			lRet += mReato.toString() + "\n";
		// 20/01/2010
		if (mSentenzaSige != null)
			lRet += mSentenzaSige.toString() + "\n";

		return lRet;
	}

	// Ordinamento per Tenore, Sentenza, Reato
	public int compareTo(Object o) {
		int lRet = 0;
		TenoreSigeEstesoModel lObj = (TenoreSigeEstesoModel) o;

		if (mTenoreSige == null || lObj.getTenoreSige() == null)
			throw new IllegalArgumentException("Tenore non valorizzato ");

		// Confronto tra codice oggetto Tenore
		lRet = (mTenoreSige.getCodOggettoSige().compareTo(lObj.getTenoreSige().getCodOggettoSige())) * 100;

		if (lRet == 0 && mSentenza != null && lObj.getSentenza() != null) {
			// Confronto tra ID Sentenza
			lRet = (mSentenza.getIdSentenza().toString()
					.compareTo(lObj.getSentenza().getIdSentenza().toString())) * 10;
			if (lRet == 0 && mReato != null && lObj.getReato() != null) {
				// Confronto tra ID Reato
				lRet = mReato.getIdReato().toString().compareTo(lObj.getReato().getIdReato().toString());
			}
		}
		return lRet;
	}

	public Vector<SentenzaSigeModel> getSentenzaSigeVector() {
		return mSentenzaSigeVector;
	}

	public void setSentenzaSigeVector(Vector<SentenzaSigeModel> mSentenzaSigeVector) {
		this.mSentenzaSigeVector = mSentenzaSigeVector;
	}

}