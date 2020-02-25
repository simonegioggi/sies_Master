package siap.sige.giudicepopolare.model;

/**
* <p>Title: GiudicePopolareModel</p>
* <p>Description: Classe Model che rappresenta il GiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class GiudicePopolareModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5816322496819279383L;

	private BigDecimal mIdGiudicePopolare;
	private String mCognome;
	private String mNome;
	private String mIndirizzo;
	private String mCodUfficioAppartenenza;
	private String mDescrUfficioAppartenenza;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mCodRuolo;
	private String mDescrRuolo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mCodiceFiscale;
	private Date mDataNascita;
	private String mCodComuneNascita;
	private String mDescrComuneNascita;
	private String mCodStatoNascita;
	private String mDescrStatoNascita;
	private String mComuneEsteroNascita;
	private BigDecimal mSezIdSezione;
	private String mDescrSezione;
	private String mCodSesso;

	// COSTRUTTORE DI DEFAULT
	public GiudicePopolareModel() {
		this.mIdGiudicePopolare = null;
		this.mCognome = "";
		this.mNome = "";
		this.mIndirizzo = "";
		this.mCodUfficioAppartenenza = "";
		this.mDescrUfficioAppartenenza = "";
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mCodRuolo = "";
		this.mDescrRuolo = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mCodiceFiscale = "";
		this.mDataNascita = null;
		this.mCodStatoNascita = "";
		this.mDescrStatoNascita = "";
		this.mCodComuneNascita = "";
		this.mDescrComuneNascita = "";
		this.mComuneEsteroNascita = "";
		this.mSezIdSezione = null;
		this.mDescrSezione = "";
		this.mCodSesso = "";
	}

	// COSTRUTTORE DI COPIA
	public GiudicePopolareModel(GiudicePopolareModel aModel) {
		this.mIdGiudicePopolare = aModel.mIdGiudicePopolare;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mIndirizzo = aModel.mIndirizzo;
		this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mCodRuolo = aModel.mCodRuolo;
		this.mDescrRuolo = aModel.mDescrRuolo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mCodiceFiscale = aModel.mCodiceFiscale;
		this.mDataNascita = aModel.mDataNascita;
		this.mCodStatoNascita = aModel.mCodStatoNascita;
		this.mDescrStatoNascita = aModel.mDescrStatoNascita;
		this.mCodComuneNascita = aModel.mCodComuneNascita;
		this.mDescrComuneNascita = aModel.mDescrComuneNascita;
		this.mComuneEsteroNascita = aModel.mComuneEsteroNascita;
		this.mSezIdSezione = aModel.mSezIdSezione;
		this.mDescrSezione = aModel.mDescrSezione;
		this.mCodSesso = aModel.mCodSesso;
	}

	// COSTRUTTORE MODEL
	public GiudicePopolareModel(BigDecimal aIdGiudicePopolare, String aCognome, String aNome,
			String aIndirizzo, String aCodUfficioAppartenenza, String aDescrUfficioAppartenenza,
			Date aDataInizioValidita, Date aDataFineValidita, String aCodRuolo, String aDescrRuolo,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, String aCodiceFiscale,
			Date aDataNascita, String aCodStatoNascita, String aDescrStatoNascita, String aCodComuneNascita,
			String aDescrComuneNascita, String aComuneEsteroNascita, BigDecimal aSezIdSezione,
			String aDescrSezione, String aCodSesso) {
		this.mIdGiudicePopolare = aIdGiudicePopolare;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mIndirizzo = aIndirizzo;
		this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mCodRuolo = aCodRuolo;
		this.mDescrRuolo = aDescrRuolo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mCodiceFiscale = aCodiceFiscale;
		this.mDataNascita = aDataNascita;
		this.mCodStatoNascita = aCodStatoNascita;
		this.mDescrStatoNascita = aDescrStatoNascita;
		this.mCodComuneNascita = aCodComuneNascita;
		this.mDescrComuneNascita = aDescrComuneNascita;
		this.mComuneEsteroNascita = aComuneEsteroNascita;
		this.mSezIdSezione = aSezIdSezione;
		this.mDescrSezione = aDescrSezione;
		this.mCodSesso = aCodSesso;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdGiudicePopolare() {
		return mIdGiudicePopolare;
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

	public String getCodUfficioAppartenenza() {
		return mCodUfficioAppartenenza;
	}

	public String getDescrUfficioAppartenenza() {
		return mDescrUfficioAppartenenza;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public String getCodRuolo() {
		return mCodRuolo;
	}

	public String getDescrRuolo() {
		return mDescrRuolo;
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

	public Date getDataNascita() {
		return mDataNascita;
	}

	public String getCodStatoNascita() {
		return mCodStatoNascita;
	}

	public String getDescrStatoNascita() {
		return mDescrStatoNascita;
	}

	public String getCodComuneNascita() {
		return mCodComuneNascita;
	}

	public String getDescrComuneNascita() {
		return mDescrComuneNascita;
	}

	public String getComuneEsteroNascita() {
		return mComuneEsteroNascita;
	}

	public BigDecimal getSezIdSezione() {
		return mSezIdSezione;
	}

	public String getDescrSezione() {
		return mDescrSezione;
	}

	public String getCodSesso() {
		return mCodSesso;
	}

	//
	// METODI SET()
	//
	public void setIdGiudicePopolare(BigDecimal aValore) {
		mIdGiudicePopolare = aValore;
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

	public void setCodUfficioAppartenenza(String aValore) {
		mCodUfficioAppartenenza = aValore;
	}

	public void setDescrUfficioAppartenenza(String aValore) {
		mDescrUfficioAppartenenza = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setCodRuolo(String aValore) {
		mCodRuolo = aValore;
	}

	public void setDescrRuolo(String aValore) {
		mDescrRuolo = aValore;
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

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setCodStatoNascita(String aValore) {
		mCodStatoNascita = aValore;
	}

	public void setDescrStatoNascita(String aValore) {
		mDescrStatoNascita = aValore;
	}

	public void setCodComuneNascita(String aValore) {
		mCodComuneNascita = aValore;
	}

	public void setDescrComuneNascita(String aValore) {
		mDescrComuneNascita = aValore;
	}

	public void setComuneEsteroNascita(String aValore) {
		mComuneEsteroNascita = aValore;
	}

	public void setSezIdSezione(BigDecimal aValore) {
		mSezIdSezione = aValore;
	}

	public void setDescrSezione(String aValore) {
		mDescrSezione = aValore;
	}

	public void setCodSesso(String aValore) {
		mCodSesso = aValore;
	}

}