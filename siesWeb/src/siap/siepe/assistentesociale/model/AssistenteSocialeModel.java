package siap.siepe.assistentesociale.model;

/**
* <p>Title: AssistenteSocialeModel</p>
* <p>Description: Classe Model che rappresenta il AssistenteSociale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AssistenteSocialeModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -6939173019052296435L;
	private BigDecimal mIdAssistenteSociale;
	private String mCognome;
	private String mNome;
	private String mIndirizzo;
	private String mTelefono;
	private String mCodUfficioAppartenenza;
	private String mDescrUfficioAppartenenza;
	private String mEmail;
	private String mFax;
	private String mCellulare;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mFlagStato;
	private String mDescrFlagStato;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mCodiceFiscale;
	// private String mDescriceFiscale;

	// COSTRUTTORE DI DEFAULT
	public AssistenteSocialeModel() {
		this.mIdAssistenteSociale = null;
		this.mCognome = "";
		this.mNome = "";
		this.mIndirizzo = "";
		this.mTelefono = "";
		this.mCodUfficioAppartenenza = "";
		this.mDescrUfficioAppartenenza = "";
		this.mEmail = "";
		this.mFax = "";
		this.mCellulare = "";
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mFlagStato = "";
		this.mDescrFlagStato = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mCodiceFiscale = "";
	}

	// COSTRUTTORE DI COPIA
	public AssistenteSocialeModel(AssistenteSocialeModel aModel) {
		this.mIdAssistenteSociale = aModel.mIdAssistenteSociale;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mIndirizzo = aModel.mIndirizzo;
		this.mTelefono = aModel.mTelefono;
		this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mEmail = aModel.mEmail;
		this.mFax = aModel.mFax;
		this.mCellulare = aModel.mCellulare;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mFlagStato = aModel.mFlagStato;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mCodiceFiscale = aModel.mCodiceFiscale;
	}

	// COSTRUTTORE MODEL
	public AssistenteSocialeModel(BigDecimal aIdAssistenteSociale, String aCognome, String aNome,
			String aIndirizzo, String aTelefono, String aCodUfficioAppartenenza,
			String aDescrUfficioAppartenenza, String aEmail, String aFax, String aCellulare,
			Date aDataInizioValidita, Date aDataFineValidita, String aFlagStato, String aDescrFlagStato,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, String aCodiceFiscale) {
		this.mIdAssistenteSociale = aIdAssistenteSociale;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mIndirizzo = aIndirizzo;
		this.mTelefono = aTelefono;
		this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mEmail = aEmail;
		this.mFax = aFax;
		this.mCellulare = aCellulare;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mFlagStato = aFlagStato;
		this.mDescrFlagStato = aDescrFlagStato;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mCodiceFiscale = aCodiceFiscale;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAssistenteSociale() {
		return mIdAssistenteSociale;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getIndirizzo() {
		return mIndirizzo;
	}

	public String getTelefono() {
		return mTelefono;
	}

	public String getCodUfficioAppartenenza() {
		return mCodUfficioAppartenenza;
	}

	public String getDescrUfficioAppartenenza() {
		return mDescrUfficioAppartenenza;
	}

	public String getEmail() {
		return mEmail;
	}

	public String getFax() {
		return mFax;
	}

	public String getCellulare() {
		return mCellulare;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getDescrFlagStato() {
		return mDescrFlagStato;
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

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public String getCodiceFiscale() {
		return mCodiceFiscale;
	}

	//
	// METODI SET()
	//
	public void setIdAssistenteSociale(BigDecimal aValore) {
		mIdAssistenteSociale = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setIndirizzo(String aValore) {
		mIndirizzo = aValore;
	}

	public void setTelefono(String aValore) {
		mTelefono = aValore;
	}

	public void setCodUfficioAppartenenza(String aValore) {
		mCodUfficioAppartenenza = aValore;
	}

	public void setDescrUfficioAppartenenza(String aValore) {
		mDescrUfficioAppartenenza = aValore;
	}

	public void setEmail(String aValore) {
		mEmail = aValore;
	}

	public void setFax(String aValore) {
		mFax = aValore;
	}

	public void setCellulare(String aValore) {
		mCellulare = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setDescrFlagStato(String aValore) {
		mDescrFlagStato = aValore;
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

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setCodiceFiscale(String aValore) {
		mCodiceFiscale = aValore;
	}

}
