package siap.sius.statistiche.model;

/**
* <p>Title: IspConteggioTempiModel</p>
* <p>Description: Classe Model che rappresenta il IspConteggioTempi</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class IspConteggioTempiModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4523790153443053977L;
	private String mDescContenutoStatis;
	private String mCodOggetto;
	private String mDescOggetto;
	private String mCodIntervallo;
	private String mDescIntervallo;
	private BigDecimal mNumTempo1;
	private BigDecimal mNumTempo2;
	private BigDecimal mNumTempo3;
	private BigDecimal mNumTempo4;
	private BigDecimal mNumTempo5;
	private BigDecimal mNumTempo6;
	private BigDecimal mNumTotale;
	private BigDecimal mDurataMedia;
	private String mFasSiuChiaveUfficio; /* mod. michele 5/12/2008 */

	// COSTRUTTORE DI DEFAULT
	public IspConteggioTempiModel() {
		mDescContenutoStatis = "";
		mCodOggetto = "";
		mDescOggetto = "";
		mCodIntervallo = "";
		mDescIntervallo = "";
		mNumTempo1 = null;
		mNumTempo2 = null;
		mNumTempo3 = null;
		mNumTempo4 = null;
		mNumTempo5 = null;
		mNumTempo6 = null;
		mNumTotale = null;
		mDurataMedia = null;
		mFasSiuChiaveUfficio = ""; /* mod. michele 5/12/2008 */
	}

	// COSTRUTTORE DI COPIA
	public IspConteggioTempiModel(IspConteggioTempiModel aModel) {
		mDescContenutoStatis = aModel.mDescContenutoStatis;
		mCodOggetto = aModel.mCodOggetto;
		mDescOggetto = aModel.mDescOggetto;
		mCodIntervallo = aModel.mCodIntervallo;
		mDescIntervallo = aModel.mDescIntervallo;
		mNumTempo1 = aModel.mNumTempo1;
		mNumTempo2 = aModel.mNumTempo2;
		mNumTempo3 = aModel.mNumTempo3;
		mNumTempo4 = aModel.mNumTempo4;
		mNumTempo5 = aModel.mNumTempo5;
		mNumTempo6 = aModel.mNumTempo6;
		mNumTotale = aModel.mNumTotale;
		mDurataMedia = aModel.mDurataMedia;
		mFasSiuChiaveUfficio = aModel.mFasSiuChiaveUfficio; /* mod. michele 5/12/2008 */
	}

	// COSTRUTTORE MODEL
	public IspConteggioTempiModel(String aDescContenutoStatis, String aCodOggetto, String aDescOggetto,
			String aCodIntervallo, String aDescIntervallo, BigDecimal aNumTempo1, BigDecimal aNumTempo2,
			BigDecimal aNumTempo3, BigDecimal aNumTempo4, BigDecimal aNumTempo5, BigDecimal aNumTempo6,
			BigDecimal aNumTotale, BigDecimal aDurataMedia,
			String aFasSiuChiaveUfficio) /* mod. michele 5/12/2008 */
	{
		mDescContenutoStatis = aDescContenutoStatis;
		mCodOggetto = aCodOggetto;
		mDescOggetto = aDescOggetto;
		mCodIntervallo = aCodIntervallo;
		mDescIntervallo = aDescIntervallo;
		mNumTempo1 = aNumTempo1;
		mNumTempo2 = aNumTempo2;
		mNumTempo3 = aNumTempo3;
		mNumTempo4 = aNumTempo4;
		mNumTempo5 = aNumTempo5;
		mNumTempo6 = aNumTempo6;
		mNumTotale = aNumTotale;
		mDurataMedia = aDurataMedia;
		mFasSiuChiaveUfficio = aFasSiuChiaveUfficio; /* mod. michele 5/12/2008 */
	}

	//
	// METODI GET()
	//

	public String getDescContenutoStatis() {
		return mDescContenutoStatis;
	}

	public String getCodOggetto() {
		return mCodOggetto;
	}

	public String getDescOggetto() {
		return mDescOggetto;
	}

	public String getCodIntervallo() {
		return mCodIntervallo;
	}

	public String getDescIntervallo() {
		return mDescIntervallo;
	}

	public BigDecimal getNumTempo1() {
		return mNumTempo1;
	}

	public BigDecimal getNumTempo2() {
		return mNumTempo2;
	}

	public BigDecimal getNumTempo3() {
		return mNumTempo3;
	}

	public BigDecimal getNumTempo4() {
		return mNumTempo4;
	}

	public BigDecimal getNumTempo5() {
		return mNumTempo5;
	}

	public BigDecimal getNumTempo6() {
		return mNumTempo6;
	}

	public BigDecimal getNumTotale() {
		return mNumTotale;
	}

	public BigDecimal getDurataMedia() {
		return mDurataMedia;
	}

	public String getFasSiuChiaveUfficio() {
		return mFasSiuChiaveUfficio;
	} /* mod. michele 5/12/2008 */

	//
	// METODI SET()
	//

	public void setDescContenutoStatis(String aValore) {
		mDescContenutoStatis = aValore;
	}

	public void setCodOggetto(String aValore) {
		mCodOggetto = aValore;
	}

	public void setDescOggetto(String aValore) {
		mDescOggetto = aValore;
	}

	public void setCodIntervallo(String aValore) {
		mCodIntervallo = aValore;
	}

	public void setDescIntervallo(String aValore) {
		mDescIntervallo = aValore;
	}

	public void setNumTempo1(BigDecimal aValore) {
		mNumTempo1 = aValore;
	}

	public void setNumTempo2(BigDecimal aValore) {
		mNumTempo2 = aValore;
	}

	public void setNumTempo3(BigDecimal aValore) {
		mNumTempo3 = aValore;
	}

	public void setNumTempo4(BigDecimal aValore) {
		mNumTempo4 = aValore;
	}

	public void setNumTempo5(BigDecimal aValore) {
		mNumTempo5 = aValore;
	}

	public void setNumTempo6(BigDecimal aValore) {
		mNumTempo6 = aValore;
	}

	public void setNumTotale(BigDecimal aValore) {
		mNumTotale = aValore;
	}

	public void setDurataMedia(BigDecimal aValore) {
		mDurataMedia = aValore;
	}

	public void setFasSiuChiaveUfficio(String aValore) {
		mFasSiuChiaveUfficio = aValore;
	} /* mod. michele 5/12/2008 */
}
