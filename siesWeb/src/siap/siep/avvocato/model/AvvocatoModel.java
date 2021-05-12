package siap.siep.avvocato.model;

/**
* <p>Title: AvvocatoModel</p>
* <p>Description: Classe Model che rappresenta l'Entità Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AvvocatoModel extends GenericModel {

	private static final long serialVersionUID = -3473586574513573356L;

	private BigDecimal mIdAvvocato;
	private String mCognome;
	private String mNome;
	private String mForo;

	// INIZIO: MEV_21 (avvocati)
	private String mDescComuneSedeForo;
	private String mPec;
	private String mFlagRegInde;
	private String mDescrComuneStudio;
	private String mDescLuogoNascitaReginde;
	private String mCodStatoNascita;
	private String mDescrStatoNascita;
	// FINE: MEV_21

	private String mIndirizzo;
	private String mTelefono;
	private String mFax;
	private String mEMail;
	private String mCodiceFiscale;
	private String mProvincia;
	private String mCap;
	private BigDecimal mFlagVisualizza;
	private String mCodComuneResidenza;
	private String mCodLuogoNascita;
	private Date mDataNascita;
	private Date mDataSospensione;
	private Date mDataRadiazione;
	private String mCodNonAttivita;
	private String mNote;
	private String mFlagCancellato;
	private String mCodUffAppartenenza;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private Date mDataInserimento;
	private String mCodUfficioAggiornamento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	// Campi per le descrizione dei codici
	private String mDescCodLuogoNascita;
	private String mDescCodComuneResidenza;
	private String mDescrNonAttivita;
	// Campi della tabella AVVOCATO_FASCICOLO_SIEP
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mDescrTipo;
	// id avvocato standard
	private BigDecimal mIdAvvocatoStandard;

	// COSTRUTTORE DI DEFAULT
	public AvvocatoModel() {
		this.mIdAvvocato = null;
		this.mCognome = "";
		this.mNome = "";
		this.mDescrTipo = "";
		this.mDescCodLuogoNascita = "";
		this.mDescCodComuneResidenza = "";
		this.mDescrNonAttivita = "";
		this.mForo = "";
		// INIZIO: MEV_21 (avvocati)
		this.mDescComuneSedeForo = "";
		this.mPec = "";
		this.mFlagRegInde = "";
		this.mDescrComuneStudio = "";
		this.mDescLuogoNascitaReginde = "";
		this.mCodStatoNascita = "";
		this.mDescrStatoNascita = "";
		// FINE: MEV_21
		this.mIndirizzo = "";
		this.mTelefono = "";
		this.mFax = "";
		this.mEMail = "";
		this.mCodiceFiscale = "";
		this.mProvincia = "";
		this.mCap = "";
		this.mFlagVisualizza = new BigDecimal(1);
		this.mCodOperatoreInserimento = "";
		this.mCodUfficioInserimento = "";
		this.mDataInserimento = null;
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mCodLuogoNascita = "";
		this.mCodComuneResidenza = "";
		this.mDataNascita = null;
		this.mDataSospensione = null;
		this.mDataRadiazione = null;
		this.mCodNonAttivita = "";
		this.mCodUffAppartenenza = "";
		this.mNote = "";
		this.mFlagCancellato = "";
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mIdAvvocatoStandard = null;
	}

	// COSTRUTTORE DI COPIA
	public AvvocatoModel(AvvocatoModel aModel) {
		this.mIdAvvocato = aModel.mIdAvvocato;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mForo = aModel.mForo;
		// INIZIO: MEV_21 (avvocati)
		this.mDescComuneSedeForo = aModel.mDescComuneSedeForo;
		this.mPec = aModel.mPec;
		this.mFlagRegInde = aModel.mFlagRegInde;
		this.mDescrComuneStudio = aModel.mDescrComuneStudio;
		this.mDescLuogoNascitaReginde = aModel.mDescLuogoNascitaReginde;
		this.mCodStatoNascita = aModel.mCodStatoNascita;
		this.mDescrStatoNascita = aModel.mDescrStatoNascita;
		// FINE: MEV_21
		this.mIndirizzo = aModel.mIndirizzo;
		this.mTelefono = aModel.mTelefono;
		this.mFax = aModel.mFax;
		this.mEMail = aModel.mEMail;
		this.mCodiceFiscale = aModel.mCodiceFiscale;
		this.mProvincia = aModel.mProvincia;
		this.mCap = aModel.mCap;
		this.mFlagVisualizza = aModel.mFlagVisualizza;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;

		this.mDescrTipo = aModel.mDescrTipo;
		this.mDescCodLuogoNascita = aModel.mDescCodLuogoNascita;
		this.mDescCodComuneResidenza = aModel.mDescCodComuneResidenza;
		this.mDescrNonAttivita = aModel.mDescrNonAttivita;
		this.mCodLuogoNascita = aModel.mCodLuogoNascita;
		this.mCodComuneResidenza = aModel.mCodComuneResidenza;
		this.mDataNascita = aModel.mDataNascita;
		this.mDataSospensione = aModel.mDataSospensione;
		this.mDataRadiazione = aModel.mDataRadiazione;
		this.mCodNonAttivita = aModel.mCodNonAttivita;
		this.mCodUffAppartenenza = aModel.mCodUffAppartenenza;
		this.mNote = aModel.mNote;
		this.mFlagCancellato = aModel.mFlagCancellato;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mIdAvvocatoStandard = aModel.mIdAvvocatoStandard;
	}

	// COSTRUTTORE MODEL
	public AvvocatoModel(BigDecimal aIdAvvocato, String aCognome, String aNome, String aForo,
			// INIZIO: MEV_21 (avvocati)
			String aDescComuneSedeForo, String aPec, String aFlagRegInde, String aDescrComuneStudio,
			String aDescLuogoNascitaReginde, String aCodStatoNascita, String aDescrStatoNascita,
			// FINE: MEV_21
			String aIndirizzo, String aTelefono, String aFax, String aEMail, String aCodiceFiscale,
			String aProvincia, String aCap, BigDecimal aFlagVisualizza, String aCodOperatoreInserimento,
			String aCodUfficioInserimento, Date aDataInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrTipo,
			String aDescCodLuogoNascita, String aDescCodComuneResidenza, String aDescrNonAttivita,
			String aCodLuogoNascita, String aCodComuneResidenza, Date aDataNascita, Date aDataSospensione,
			Date aDataRadiazione, String aCodNonAttivita, String aCodUffAppartenenza, String aNote,
			String aFlagCancellato, BigDecimal aIdAvvocatoStandard) {
		this.mIdAvvocato = aIdAvvocato;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mForo = aForo;
		// INIZIO: MEV_21 (avvocati)
		this.mDescComuneSedeForo = aDescComuneSedeForo;
		this.mPec = aPec;
		this.mFlagRegInde = aFlagRegInde;
		this.mDescrComuneStudio = aDescrComuneStudio;
		this.mDescLuogoNascitaReginde = aDescLuogoNascitaReginde;
		this.mCodStatoNascita = aCodStatoNascita;
		this.mDescrStatoNascita = aDescrStatoNascita;
		// FINE: MEV_21
		this.mIndirizzo = aIndirizzo;
		this.mTelefono = aTelefono;
		this.mFax = aFax;
		this.mEMail = aEMail;
		this.mCodiceFiscale = aCodiceFiscale;
		this.mProvincia = aProvincia;
		this.mCap = aCap;
		this.mFlagVisualizza = aFlagVisualizza;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;

		this.mDescrTipo = aDescrTipo;
		this.mDescCodLuogoNascita = aDescCodLuogoNascita;
		this.mDescCodComuneResidenza = aDescCodComuneResidenza;
		this.mDescrNonAttivita = aDescrNonAttivita;
		this.mCodLuogoNascita = aCodLuogoNascita;
		this.mCodComuneResidenza = aCodComuneResidenza;
		this.mDataNascita = aDataNascita;
		this.mDataSospensione = aDataSospensione;
		this.mDataRadiazione = aDataRadiazione;
		this.mCodNonAttivita = aCodNonAttivita;
		this.mCodUffAppartenenza = aCodUffAppartenenza;
		this.mNote = aNote;
		this.mFlagCancellato = aFlagCancellato;
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mIdAvvocatoStandard = null;
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

	// INIZIO: MEV_21 (avvocati)
	public String getDescComuneSedeForo() {
		return mDescComuneSedeForo;
	}

	public String getPec() {
		return mPec;
	}

	public String getFlagRegInde() {
		return mFlagRegInde;
	}

	public String getDescrComuneStudio() {
		return mDescrComuneStudio;
	}

	public String getDescLuogoNascitaReginde() {
		return mDescLuogoNascitaReginde;
	}

	public String getCodStatoNascita() {
		return mCodStatoNascita;
	}

	public String getDescrStatoNascita() {
		return mDescrStatoNascita;
	}
	// FINE: MEV_21

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

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
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

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrTipo() {
		return mDescrTipo;
	}

	public String getDescrNonAttivita() {
		return mDescrNonAttivita;
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

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public String getFlagCancellato() {
		return mFlagCancellato;
	}

	public BigDecimal getIdAvvocatoStandard() {
		return mIdAvvocatoStandard;
	}

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

	// INIZIO: MEV_21 (avvocati)
	public void setDescComuneSedeForo(String aValore) {
		mDescComuneSedeForo = aValore;
	}

	public void setPec(String aValore) {
		mPec = aValore;
	}

	public void setFlagRegInde(String aValore) {
		mFlagRegInde = aValore;
	}

	public void setDescrComuneStudio(String aValore) {
		mDescrComuneStudio = aValore;
	}

	public void setDescLuogoNascitaReginde(String aValore) {
		mDescLuogoNascitaReginde = aValore;
	}

	public void setCodStatoNascita(String aValore) {
		mCodStatoNascita = aValore;
	}

	public void setDescrStatoNascita(String aValore) {
		mDescrStatoNascita = aValore;
	}
	// FINE: MEV_21

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

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
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

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrTipo(String aValore) {
		mDescrTipo = aValore;
	}

	public void setDescLuogoNascita(String aValore) {
		mDescCodLuogoNascita = aValore;
	}

	public void setDescComuneResidenza(String aValore) {
		mDescCodComuneResidenza = aValore;
	}

	public void setDescrNonAttivita(String aValore) {
		mDescrNonAttivita = aValore;
	}

	public void setCodLuogoNascita(String aValore) {
		mCodLuogoNascita = aValore;
	}

	public void setCodComuneResidenza(String aValore) {
		mCodComuneResidenza = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
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

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setIdAvvocatoStandard(BigDecimal aValore) {
		mIdAvvocatoStandard = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {

		String lStr = new String();

		lStr = "AvvocatoModel:\n" + "[ mIdAvvocato = " + mIdAvvocato + " ]\n" + "[ mCognome = " + mCognome
				+ " ]\n" + "[ mNome = " + mNome + " ]\n" + "[ mForo = " + mForo + " ]\n"
				// INIZIO: MEV_21 (avvocati)
				+ "[ mDescComuneSedeForo = " + mDescComuneSedeForo + " ]\n" + "[ mPec = " + mPec + " ]\n"
				+ "[ mFlagRegInde = " + mFlagRegInde + " ]\n" + "[ mDescrComuneStudio = " + mDescrComuneStudio
				+ " ]\n" + "[ mDescLuogoNascitaReginde = " + mDescLuogoNascitaReginde + " ]\n"
				+ "[ mCodStatoNascita = " + mCodStatoNascita + " ]\n" + "[ mDescrStatoNascita = "
				+ mDescrStatoNascita + " ]\n"
				// FINE: MEV_21
				+ "[ mIndirizzo = " + mIndirizzo + " ]\n" + "[ mTelefono = " + mTelefono + " ]\n"
				+ "[ mFax = " + mFax + " ]\n" + "[ mEMail = " + mEMail + " ]\n" + "[ mCodiceFiscale = "
				+ mCodiceFiscale + " ]\n" + "[ mProvincia = " + mProvincia + " ]\n" + "[ mCap = " + mCap
				+ " ]\n" + "[ mFlagVisualizza = " + mFlagVisualizza + " ]\n" + "[ mCodComuneResidenza = "
				+ mCodComuneResidenza + " ]\n" + "[ mCodLuogoNascita = " + mCodLuogoNascita + " ]\n"
				+ "[ mDataNascita = " + mDataNascita + " ]\n" + "[ mDataSospensione = " + mDataSospensione
				+ " ]\n" + "[ mDataRadiazione = " + mDataRadiazione + " ]\n" + "[ mCodNonAttivita = "
				+ mCodNonAttivita + " ]\n" + "[ mNote = " + mNote + " ]\n" + "[ mFlagCancellato = "
				+ mFlagCancellato + " ]\n" + "[ mCodUffAppartenenza = " + mCodUffAppartenenza + " ]\n"
				+ "[ mCodOperatoreInserimento = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento = " + mDataInserimento + " ]\n" + "[ mCodUfficioInserimento = "
				+ mCodUfficioInserimento + " ]\n" + "[ mCodOperatoreAggiornamento = "
				+ mCodOperatoreAggiornamento + " ]\n" + "[ mDataAggiornamento = " + mDataAggiornamento
				+ " ]\n" + "[ mCodUfficioAggiornamento = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mDataInizioValidita = " + mDataInizioValidita + " ]\n" + "[ mDataFineoValidita = "
				+ mDataFineValidita + " ]\n" + "[ mDescrTipo = " + mDescrTipo + " ]\n"
				+ "[ mIdAvvocatoStandard = " + mIdAvvocatoStandard + " ]\n";

		return lStr;
	}

}