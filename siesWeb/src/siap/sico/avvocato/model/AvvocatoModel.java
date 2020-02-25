package siap.sico.avvocato.model;

/**
* <p>Title: AvvocatoModel</p>
* <p>Description: Classe Model che rappresenta il Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AvvocatoModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -3398717013980267066L;

	private BigDecimal mIdAvvocato;
	private String mCognome;
	private String mNome;
	private String mDescrTipo;
	private String mForo;
	private String mIndirizzo;
	private String mTelefono;
	private String mFax;
	private String mEMail;
	private String mCodLuogoNascita;
	private String mCodComuneResidenza;
	private String mDescCodLuogoNascita;
	private String mDescCodComuneResidenza;
	private Date mDataNascita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioInserimento;
	private String mCodUfficioAggiornamento;
	private String mCodNonAttivita;
	private String mDescrNonAttivita;
	private Date mDataSospensione;
	private Date mDataRadiazione;
	private String mCodUffAppartenenza;
	private String mNote;
	private String mFlagCancellato;
	private String mCodiceFiscale;
	private String mProvincia;
	private String mCap;
	private BigDecimal mFlagVisualizza;
	private BigDecimal mIdAvvocatoStandard;

	// COSTRUTTORE DI DEFAULT
	public AvvocatoModel() {
		this.mIdAvvocato = null;
		this.mCognome = "";
		this.mNome = "";
		this.mDescrTipo = "";
		this.mForo = "";
		this.mIndirizzo = "";
		this.mTelefono = "";
		this.mFax = "";
		this.mEMail = "";
		this.mCodLuogoNascita = "";
		this.mCodComuneResidenza = "";
		this.mDescCodLuogoNascita = "";
		this.mDescCodComuneResidenza = "";
		this.mDataNascita = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioInserimento = "";
		this.mCodUfficioAggiornamento = "";
		this.mDataSospensione = null;
		this.mDataRadiazione = null;
		this.mCodNonAttivita = "";
		this.mCodUffAppartenenza = "";
		this.mNote = "";
		this.mFlagCancellato = "";
		this.mDescrNonAttivita = "";
		this.mCodiceFiscale = "";
		this.mProvincia = "";
		this.mCap = "";
		this.mFlagVisualizza = null;
		this.mIdAvvocatoStandard = null;
	}

	// COSTRUTTORE DI COPIA
	public AvvocatoModel(AvvocatoModel aModel) {
		this.mIdAvvocato = aModel.mIdAvvocato;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mForo = aModel.mForo;
		this.mIndirizzo = aModel.mIndirizzo;
		this.mTelefono = aModel.mTelefono;
		this.mFax = aModel.mFax;
		this.mEMail = aModel.mEMail;
		this.mCodLuogoNascita = aModel.mCodLuogoNascita;
		this.mCodComuneResidenza = aModel.mCodComuneResidenza;
		this.mDescCodLuogoNascita = aModel.mDescCodLuogoNascita;
		this.mDescCodComuneResidenza = aModel.mDescCodComuneResidenza;
		this.mDataNascita = aModel.mDataNascita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mDescrTipo = aModel.mDescrTipo;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDataSospensione = aModel.mDataSospensione;
		this.mDataRadiazione = aModel.mDataRadiazione;
		this.mCodNonAttivita = aModel.mCodNonAttivita;
		this.mCodUffAppartenenza = aModel.mCodUffAppartenenza;
		this.mNote = aModel.mNote;
		this.mFlagCancellato = aModel.mFlagCancellato;
		this.mDescrNonAttivita = aModel.mDescrNonAttivita;
		this.mCodiceFiscale = aModel.mCodiceFiscale;
		this.mProvincia = aModel.mProvincia;
		this.mCap = aModel.mCap;
		this.mFlagVisualizza = aModel.mFlagVisualizza;
		this.mIdAvvocatoStandard = aModel.mIdAvvocatoStandard;
	}

	// COSTRUTTORE MODEL
	public AvvocatoModel(BigDecimal aIdAvvocato, String aCognome, String aNome, String aForo,
			String aIndirizzo, String aTelefono, String aFax, String aEMail, String aCodLuogoNascita,
			String aCodComuneResidenza, String aDescCodLuogoNascita, String aDescCodComuneResidenza,
			Date aDataNascita, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aDescrTipo,
			String aCodUfficioInserimento, String aCodUfficioAggiornamento, Date aDataSospensione,
			Date aDataRadiazione, String aCodNonAttivita, String aCodUffAppartenenza, String aNote,
			String aFlagCancellato, String aDescrNonAttivita, String aCodiceFiscale, String aProvincia,
			String aCap, BigDecimal aFlagVisualizza, BigDecimal aIdAvvocatoStandard)

	{
		this.mIdAvvocato = aIdAvvocato;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mForo = aForo;
		this.mIndirizzo = aIndirizzo;
		this.mTelefono = aTelefono;
		this.mFax = aFax;
		this.mEMail = aEMail;
		this.mCodLuogoNascita = aCodLuogoNascita;
		this.mCodComuneResidenza = aCodComuneResidenza;
		this.mDescCodLuogoNascita = aDescCodLuogoNascita;
		this.mDescCodComuneResidenza = aDescCodComuneResidenza;
		this.mDataNascita = aDataNascita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mDescrTipo = aDescrTipo;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDataSospensione = aDataSospensione;
		this.mDataRadiazione = aDataRadiazione;
		this.mCodNonAttivita = aCodNonAttivita;
		this.mCodUffAppartenenza = aCodUffAppartenenza;
		this.mNote = aNote;
		this.mFlagCancellato = aFlagCancellato;
		this.mDescrNonAttivita = aDescrNonAttivita;
		this.mCodiceFiscale = aCodiceFiscale;
		this.mProvincia = aProvincia;
		this.mCap = aCap;
		this.mFlagVisualizza = aFlagVisualizza;
		this.mIdAvvocatoStandard = aIdAvvocatoStandard;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdAvvocato() {
		return mIdAvvocato;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getForo() {
		return mForo;
	}

	public String getIndirizzo() {
		return mIndirizzo;
	}

	public String getTelefono() {
		return mTelefono;
	}

	public String getFax() {
		return mFax;
	}

	public String getEMail() {
		return mEMail;
	}

	public String getCodLuogoNascita() {
		return mCodLuogoNascita;
	}

	public String getCodComuneResidenza() {
		return mCodComuneResidenza;
	}

	public String getDescLuogoNascita() {
		return mDescCodLuogoNascita;
	}

	public String getDescComuneResidenza() {
		return mDescCodComuneResidenza;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getDescrTipo() {
		return mDescrTipo;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public Date getDataSospensione() {
		return mDataSospensione;
	}

	public Date getDataRadiazione() {
		return mDataRadiazione;
	}

	public String getCodNonAttivita() {
		return mCodNonAttivita;
	}

	public String getCodUffAppartenenza() {
		return mCodUffAppartenenza;
	}

	public String getNote() {
		return mNote;
	}

	public String getFlagCancellato() {
		return mFlagCancellato;
	}

	public String getDescrNonAttivita() {
		return mDescrNonAttivita;
	}

	public String getCodiceFiscale() {
		return mCodiceFiscale;
	}

	public String getProvincia() {
		return mProvincia;
	}

	public String getCap() {
		return mCap;
	}

	public BigDecimal getFlagVisualizza() {
		return mFlagVisualizza;
	}

	public BigDecimal getIdAvvocatoStandard() {
		return mIdAvvocatoStandard;
	}

	//
	// METODI SET()
	//

	public void setIdAvvocato(BigDecimal aValore) {
		mIdAvvocato = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setForo(String aValore) {
		mForo = aValore;
	}

	public void setIndirizzo(String aValore) {
		mIndirizzo = aValore;
	}

	public void setTelefono(String aValore) {
		mTelefono = aValore;
	}

	public void setFax(String aValore) {
		mFax = aValore;
	}

	public void setEMail(String aValore) {
		mEMail = aValore;
	}

	public void setCodLuogoNascita(String aValore) {
		mCodLuogoNascita = aValore;
	}

	public void setCodComuneResidenza(String aValore) {
		mCodComuneResidenza = aValore;
	}

	public void setDescLuogoNascita(String aValore) {
		mDescCodLuogoNascita = aValore;
	}

	public void setDescComuneResidenza(String aValore) {
		mDescCodComuneResidenza = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setDescrTipo(String aValore) {
		mDescrTipo = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDataSospensione(Date aValore) {
		mDataSospensione = aValore;
	}

	public void setDataRadiazione(Date aValore) {
		mDataRadiazione = aValore;
	}

	public void setCodNonAttivita(String aValore) {
		mCodNonAttivita = aValore;
	}

	public void setCodUffAppartenenza(String aValore) {
		mCodUffAppartenenza = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setFlagCancellato(String aValore) {
		mFlagCancellato = aValore;
	}

	public void setDescrNonAttivita(String aValore) {
		mDescrNonAttivita = aValore;
	}

	public void setCodiceFiscale(String aValore) {
		mCodiceFiscale = aValore;
	}

	public void setProvincia(String aValore) {
		mProvincia = aValore;
	}

	public void setCap(String aValore) {
		mCap = aValore;
	}

	public void setFlagVisualizza(BigDecimal aValore) {
		mFlagVisualizza = aValore;
	}

	public void setIdAvvocatoStandard(BigDecimal aValore) {
		mIdAvvocatoStandard = aValore;
	}

}