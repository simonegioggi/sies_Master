package siap.siep.storicoavvocato.model;

/**
* <p>Title: StoricoAvvocatoModel</p>
* <p>Description: Classe Model che rappresenta il StoricoAvvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class StoricoAvvocatoModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -2812824150297332161L;

	private BigDecimal mIdStoricoAvvocato;
	private String mCognome;
	private String mNome;
	private String mForo;
	private String mIndirizzo;
	private String mTelefono;
	private String mFax;
	private String mEMail;
	private String mCodiceFiscale;
	private BigDecimal mFlagVisualizza;
	private String mCodComuneResidenza;
	private String mDescrComuneResidenza;
	private String mCodLuogoNascita;
	private String mDescrLuogoNascita;
	private Date mDataNascita;
	private Date mDataSospesoFinoAl;
	private Date mDataRadiatoDal;
	private String mCodNonAttivita;
	private String mDescrNonAttivita;
	private String mNote;
	private String mCodUfficioAppartenenza;
	private String mDescrUfficioAppartenenza;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mProvincia;
	private String mCap;
	private BigDecimal mIdAvvocatoStandard;
	private BigDecimal mAvvIdAvvocato;
	private String mFlagCancellato;

	// COSTRUTTORE DI DEFAULT
	public StoricoAvvocatoModel() {
		this.mIdStoricoAvvocato = null;
		this.mCognome = "";
		this.mNome = "";
		this.mForo = "";
		this.mIndirizzo = "";
		this.mTelefono = "";
		this.mFax = "";
		this.mEMail = "";
		this.mCodiceFiscale = "";
		this.mFlagVisualizza = new BigDecimal(1);
		this.mCodComuneResidenza = "";
		this.mDescrComuneResidenza = "";
		this.mCodLuogoNascita = "";
		this.mDescrLuogoNascita = "";
		this.mDataNascita = null;
		this.mDataSospesoFinoAl = null;
		this.mDataRadiatoDal = null;
		this.mCodNonAttivita = "";
		this.mDescrNonAttivita = "";
		this.mNote = "";
		this.mCodUfficioAppartenenza = "";
		this.mDescrUfficioAppartenenza = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mProvincia = "";
		this.mCap = "";
		this.mIdAvvocatoStandard = null;
		this.mAvvIdAvvocato = null;
		this.mFlagCancellato = "";

	}

	// COSTRUTTORE DI COPIA
	public StoricoAvvocatoModel(StoricoAvvocatoModel aModel) {
		this.mIdStoricoAvvocato = aModel.mIdStoricoAvvocato;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mForo = aModel.mForo;
		this.mIndirizzo = aModel.mIndirizzo;
		this.mTelefono = aModel.mTelefono;
		this.mFax = aModel.mFax;
		this.mEMail = aModel.mEMail;
		this.mCodiceFiscale = aModel.mCodiceFiscale;
		this.mFlagVisualizza = aModel.mFlagVisualizza;
		this.mCodComuneResidenza = aModel.mCodComuneResidenza;
		this.mDescrComuneResidenza = aModel.mDescrComuneResidenza;
		this.mCodLuogoNascita = aModel.mCodLuogoNascita;
		this.mDescrLuogoNascita = aModel.mDescrLuogoNascita;
		this.mDataNascita = aModel.mDataNascita;
		this.mDataSospesoFinoAl = aModel.mDataSospesoFinoAl;
		this.mDataRadiatoDal = aModel.mDataRadiatoDal;
		this.mCodNonAttivita = aModel.mCodNonAttivita;
		this.mDescrNonAttivita = aModel.mDescrNonAttivita;
		this.mNote = aModel.mNote;
		this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mProvincia = aModel.mProvincia;
		this.mCap = aModel.mCap;
		this.mIdAvvocatoStandard = aModel.mIdAvvocatoStandard;
		this.mAvvIdAvvocato = aModel.mAvvIdAvvocato;
		this.mFlagCancellato = aModel.mFlagCancellato;

	}

	// COSTRUTTORE MODEL
	public StoricoAvvocatoModel(BigDecimal aIdStoricoAvvocato, String aCognome, String aNome, String aForo,
			String aIndirizzo, String aTelefono, String aFax, String aEMail, String aCodiceFiscale,
			BigDecimal aFlagVisualizza, String aCodComuneResidenza, String aDescrComuneResidenza,
			String aCodLuogoNascita, String aDescrLuogoNascita, Date aDataNascita, Date aDataSospesoFinoAl,
			Date aDataRadiatoDal, String aCodNonAttivita, String aDescrNonAttivita, String aNote,
			String aCodUfficioAppartenenza, String aDescrUfficioAppartenenza, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aProvincia, String aCap, BigDecimal aIdAvvocatoStandard, BigDecimal aAvvIdAvvocato,
			String aFlagCancellato) {
		this.mIdStoricoAvvocato = aIdStoricoAvvocato;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mForo = aForo;
		this.mIndirizzo = aIndirizzo;
		this.mTelefono = aTelefono;
		this.mFax = aFax;
		this.mEMail = aEMail;
		this.mCodiceFiscale = aCodiceFiscale;
		this.mFlagVisualizza = aFlagVisualizza;
		this.mCodComuneResidenza = aCodComuneResidenza;
		this.mDescrComuneResidenza = aDescrComuneResidenza;
		this.mCodLuogoNascita = aCodLuogoNascita;
		this.mDescrLuogoNascita = aDescrLuogoNascita;
		this.mDataNascita = aDataNascita;
		this.mDataSospesoFinoAl = aDataSospesoFinoAl;
		this.mDataRadiatoDal = aDataRadiatoDal;
		this.mCodNonAttivita = aCodNonAttivita;
		this.mDescrNonAttivita = aDescrNonAttivita;
		this.mNote = aNote;
		this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mProvincia = aProvincia;
		this.mCap = aCap;
		this.mIdAvvocatoStandard = aIdAvvocatoStandard;
		this.mAvvIdAvvocato = aAvvIdAvvocato;
		this.mFlagCancellato = aFlagCancellato;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdStoricoAvvocato() {
		return mIdStoricoAvvocato;
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

	public String getCodiceFiscale() {
		return mCodiceFiscale;
	}

	public BigDecimal getFlagVisualizza() {
		return mFlagVisualizza;
	}

	public String getCodComuneResidenza() {
		return mCodComuneResidenza;
	}

	public String getDescrComuneResidenza() {
		return mDescrComuneResidenza;
	}

	public String getCodLuogoNascita() {
		return mCodLuogoNascita;
	}

	public String getDescrLuogoNascita() {
		return mDescrLuogoNascita;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	public Date getDataSospesoFinoAl() {
		return mDataSospesoFinoAl;
	}

	public Date getDataRadiatoDal() {
		return mDataRadiatoDal;
	}

	public String getCodNonAttivita() {
		return mCodNonAttivita;
	}

	public String getDescrNonAttivita() {
		return mDescrNonAttivita;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodUfficioAppartenenza() {
		return mCodUfficioAppartenenza;
	}

	public String getDescrUfficioAppartenenza() {
		return mDescrUfficioAppartenenza;
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

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
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

	public String getCap() {
		return mCap;
	}

	public String getProvincia() {
		return mProvincia;
	}

	public BigDecimal getIdAvvocatoStandard() {
		return mIdAvvocatoStandard;
	}

	public BigDecimal getAvvIdAvvocato() {
		return mAvvIdAvvocato;
	}

	public String getFlagCancellato() {
		return mFlagCancellato;
	}

	//
	// METODI SET()
	//

	public void setIdStoricoAvvocato(BigDecimal aValore) {
		mIdStoricoAvvocato = aValore;
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

	public void setCodiceFiscale(String aValore) {
		mCodiceFiscale = aValore;
	}

	public void setFlagVisualizza(BigDecimal aValore) {
		mFlagVisualizza = aValore;
	}

	public void setCodComuneResidenza(String aValore) {
		mCodComuneResidenza = aValore;
	}

	public void setDescrComuneResidenza(String aValore) {
		mDescrComuneResidenza = aValore;
	}

	public void setCodLuogoNascita(String aValore) {
		mCodLuogoNascita = aValore;
	}

	public void setDescrLuogoNascita(String aValore) {
		mDescrLuogoNascita = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setDataSospesoFinoAl(Date aValore) {
		mDataSospesoFinoAl = aValore;
	}

	public void setDataRadiatoDal(Date aValore) {
		mDataRadiatoDal = aValore;
	}

	public void setCodNonAttivita(String aValore) {
		mCodNonAttivita = aValore;
	}

	public void setDescrNonAttivita(String aValore) {
		mDescrNonAttivita = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodUfficioAppartenenza(String aValore) {
		mCodUfficioAppartenenza = aValore;
	}

	public void setDescrUfficioAppartenenza(String aValore) {
		mDescrUfficioAppartenenza = aValore;
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

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
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

	public void setProvincia(String aValore) {
		mProvincia = aValore;
	}

	public void setCap(String aValore) {
		mCap = aValore;
	}

	public void setIdAvvocatoStandard(BigDecimal aValore) {
		mIdAvvocatoStandard = aValore;
	}

	public void setAvvIdAvvocato(BigDecimal aValore) {
		mAvvIdAvvocato = aValore;
	}

	public void setFlagCancellato(String aValore) {
		mFlagCancellato = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdStoricoAvvocato + " - " + mCognome + " - " + mNome + " - " + mForo + " - " + mIndirizzo
				+ " - " + mTelefono + " - " + mFax + " - " + mEMail + " - " + mCodiceFiscale + " - "
				+ mFlagVisualizza + " - " + mCodComuneResidenza + " - " + mDescrComuneResidenza + " - "
				+ mCodLuogoNascita + " - " + mDescrLuogoNascita + " - " + mDataNascita + " - "
				+ mDataSospesoFinoAl + " - " + mDataRadiatoDal + " - " + mCodNonAttivita + " - "
				+ mDescrNonAttivita + " - " + mNote + " - " + mCodUfficioAppartenenza + " - "
				+ mDescrUfficioAppartenenza + " - " + mCodOperatoreInserimento + " - " + mDataInserimento
				+ " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mProvincia + " - " + mCap + " - " + mIdAvvocatoStandard + " - " + mAvvIdAvvocato
				+ " - " + mFlagCancellato;

		return lStr;
	}

}