package siap.sius.statistiche.model;

/**
* <p>Title: IspOggettiSelezionatiModel</p>
* <p>Description: Classe Model che rappresenta il IspOggettiSelezionati</p>
* Il model rappresenta gli oggetti selezionati per la statistica comparata magistrati.
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: </p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class IspMotivoOggettoSelezionatiModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 8154605049388936462L;
	private String mCodOggetto;
	private String mDescOggetto;
	private String mCodMotivo;
	private String mDescMotivo;
	private String mTipoUfficio;
	private String mCodUfficio;

	// COSTRUTTORE DI DEFAULT
	public IspMotivoOggettoSelezionatiModel() {
		mCodOggetto = "";
		mDescOggetto = "";
		mCodMotivo = "";
		mDescMotivo = "";
		mTipoUfficio = "";
		mCodUfficio = "";
	}

	// COSTRUTTORE DI COPIA
	public IspMotivoOggettoSelezionatiModel(IspMotivoOggettoSelezionatiModel aModel) {
		mCodOggetto = aModel.mCodOggetto;
		mDescOggetto = aModel.mDescOggetto;
		mCodMotivo = aModel.mCodMotivo;
		mDescMotivo = aModel.mDescMotivo;
		mTipoUfficio = aModel.mTipoUfficio;
		mCodUfficio = aModel.mCodUfficio;
	}

	// COSTRUTTORE MODEL
	/*
	 * public IspOggettiSelezionatiModel ( String aCodOggetto, String aDescOggetto, String
	 * aDescContenutoStatis, BigDecimal aNumPendentiInizio, BigDecimal aNumSopravvenuti, BigDecimal
	 * aNumDefEsito1, BigDecimal aNumDefEsito2, BigDecimal aNumDefEsito3, BigDecimal aNumDefEsito4, BigDecimal
	 * aNumDefEsito5, BigDecimal aNumDefEsito6, BigDecimal aNumDefIscErr, BigDecimal aNumPendentiFine, String
	 * aFasSiuChiaveUfficio, BigDecimal aNumCancellati, BigDecimal aNumUnificati) { mCodOggetto = aCodOggetto;
	 * mDescOggetto = aDescOggetto; mDescContenutoStatis = aDescContenutoStatis; mNumPendentiInizio =
	 * aNumPendentiInizio; mNumSopravvenuti = aNumSopravvenuti; mNumDefEsito1 = aNumDefEsito1; mNumDefEsito2 =
	 * aNumDefEsito2; mNumDefEsito3 = aNumDefEsito3; mNumDefEsito4 = aNumDefEsito4; mNumDefEsito5 =
	 * aNumDefEsito5; mNumDefEsito6 = aNumDefEsito6; mNumDefIscErr = aNumDefIscErr; mNumPendentiFine =
	 * aNumPendentiFine; mFasSiuChiaveUfficio = aFasSiuChiaveUfficio; mNumCancellati = aNumCancellati;
	 * mNumUnificati = aNumUnificati; }
	 */
	//
	// METODI GET()
	//

	public String getCodOggetto() {
		return mCodOggetto;
	}

	public String getDescOggetto() {
		return mDescOggetto;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getDescMotivo() {
		return mDescMotivo;
	}

	public String getTipoUfficio() {
		return mTipoUfficio;
	}

	public String getCodUfficio() {
		return mCodUfficio;
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

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setDescMotivo(String aValore) {
		mDescMotivo = aValore;
	}

	public void setTipoUfficio(String aValore) {
		mTipoUfficio = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

}
