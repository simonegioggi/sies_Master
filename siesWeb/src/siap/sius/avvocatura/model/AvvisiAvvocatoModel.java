package siap.sius.avvocatura.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: AvvisiAvvocatoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta Avvisi_Avvocato
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class AvvisiAvvocatoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 99767722000830400L;
	private BigDecimal mIdAvviso;
	private BigDecimal mIdAvvocato;
	private String mCognomeSoggetto;
	private String mNomeSoggetto;
	// private BigDecimal mIdProvvedimento;
	private BigDecimal mIdEvento;
	private String mDescProvvedimento;
	private String mUfficioEmittente;
	private String mTestoAvviso;
	private String mFlagVisualizzazione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;

	// COSTRUTTORE DI DEFAULT
	public AvvisiAvvocatoModel() {
		this.mIdAvviso = null;
		this.mIdAvvocato = null;
		this.mCognomeSoggetto = "";
		this.mNomeSoggetto = "";
		// this.mIdProvvedimento = null;
		this.mIdEvento = null;
		this.mDescProvvedimento = "";
		this.mUfficioEmittente = "";
		this.mTestoAvviso = "";
		this.mFlagVisualizzazione = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
	}

	// COSTRUTTORE DI COPIA
	public AvvisiAvvocatoModel(AvvisiAvvocatoModel aModel) {
		this.mIdAvviso = aModel.mIdAvviso;
		this.mIdAvvocato = aModel.mIdAvvocato;
		this.mCognomeSoggetto = aModel.mCognomeSoggetto;
		this.mNomeSoggetto = aModel.mNomeSoggetto;
		// this.mIdProvvedimento = aModel.mIdProvvedimento;
		this.mIdEvento = aModel.mIdEvento;
		this.mDescProvvedimento = aModel.mDescProvvedimento;
		this.mUfficioEmittente = aModel.mUfficioEmittente;
		this.mTestoAvviso = aModel.mTestoAvviso;
		this.mFlagVisualizzazione = aModel.mFlagVisualizzazione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
	}

	// COSTRUTTORE MODEL
	public AvvisiAvvocatoModel(BigDecimal aIdAvviso, BigDecimal aIdAvvocato, String aCognomeSoggetto,
			String aNomeSoggetto,
			// BigDecimal aIdProvvedimento,
			BigDecimal aIdEvento, String aDescProvvedimento, String aUfficioEmittente, String aTestoAvviso,
			String aFlagVisualizzazione, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento) {
		this.mIdAvviso = aIdAvviso;
		this.mIdAvvocato = aIdAvvocato;
		this.mCognomeSoggetto = aCognomeSoggetto;
		this.mNomeSoggetto = aNomeSoggetto;
		// this.mIdProvvedimento = aIdProvvedimento;
		this.mIdEvento = aIdEvento;
		this.mDescProvvedimento = aDescProvvedimento;
		this.mUfficioEmittente = aUfficioEmittente;
		this.mTestoAvviso = aTestoAvviso;
		this.mFlagVisualizzazione = aFlagVisualizzazione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdAvviso() {
		return mIdAvviso;
	}

	public BigDecimal getIdAvvocato() {
		return mIdAvvocato;
	}

	public String getCognomeSoggetto() {
		return mCognomeSoggetto;
	}

	public String getNomeSoggetto() {
		return mNomeSoggetto;
	}

	// public BigDecimal getIdProvvedimento() { return mIdProvvedimento; }
	public BigDecimal getIdEvento() {
		return mIdEvento;
	}

	public String getDescProvvedimento() {
		return mDescProvvedimento;
	}

	public String getUfficioEmittente() {
		return mUfficioEmittente;
	}

	public String getTestoAvviso() {
		return mTestoAvviso;
	}

	public String getFlagVisualizzazione() {
		return mFlagVisualizzazione;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	//
	// METODI SET()
	//
	public void setIdAvviso(BigDecimal aValore) {
		mIdAvviso = aValore;
	}

	public void setIdAvvocato(BigDecimal aValore) {
		mIdAvvocato = aValore;
	}

	public void setCognomeSoggeto(String aValore) {
		mCognomeSoggetto = aValore;
	}

	public void setNomeSoggetto(String aValore) {
		mNomeSoggetto = aValore;
	}

	// public void setIdProvvedimento(BigDecimal aValore ) { mIdProvvedimento = aValore; }
	public void setIdEvento(BigDecimal aValore) {
		mIdEvento = aValore;
	}

	public void setDescProvvedimento(String aValore) {
		mDescProvvedimento = aValore;
	}

	public void setUfficioEmittente(String aValore) {
		mUfficioEmittente = aValore;
	}

	public void setTestoAvviso(String aValore) {
		mTestoAvviso = aValore;
	}

	public void setFlagVisualizzazione(String aValore) {
		mFlagVisualizzazione = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

}