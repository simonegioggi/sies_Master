package siap.sius.avvocatura.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AvvisiElencoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2110775335091727406L;
	private BigDecimal mIdAvviso;
	private BigDecimal mAnnoSius;
	private BigDecimal mNumeroSius;
	private String mCognomeSoggetto;
	private String mNomeSoggetto;
	private String mDescProvvedimento;
	private String mUfficioEmittente;
	private String mTestoAvviso;
	private String mFlagVisualizzazione;
	private Date mDataUdienza;
	private Date mDataDeposito;

	private BigDecimal mIdEvento;
	private String mCodiTipoProvvedimento;
	private String mCodiEsito;
	private String mCodFiscaleAvvocato;

	public AvvisiElencoModel() {
		this.mIdAvviso = null;
		this.mAnnoSius = null;
		this.mNumeroSius = null;
		this.mCognomeSoggetto = "";
		this.mNomeSoggetto = "";
		this.mIdEvento = null;
		this.mDescProvvedimento = "";
		this.mUfficioEmittente = "";
		this.mTestoAvviso = "";
		this.mFlagVisualizzazione = "N";
		this.mDataUdienza = null;
		this.mDataDeposito = null;
		this.mCodiTipoProvvedimento = "";
		this.mCodiEsito = "";
		this.mCodFiscaleAvvocato = "";
	}

	public AvvisiElencoModel(BigDecimal mIdAvviso, BigDecimal mAnnoSius, BigDecimal mNumeroSius,
			String mCognomeSoggetto, String mNomeSoggetto, BigDecimal mIdEvento, String mDescProvvedimento,
			String mUfficioEmittente, String mTestoAvviso, String mFlagVisualizzazione, Date mDataUdienza,
			Date mDataDeposito, String mCodiTipoProvvedimento, String mCodiEsito,
			String mCodFiscaleAvvocato) {
		super();
		this.mIdAvviso = mIdAvviso;
		this.mAnnoSius = mAnnoSius;
		this.mNumeroSius = mNumeroSius;
		this.mCognomeSoggetto = mCognomeSoggetto;
		this.mNomeSoggetto = mNomeSoggetto;
		this.mIdEvento = mIdEvento;
		this.mDescProvvedimento = mDescProvvedimento;
		this.mUfficioEmittente = mUfficioEmittente;
		this.mTestoAvviso = mTestoAvviso;
		this.mFlagVisualizzazione = mFlagVisualizzazione;
		this.mDataUdienza = mDataUdienza;
		this.mDataDeposito = mDataDeposito;
		this.mCodiTipoProvvedimento = mCodiTipoProvvedimento;
		this.mCodiEsito = mCodiEsito;
		this.mCodFiscaleAvvocato = mCodFiscaleAvvocato;
	}

	public BigDecimal getmIdAvviso() {
		return mIdAvviso;
	}

	public void setmIdAvviso(BigDecimal mIdAvviso) {
		this.mIdAvviso = mIdAvviso;
	}

	public BigDecimal getmAnnoSius() {
		return mAnnoSius;
	}

	public void setmAnnoSius(BigDecimal mAnnoSius) {
		this.mAnnoSius = mAnnoSius;
	}

	public BigDecimal getmNumeroSius() {
		return mNumeroSius;
	}

	public void setmNumeroSius(BigDecimal mNumeroSius) {
		this.mNumeroSius = mNumeroSius;
	}

	public String getmCognomeSoggetto() {
		return mCognomeSoggetto;
	}

	public void setmCognomeSoggetto(String mCognomeSoggetto) {
		this.mCognomeSoggetto = mCognomeSoggetto;
	}

	public String getmNomeSoggetto() {
		return mNomeSoggetto;
	}

	public void setmNomeSoggetto(String mNomeSoggetto) {
		this.mNomeSoggetto = mNomeSoggetto;
	}

	public BigDecimal getmIdEvento() {
		return mIdEvento;
	}

	public void setmIdEvento(BigDecimal mIdEvento) {
		this.mIdEvento = mIdEvento;
	}

	public String getmDescProvvedimento() {
		return mDescProvvedimento;
	}

	public void setmDescProvvedimento(String mDescProvvedimento) {
		this.mDescProvvedimento = mDescProvvedimento;
	}

	public String getmUfficioEmittente() {
		return mUfficioEmittente;
	}

	public void setmUfficioEmittente(String mUfficioEmittente) {
		this.mUfficioEmittente = mUfficioEmittente;
	}

	public String getmTestoAvviso() {
		return mTestoAvviso;
	}

	public void setmTestoAvviso(String mTestoAvviso) {
		this.mTestoAvviso = mTestoAvviso;
	}

	public String getmFlagVisualizzazione() {
		return mFlagVisualizzazione;
	}

	public void setmFlagVisualizzazione(String mFlagVisualizzazione) {
		this.mFlagVisualizzazione = mFlagVisualizzazione;
	}

	public Date getmDataUdienza() {
		return mDataUdienza;
	}

	public void setmDataUdienza(Date mDataUdienza) {
		this.mDataUdienza = mDataUdienza;
	}

	public Date getmDataDeposito() {
		return mDataDeposito;
	}

	public void setmDataDeposito(Date mDataDeposito) {
		this.mDataDeposito = mDataDeposito;
	}

	public String getmCodiTipoProvvedimento() {
		return mCodiTipoProvvedimento;
	}

	public void setmCodiTipoProvvedimento(String mCodiTipoProvvedimento) {
		this.mCodiTipoProvvedimento = mCodiTipoProvvedimento;
	}

	public String getmCodiEsito() {
		return mCodiEsito;
	}

	public void setmCodiEsito(String mCodiEsito) {
		this.mCodiEsito = mCodiEsito;
	}

	public String getmCodFiscaleAvvocato() {
		return mCodFiscaleAvvocato;
	}

	public void setmCodFiscaleAvvocato(String mCodFiscaleAvvocato) {
		this.mCodFiscaleAvvocato = mCodFiscaleAvvocato;
	}

}