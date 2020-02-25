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

public class IspRelatoreSelezionatiModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 5886617080610476998L;
	private String mCodMagistrato;
	private String mTipoUfficio;
	private String mCodUfficio;

	// COSTRUTTORE DI DEFAULT
	public IspRelatoreSelezionatiModel() {
		mCodMagistrato = "";
		mTipoUfficio = "";
		mCodUfficio = "";
	}

	// COSTRUTTORE DI COPIA
	public IspRelatoreSelezionatiModel(IspRelatoreSelezionatiModel aModel) {
		mCodMagistrato = aModel.mCodMagistrato;
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

	public String getCodMagistrato() {
		return mCodMagistrato;
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

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setTipoUfficio(String aValore) {
		mTipoUfficio = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

}
