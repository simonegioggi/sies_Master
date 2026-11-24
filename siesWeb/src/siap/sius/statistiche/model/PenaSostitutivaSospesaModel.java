package siap.sius.statistiche.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * MEV_2026-124_SG: procedimento con pena sostitutiva sospesa e priva di successiva ripresa.
 */
public class PenaSostitutivaSospesaModel extends GenericModel {

	private static final long serialVersionUID = 1L;

	private BigDecimal mIdFascicoloSius;
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mCognome;
	private String mNome;
	private Date mDataIscrizione;
	private Date mDataInizioSospensione;
	private Date mDataFineSospensione;
	private BigDecimal mSospensioneAA;
	private BigDecimal mSospensioneMM;
	private BigDecimal mSospensioneGG;
	private String mCodTipoUfficioSosp;

	public BigDecimal getIdFascicoloSius() {
		return mIdFascicoloSius;
	}

	public void setIdFascicoloSius(BigDecimal aValore) {
		mIdFascicoloSius = aValore;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public String getCognome() {
		return mCognome;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public String getNome() {
		return mNome;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public Date getDataIscrizione() {
		return mDataIscrizione;
	}

	public void setDataIscrizione(Date aValore) {
		mDataIscrizione = aValore;
	}

	public Date getDataInizioSospensione() {
		return mDataInizioSospensione;
	}

	public void setDataInizioSospensione(Date aValore) {
		mDataInizioSospensione = aValore;
	}

	public Date getDataFineSospensione() {
		return mDataFineSospensione;
	}

	public void setDataFineSospensione(Date aValore) {
		mDataFineSospensione = aValore;
	}

	public BigDecimal getSospensioneAA() {
		return mSospensioneAA;
	}

	public void setSospensioneAA(BigDecimal aValore) {
		mSospensioneAA = aValore;
	}

	public BigDecimal getSospensioneMM() {
		return mSospensioneMM;
	}

	public void setSospensioneMM(BigDecimal aValore) {
		mSospensioneMM = aValore;
	}

	public BigDecimal getSospensioneGG() {
		return mSospensioneGG;
	}

	public void setSospensioneGG(BigDecimal aValore) {
		mSospensioneGG = aValore;
	}

	public String getCodTipoUfficioSosp() {
		return mCodTipoUfficioSosp;
	}

	public void setCodTipoUfficioSosp(String aValore) {
		mCodTipoUfficioSosp = aValore;
	}
}
