package siap.sico.assistentegiudiziario.model;

/**
* <p>Title: AssistenteGiudiziarioModel</p>
* <p>Description: Classe Model che rappresenta il AssistenteGiudiziario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AssistenteGiudiziarioModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 3359877205056618823L;

	private BigDecimal mIdAssistenteGiudiziario;
	private String mCognome;
	private String mNome;
	private String mCodUfficioAppartenenza;
	private String mDescrUfficioAppartenenza;
	private String mFlagStato;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public AssistenteGiudiziarioModel() {
		this.mIdAssistenteGiudiziario = null;
		this.mCognome = "";
		this.mNome = "";
		this.mCodUfficioAppartenenza = "";
		this.mDescrUfficioAppartenenza = "";
		this.mFlagStato = "";
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
	}

	// COSTRUTTORE DI COPIA
	public AssistenteGiudiziarioModel(AssistenteGiudiziarioModel aModel) {
		this.mIdAssistenteGiudiziario = aModel.mIdAssistenteGiudiziario;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mFlagStato = aModel.mFlagStato;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	// COSTRUTTORE MODEL
	public AssistenteGiudiziarioModel(BigDecimal aIdAssistenteGiudiziario, String aCognome, String aNome,
			String aCodUfficioAppartenenza, String aDescrUfficioAppartenenza, String aFlagStato,
			Date aDataInizioValidita, Date aDataFineValidita, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento) {
		this.mIdAssistenteGiudiziario = aIdAssistenteGiudiziario;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mFlagStato = aFlagStato;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAssistenteGiudiziario() {
		return mIdAssistenteGiudiziario;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getCodUfficioAppartenenza() {
		return mCodUfficioAppartenenza;
	}

	public String getDescrUfficioAppartenenza() {
		return mDescrUfficioAppartenenza;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
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

	//
	// METODI SET()
	//

	public void setIdAssistenteGiudiziario(BigDecimal aValore) {
		mIdAssistenteGiudiziario = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setCodUfficioAppartenenza(String aValore) {
		mCodUfficioAppartenenza = aValore;
	}

	public void setDescrUfficioAppartenenza(String aValore) {
		mDescrUfficioAppartenenza = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
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

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAssistenteGiudiziario + " - " + mCognome + " - " + mNome + " - "
				+ mCodUfficioAppartenenza + " - " + mDescrUfficioAppartenenza + " - " + mFlagStato + " - "
				+ mDataInizioValidita + " - " + mDataFineValidita + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento;

		return lStr;
	}

}