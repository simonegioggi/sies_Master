package siap.sius.statistiche.model;

/**
* <p>Title: IspConteggioRelatoriModel</p>
* <p>Description: Classe Model che rappresenta il IspConteggioRelatori</p>
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
public class IspConteggioRelatoriModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -887769960031780862L;
	private String mCodRelatore;
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
	private String mFasSiuChiaveUfficio;
	private BigDecimal mNumCancellati;
	private BigDecimal mNumUnificati;
	private BigDecimal mNumAppProvv; // MEV9

	// COSTRUTTORE DI DEFAULT
	public IspConteggioRelatoriModel() {
		mCodRelatore = "";
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
		mFasSiuChiaveUfficio = "";
		mNumCancellati = null;
		mNumUnificati = null;
		mNumAppProvv = null; //MEV9
	}

	// COSTRUTTORE DI COPIA
	public IspConteggioRelatoriModel(IspConteggioRelatoriModel aModel) {
		mCodRelatore = aModel.mCodRelatore;
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
		mFasSiuChiaveUfficio = aModel.mFasSiuChiaveUfficio;
		mNumCancellati = aModel.mNumCancellati;
		mNumUnificati = aModel.mNumUnificati; 
		mNumAppProvv = aModel.mNumAppProvv; //MEV9
	}

	// COSTRUTTORE MODEL
	public IspConteggioRelatoriModel(String aCodRelatore, String aDescContenutoStatis,
			BigDecimal aNumPendentiInizio, BigDecimal aNumSopravvenuti, BigDecimal aNumDefEsito1,
			BigDecimal aNumDefEsito2, BigDecimal aNumDefEsito3, BigDecimal aNumDefEsito4,
			BigDecimal aNumDefEsito5, BigDecimal aNumDefEsito6, BigDecimal aNumDefIscErr,
			BigDecimal aNumPendentiFine, String aFasSiuChiaveUfficio, BigDecimal aNumCancellati,
			BigDecimal aNumUnificati,
			BigDecimal aNumAppProvv // MEV9
			) {
		mCodRelatore = aCodRelatore;
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
		mFasSiuChiaveUfficio = aFasSiuChiaveUfficio;
		mNumCancellati = aNumCancellati;
		mNumUnificati = aNumUnificati;
		mNumAppProvv = aNumAppProvv; // MEV9
	}

	//
	// METODI GET()
	//

	public String getCodRelatore() {
		return mCodRelatore;
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
	}

	public BigDecimal getNumCancellati() {
		return mNumCancellati;
	}

	public BigDecimal getNumUnificati() {
		return mNumUnificati;
	}
	
	// MEV9
	public BigDecimal getNumAppProvv() {
		return mNumAppProvv;
	}

	//
	// METODI SET()
	//

	public void setCodRelatore(String aValore) {
		mCodRelatore = aValore;
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
	}

	public void setNumCancellati(BigDecimal aValore) {
		mNumCancellati = aValore;
	}

	public void setNumUnificati(BigDecimal aValore) {
		mNumUnificati = aValore;
	}

	// MEV9
	public void setNumAppProvv(BigDecimal aValore) {
		mNumAppProvv = aValore;
	}
	
	
	
	/**
	 * Somma tra due model. E' possibile sommare solo due IspConteggioRelatoriModel con lo stesso contenuto.
	 * La funzione viene utilizzata per ottenere la Statistica Aggregata a partire da quella Dettagliata.
	 */
	public IspConteggioRelatoriModel add(IspConteggioRelatoriModel aModel) throws F3BException {
		// Model risultato della somma
		IspConteggioRelatoriModel lAppoggioMod = new IspConteggioRelatoriModel(this);

		if (aModel != null) {
			// Si sommano solo dati relativi allo stesso CONTENUTO
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
				// MEV9
				lAppoggioMod.setNumAppProvv(mNumAppProvv.add(aModel.getNumAppProvv()));
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile sommare 2 IspConteggioRelatoriModel con contenuto diverso !");

		}
		return lAppoggioMod;
	}

}
