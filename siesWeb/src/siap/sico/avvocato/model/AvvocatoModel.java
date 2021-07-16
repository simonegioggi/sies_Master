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
	private String mDescComuneSedeForo; // 20210608	MEV_Scheda-21	
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
	// 20210608	MEV_Scheda-21 Aggiunta nuove colonne AVVOCATO per bonifica REGINDE.
	private String mPec;
	private String mFlagRegInde;
	private String mDescrComuneStudio;
	private String mCodStatoNascita;
	private String mDescStatoNascita;
	private String mDescLuogoNasRegInde;
	private BigDecimal mIdAvvocatoBonificato;

	// COSTRUTTORE DI DEFAULT
	public AvvocatoModel() {
		this.mIdAvvocato = null;
		this.mCognome = "";
		this.mNome = "";
		this.mDescrTipo = "";
		this.mForo = "";
		this.mDescComuneSedeForo = "";  // 20210608	MEV_Scheda-21
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
		this.mPec = "";
		this.mFlagRegInde = "";
		this.mDescrComuneStudio = "";
		this.mCodStatoNascita = "";
		this.mDescStatoNascita = "";
		this.mDescLuogoNasRegInde = "";
		this.mIdAvvocatoBonificato = null;
	}

	// COSTRUTTORE DI COPIA
	public AvvocatoModel(AvvocatoModel aModel) {
		this.mIdAvvocato = aModel.mIdAvvocato;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mForo = aModel.mForo;
		this.mDescComuneSedeForo = aModel.mDescComuneSedeForo;   // 20210608	MEV_Scheda-21
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
		this.mPec = aModel.mPec;
		this.mFlagRegInde = aModel.mFlagRegInde;
		this.mDescrComuneStudio = aModel.mDescrComuneStudio;
		this.mCodStatoNascita = aModel.mCodStatoNascita;
		this.mDescStatoNascita = aModel.mDescStatoNascita;
		this.mDescLuogoNasRegInde = aModel.mDescLuogoNasRegInde;
		this.mIdAvvocatoBonificato = aModel.mIdAvvocatoBonificato;
	}

	// COSTRUTTORE MODEL
	public AvvocatoModel(BigDecimal aIdAvvocato, String aCognome, String aNome, String aForo,
			String aDescComuneSedeForo, // 20210608	MEV_Scheda-21
			String aIndirizzo, String aTelefono, String aFax, String aEMail, String aCodLuogoNascita,
			String aCodComuneResidenza, String aDescCodLuogoNascita, String aDescCodComuneResidenza,
			Date aDataNascita, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aDescrTipo,
			String aCodUfficioInserimento, String aCodUfficioAggiornamento, Date aDataSospensione,
			Date aDataRadiazione, String aCodNonAttivita, String aCodUffAppartenenza, String aNote,
			String aFlagCancellato, String aDescrNonAttivita, String aCodiceFiscale, String aProvincia,
			String aCap, BigDecimal aFlagVisualizza, BigDecimal aIdAvvocatoStandard, 
			// 20210603	MEV_Scheda-21 Aggiunta nuove colonne AVVOCATO per bonifica REGINDE.
			String aPec, String aFlagRegInde, String aDescrComuneStudio, String aCodStatoNascita, 
			String aDescStatoNascita, String aDescLuogoNasRegInde, BigDecimal aIdAvvocatoBonificato)

	{
		this.mIdAvvocato = aIdAvvocato;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mForo = aForo;
		this.mDescComuneSedeForo = aDescComuneSedeForo;  // 20210608	MEV_Scheda-21
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
		// 20210603	MEV_Scheda-21 Aggiunta nuove colonne AVVOCATO per bonifica REGINDE.
		this.mPec = aPec;
		this.mFlagRegInde = aFlagRegInde;
		this.mDescrComuneStudio = aDescrComuneStudio;
		this.mCodStatoNascita = aCodStatoNascita;
		this.mDescStatoNascita = aDescStatoNascita;
		this.mDescLuogoNasRegInde = aDescLuogoNasRegInde;
		this.mIdAvvocatoBonificato = aIdAvvocatoBonificato;
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

	// 20210608	MEV_Scheda-21 Aggiunta nuove colonne AVVOCATO per bonifica REGINDE.
	public String getDescComuneSedeForo() {
		return mDescComuneSedeForo;
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

/*	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	} */

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

	
	public String getPec() {
		return mPec;
	}
	public String getFlagRegInde() {
		return mFlagRegInde;
	}
	public String getDescrComuneStudio() {
		return mDescrComuneStudio;
	}
	public String getCodStatoNascita() {
		return mCodStatoNascita;
	}
	public String getDescStatoNascita() {
		return mDescStatoNascita;
	}
	public String getDescLuogoNasRegInde() {
		return mDescLuogoNasRegInde;
	}
	public BigDecimal getIdAvvocatoBonificato() {
		return mIdAvvocatoBonificato;
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

	// 20210608	MEV_Scheda-21
	public void setDescComuneSedeForo(String aValore) {
		mDescComuneSedeForo = aValore;
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

	// 20210608	MEV_Scheda-21 Aggiunta nuove colonne AVVOCATO per bonifica REGINDE.
	public void setPec(String aValore) {
		mPec = aValore;
	}
	public void setFlagRegInde(String aValore) {
		mFlagRegInde = aValore;
	}
	public void setDescrComuneStudio(String aValore) {
		mDescrComuneStudio = aValore;
	}
	public void setCodStatoNascita(String aValore) {
		mCodStatoNascita = aValore;
	}
	public void setDescStatoNascita(String aValore) {
		mDescStatoNascita = aValore;
	}
	public void setDescLuogoNasRegInde(String aValore) {
		mDescLuogoNasRegInde = aValore;
	}
	public void setIdAvvocatoBonificato(BigDecimal aValore) {
		mIdAvvocatoBonificato = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {

		String lStr = new String();

		lStr = "AvvocatoModel:\n" + "[ mIdAvvocato = " + mIdAvvocato + " ]\n" + "[ mCognome = " + mCognome
				+ " ]\n" + "[ mNome = " + mNome + " ]\n" + "[ mForo = " + mForo + " ]\n"
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
				//+ "[ mDataInizioValidita = " + mDataInizioValidita + " ]\n" 
				//+ "[ mDataFineoValidita = " + mDataFineValidita + " ]\n" 
				+ "[ mDescrTipo = " + mDescrTipo + " ]\n"
				+ "[ mIdAvvocatoStandard = " + mIdAvvocatoStandard + " ]\n"
				// 20210608	MEV_Scheda-21 Aggiunta nuove colonne AVVOCATO per bonifica REGINDE.
				+ "[ mDescComuneSedeForo = " + mDescComuneSedeForo + " ]\n" + "[ mPec = " + mPec + " ]\n"
				+ "[ mFlagRegInde = " + mFlagRegInde + " ]\n" + "[ mDescrComuneStudio = " + mDescrComuneStudio
				+ " ]\n" + "[ mDescLuogoNascitaRegInde = " + mDescLuogoNasRegInde + " ]\n"
				+ "[ mCodStatoNascita = " + mCodStatoNascita + " ]\n" + "[ mDescStatoNascita = "+ mDescStatoNascita + " ]\n" + "[ mIdAvvocatoBonificato = " + mIdAvvocatoBonificato + " ]\n";

		return lStr;
	}
	
}