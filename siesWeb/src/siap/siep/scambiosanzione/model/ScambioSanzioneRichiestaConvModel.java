package siap.siep.scambiosanzione.model;

/**
* <p>Title: ScambioSanzioneRichiestaConvModel</p>
* <p>Description: Questa Classe Model associa SCAMBIO_SANZIONE E RICHIESTA_CONVERSIONE </p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import f3b.model.GenericModel;

public class ScambioSanzioneRichiestaConvModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7751464531116313616L;

	private ScambioSanzioneModel mScambioSanzione;
	private RichiestaConversioneModel mRichiestaConv;
	private BigDecimal mCodEsito;

	// COSTRUTTORE DI DEFAULT
	public ScambioSanzioneRichiestaConvModel() {
		mScambioSanzione = null;
		mRichiestaConv = null;
		mCodEsito = null;
	}

	// COSTRUTTORE DI COPIA
	public ScambioSanzioneRichiestaConvModel(ScambioSanzioneRichiestaConvModel aModel) {
		mScambioSanzione = new ScambioSanzioneModel(aModel.getScambioSanzione());
		mRichiestaConv = new RichiestaConversioneModel(aModel.getRichiestaConv());
		mCodEsito = null;
	}

	// COSTRUTTORE MODEL
	public ScambioSanzioneRichiestaConvModel(RichiestaConversioneModel aScambioSanzioneRichiestaConv) {
		this();
	}

	public ScambioSanzioneRichiestaConvModel(ScambioSanzioneModel aScambioSanzione,
			RichiestaConversioneModel aRichiestaConv) {
		mScambioSanzione = new ScambioSanzioneModel(aScambioSanzione);
		mRichiestaConv = new RichiestaConversioneModel(aRichiestaConv);
	}

	public ScambioSanzioneRichiestaConvModel(ScambioSanzioneModel aScambioSanzione,
			RichiestaConversioneModel aRichiestaConv, BigDecimal aCodEsito) {
		mScambioSanzione = new ScambioSanzioneModel(aScambioSanzione);
		mRichiestaConv = new RichiestaConversioneModel(aRichiestaConv);
		mCodEsito = aCodEsito;
	}

	// METODI GET()
	//
	public ScambioSanzioneModel getScambioSanzione() {
		return mScambioSanzione;
	}

	public RichiestaConversioneModel getRichiestaConv() {
		return mRichiestaConv;
	}

	public BigDecimal getCodEsito() {
		return mCodEsito;
	}

	// METODI SET()
	//
	public void setScambioSanzione(ScambioSanzioneModel aValore) {
		mScambioSanzione = aValore;
	}

	public void setRichiestaConv(RichiestaConversioneModel aValore) {
		mRichiestaConv = aValore;
	}

	public void setCodEsito(BigDecimal aValore) {
		mCodEsito = aValore;
	}

	public String toString() {
		String lRet = getClass().getName() + "\n";
		if (mScambioSanzione != null)
			lRet += mScambioSanzione.toString() + "\n";
		if (mRichiestaConv != null)
			lRet += mRichiestaConv.toString() + "\n";
		if (mCodEsito != null)
			lRet += mCodEsito.toString() + "\n";

		return lRet;
	}

}