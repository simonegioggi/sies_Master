package siap.sius.statistiche.model;

/**
* <p>Title: IspConteggioOggettiModel</p>
* <p>Description: Classe Model che rappresenta il IspConteggioOggetti</p>
* Il model rappresenta i conteggi statistici effettuati dalla funzione Statistica di "Estrazione Oggetti SIUS",
* e viene utilizzato per generare il report.
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;
import f3b.util.F3BException;

/**
 * Classe rappresentativa dei conteggi statistici relativi a provvedimenti di uno specifico oggetto.
 * 
 * @author Lesposito
 *
 */
public class IspConteggioOggettiModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 6909180550460983932L;
	private String mCodOggetto;
	private String mDescOggetto;
	private String mDescContenutoStatis;
	private BigDecimal mNumPendentiInizio;
	private BigDecimal mNumSopravvenuti;
	private BigDecimal mNumDefEsito1;
	private BigDecimal mNumDefEsito2;
	private BigDecimal mNumDefEsito3;
	private BigDecimal mNumDefEsito4;
	private BigDecimal mNumDefEsito5;
	private BigDecimal mNumDefEsito6;
	private BigDecimal mNumDefIscErr;
	private BigDecimal mNumPendentiFine;
	private String mFasSiuChiaveUfficio; /* mod michele 2/12/2008 */
	private BigDecimal mNumCancellati;
	private BigDecimal mNumUnificati;
	
	// COSTRUTTORE DI DEFAULT
	public IspConteggioOggettiModel() {
		mCodOggetto = "";
		mDescOggetto = "";
		mDescContenutoStatis = "";
		mNumPendentiInizio = null;
		mNumSopravvenuti = null;
		mNumDefEsito1 = null;
		mNumDefEsito2 = null;
		mNumDefEsito3 = null;
		mNumDefEsito4 = null;
		mNumDefEsito5 = null;
		mNumDefEsito6 = null;
		mNumDefIscErr = null;
		mNumPendentiFine = null;
		mFasSiuChiaveUfficio = ""; /* mod michele 2/12/2008 */
		mNumCancellati = null;
		mNumUnificati = null;
	}

	// COSTRUTTORE DI COPIA
	public IspConteggioOggettiModel(IspConteggioOggettiModel aModel) {
		mCodOggetto = aModel.mCodOggetto;
		mDescOggetto = aModel.mDescOggetto;
		mDescContenutoStatis = aModel.mDescContenutoStatis;
		mNumPendentiInizio = aModel.mNumPendentiInizio;
		mNumSopravvenuti = aModel.mNumSopravvenuti;
		mNumDefEsito1 = aModel.mNumDefEsito1;
		mNumDefEsito2 = aModel.mNumDefEsito2;
		mNumDefEsito3 = aModel.mNumDefEsito3;
		mNumDefEsito4 = aModel.mNumDefEsito4;
		mNumDefEsito5 = aModel.mNumDefEsito5;
		mNumDefEsito6 = aModel.mNumDefEsito6;
		mNumDefIscErr = aModel.mNumDefIscErr;
		mNumPendentiFine = aModel.mNumPendentiFine;
		mFasSiuChiaveUfficio = aModel.mFasSiuChiaveUfficio; /* mod michele 2/12/2008 */
		mNumCancellati = aModel.mNumCancellati;
		mNumUnificati = aModel.mNumUnificati;
	}

	// COSTRUTTORE MODEL
	public IspConteggioOggettiModel(String aCodOggetto, String aDescOggetto, String aDescContenutoStatis,
			BigDecimal aNumPendentiInizio, BigDecimal aNumSopravvenuti, BigDecimal aNumDefEsito1,
			BigDecimal aNumDefEsito2, BigDecimal aNumDefEsito3, BigDecimal aNumDefEsito4,
			BigDecimal aNumDefEsito5, BigDecimal aNumDefEsito6, BigDecimal aNumDefIscErr,
			BigDecimal aNumPendentiFine, String aFasSiuChiaveUfficio, /* mod michele 2/12/2008 */
			BigDecimal aNumCancellati, BigDecimal aNumUnificati) {
		mCodOggetto = aCodOggetto;
		mDescOggetto = aDescOggetto;
		mDescContenutoStatis = aDescContenutoStatis;
		mNumPendentiInizio = aNumPendentiInizio;
		mNumSopravvenuti = aNumSopravvenuti;
		mNumDefEsito1 = aNumDefEsito1;
		mNumDefEsito2 = aNumDefEsito2;
		mNumDefEsito3 = aNumDefEsito3;
		mNumDefEsito4 = aNumDefEsito4;
		mNumDefEsito5 = aNumDefEsito5;
		mNumDefEsito6 = aNumDefEsito6;
		mNumDefIscErr = aNumDefIscErr;
		mNumPendentiFine = aNumPendentiFine;
		mFasSiuChiaveUfficio = aFasSiuChiaveUfficio; /* mod michele 2/12/2008 */
		mNumCancellati = aNumCancellati;
		mNumUnificati = aNumUnificati;
	}

	//
	// METODI GET()
	//

	public String getCodOggetto() {
		return mCodOggetto;
	}

	public String getDescOggetto() {
		return mDescOggetto;
	}

	public String getDescContenutoStatis() {
		return mDescContenutoStatis;
	}

	public BigDecimal getNumPendentiInizio() {
		return mNumPendentiInizio;
	}

	public BigDecimal getNumSopravvenuti() {
		return mNumSopravvenuti;
	}

	public BigDecimal getNumDefEsito1() {
		return mNumDefEsito1;
	}

	public BigDecimal getNumDefEsito2() {
		return mNumDefEsito2;
	}

	public BigDecimal getNumDefEsito3() {
		return mNumDefEsito3;
	}

	public BigDecimal getNumDefEsito4() {
		return mNumDefEsito4;
	}

	public BigDecimal getNumDefEsito5() {
		return mNumDefEsito5;
	}

	public BigDecimal getNumDefEsito6() {
		return mNumDefEsito6;
	}

	public BigDecimal getNumDefIscErr() {
		return mNumDefIscErr;
	}

	public BigDecimal getNumPendentiFine() {
		return mNumPendentiFine;
	}

	public String getFasSiuChiaveUfficio() {
		return mFasSiuChiaveUfficio;
	} /* mod michele 2/12/2008 */

	public BigDecimal getNumCancellati() {
		return mNumCancellati;
	}

	public BigDecimal getNumUnificati() {
		return mNumUnificati;
	}

	//
	// METODI SET()
	//

	public void setCodOggetto(String aValore) {
		mCodOggetto = aValore;
	}

	public void setDescOggetto(String aValore) {
		mDescOggetto = aValore;
	}

	public void setDescContenutoStatis(String aValore) {
		mDescContenutoStatis = aValore;
	}

	public void setNumPendentiInizio(BigDecimal aValore) {
		mNumPendentiInizio = aValore;
	}

	public void setNumSopravvenuti(BigDecimal aValore) {
		mNumSopravvenuti = aValore;
	}

	public void setNumDefEsito1(BigDecimal aValore) {
		mNumDefEsito1 = aValore;
	}

	public void setNumDefEsito2(BigDecimal aValore) {
		mNumDefEsito2 = aValore;
	}

	public void setNumDefEsito3(BigDecimal aValore) {
		mNumDefEsito3 = aValore;
	}

	public void setNumDefEsito4(BigDecimal aValore) {
		mNumDefEsito4 = aValore;
	}

	public void setNumDefEsito5(BigDecimal aValore) {
		mNumDefEsito5 = aValore;
	}

	public void setNumDefEsito6(BigDecimal aValore) {
		mNumDefEsito6 = aValore;
	}

	public void setNumDefIscErr(BigDecimal aValore) {
		mNumDefIscErr = aValore;
	}

	public void setNumPendentiFine(BigDecimal aValore) {
		mNumPendentiFine = aValore;
	}

	public void setFasSiuChiaveUfficio(String aValore) {
		mFasSiuChiaveUfficio = aValore;
	} /* mod michele 2/12/2008 */

	public void setNumCancellati(BigDecimal aValore) {
		mNumCancellati = aValore;
	}

	public void setNumUnificati(BigDecimal aValore) {
		mNumUnificati = aValore;
	}

	/**
	 * Somma tra due model. E' possibile sommare solo due IspConteggioOggettiModel con lo stesso contenuto. La
	 * funzione viene utilizzata per ottenere la Statistica Aggregata a partire da quella Dettagliata.
	 */
	public IspConteggioOggettiModel add(IspConteggioOggettiModel aModel) throws F3BException {
		// Model risultato della somma
		IspConteggioOggettiModel lAppoggioMod = new IspConteggioOggettiModel(this);

		if (aModel != null) {
			// Si sommano solo oggetti con lo stesso CONTENUTO
			if (aModel.getDescContenutoStatis().compareTo(mDescContenutoStatis) == 0) {
				lAppoggioMod.setNumPendentiInizio(mNumPendentiInizio.add(aModel.getNumPendentiInizio()));
				lAppoggioMod.setNumSopravvenuti(mNumSopravvenuti.add(aModel.getNumSopravvenuti()));
				lAppoggioMod.setNumDefEsito1(mNumDefEsito1.add(aModel.getNumDefEsito1()));
				lAppoggioMod.setNumDefEsito2(mNumDefEsito2.add(aModel.getNumDefEsito2()));
				lAppoggioMod.setNumDefEsito3(mNumDefEsito3.add(aModel.getNumDefEsito3()));
				lAppoggioMod.setNumDefEsito4(mNumDefEsito4.add(aModel.getNumDefEsito4()));
				lAppoggioMod.setNumDefEsito5(mNumDefEsito5.add(aModel.getNumDefEsito5()));
				lAppoggioMod.setNumDefEsito6(mNumDefEsito6.add(aModel.getNumDefEsito6()));
				lAppoggioMod.setNumDefIscErr(mNumDefIscErr.add(aModel.getNumDefIscErr()));
				lAppoggioMod.setNumPendentiFine(mNumPendentiFine.add(aModel.getNumPendentiFine()));
				lAppoggioMod.setNumCancellati(mNumCancellati.add(aModel.getNumCancellati()));
				lAppoggioMod.setNumUnificati(mNumUnificati.add(aModel.getNumUnificati()));
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile sommare 2 IspConteggioOggettiModel con contenuto diverso !");

		}
		return lAppoggioMod;
	}

}
