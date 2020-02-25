package siap.sico.utente.model;

import java.util.Date;

import f3b.security.model.UserModel;

public class UtenteViewModel extends UserModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5760883468569243924L;

	private String mCognome;
	private String mNome;
	private String mUtTelefono;
	private String mUtFax;
	private String mUtEmail;
	private Date mDataFineValidita;
	private Date mDataOraConnessione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private Date mDataUltimaModifcaPwd;
	private String mIP;
	private String mDescTipoUfficio;
	private String mDescProfilo;
	private String mComuneUfficio;

	// COSTRUTTORE DI DEFAULT
	public UtenteViewModel() {

		this.mCognome = "";
		this.mNome = "";
		this.mUtTelefono = "";
		this.mUtFax = "";
		this.mUtEmail = "";
		this.mDataFineValidita = null;
		this.mDataOraConnessione = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mDataUltimaModifcaPwd = null;
		this.mDescTipoUfficio = "";
		this.mComuneUfficio = "";
		this.mDescProfilo = "";
		this.mIP = null;
	}

	//
	// METODI GET()
	//

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getUtTelefono() {
		return mUtTelefono;
	}

	public String getUtFax() {
		return mUtFax;
	}

	public String getUtEmail() {
		return mUtEmail;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public Date getDataOraConnessione() {
		return mDataOraConnessione;
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

	public Date getDataUltimaModifcaPwd() {
		return mDataUltimaModifcaPwd;
	}

	public String getDescTipoUfficio() {
		return mDescTipoUfficio;
	}

	public String getComuneUfficio() {
		return mComuneUfficio;
	}

	public String getDescProfilo() {
		return mDescProfilo;
	}

	public String getIP() {
		return mIP;
	}
	//
	// METODI SET()
	//

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setUtTelefono(String aValore) {
		mUtTelefono = aValore;
	}

	public void setUtFax(String aValore) {
		mUtFax = aValore;
	}

	public void setUtEmail(String aValore) {
		mUtEmail = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setDataOraConnessione(Date aValore) {
		mDataOraConnessione = aValore;
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

	public void setDataUltimaModifcaPwd(Date aValore) {
		mDataUltimaModifcaPwd = aValore;
	}

	public void setIP(String aValore) {
		mIP = aValore;
	}

	public void setDescTipoUfficio(String aValore) {
		mDescTipoUfficio = aValore;
	}

	public void setComuneUfficio(String aValore) {
		mComuneUfficio = aValore;
	}

	public void setDescProfilo(String aValore) {
		mDescProfilo = aValore;
	}

	public String toString() {
		String lToString = super.toString() + " - " + this.mCognome + " - " + this.mNome + " - "
				+ this.mUtTelefono + " - " + this.mUtFax + " - " + this.mUtEmail + " - "
				+ this.mDataFineValidita + " - " + this.mDataOraConnessione + " - "
				+ this.mCodOperatoreInserimento + " - " + this.mDataInserimento + " - "
				+ this.mCodOperatoreAggiornamento + " - " + this.mDataAggiornamento + " - "
				+ this.mDataUltimaModifcaPwd + " - " + this.mIP + " - " + this.mDescTipoUfficio + " - "
				+ this.mComuneUfficio + " - " + this.mDescProfilo;

		return lToString;
	}

}