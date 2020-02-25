package siap.sius.udienza.model;

/**
* <p>Title: UdienzaModel</p>
* <p>Description: Classe Model che rappresenta il Udienza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class UdienzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 989356395816916804L;

	private BigDecimal mIdUdienza;
	private Date mDataUdienza;
	private String mCodPresidente;
	private String mDescrPresidente;
	private String mCodGiudice1;
	private String mDescrGiudice1;
	private String mCodGiudice2;
	private String mDescrGiudice2;
	private String mCodPg;
	private String mDescrPg;
	private BigDecimal mCodIdEsperto1;
	private String mDescrIdEsperto1;
	private BigDecimal mCodIdEsperto2;
	private String mDescrIdEsperto2;
	private BigDecimal mCodIdAssistente;
	private String mDescrIdAssistente;
	// private String mFlagRinviata;
	private BigDecimal mNumeroMaxFascicoli;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private Date mDataUdienzaFine; // data di udienza fine per definire un range di date nella ricerca
	private String mNomeTemplate;
	private String mLuogoUdienza;
	private String mCodUfficioAppartenenza;
	private String mDescrUfficioAppartenenza;
	private BigDecimal mNumCollegio;
	private String mOraInizio;
	private String mMinInizio;
	private String mOraFine;
	private String mMinFine;
	private String mOraFineCC; // Ora di fine Camera di Consiglio
	private String mMinFineCC; // Minuti di fine Camera di Consiglio

	// COSTRUTTORE DI DEFAULT
	public UdienzaModel() {
		this.mIdUdienza = null;
		this.mDataUdienza = null;
		this.mCodPresidente = "-";
		this.mDescrPresidente = "";
		this.mCodGiudice1 = "-";
		this.mDescrGiudice1 = "";
		this.mCodGiudice2 = "-";
		this.mDescrGiudice2 = "";
		this.mCodPg = "-";
		this.mDescrPg = "";
		this.mCodIdEsperto1 = new BigDecimal(0);
		this.mDescrIdEsperto1 = "";
		this.mCodIdEsperto2 = new BigDecimal(0);
		this.mDescrIdEsperto2 = "";
		this.mCodIdAssistente = new BigDecimal(0);
		this.mDescrIdAssistente = "";
		// this.mFlagRinviata = "";
		this.mNumeroMaxFascicoli = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDataUdienzaFine = null;
		this.mNomeTemplate = "";
		this.mLuogoUdienza = "";
		this.mCodUfficioAppartenenza = "";
		this.mDescrUfficioAppartenenza = "";
		this.mNumCollegio = null;
		this.mOraInizio = "";
		this.mMinInizio = "";
		this.mOraFine = "";
		this.mMinFine = "";
		this.mOraFineCC = "";
		this.mMinFineCC = "";
	}

	// COSTRUTTORE DI COPIA
	public UdienzaModel(UdienzaModel aModel) {
		this.mIdUdienza = aModel.mIdUdienza;
		this.mDataUdienza = aModel.mDataUdienza;
		this.mCodPresidente = aModel.mCodPresidente;
		this.mDescrPresidente = aModel.mDescrPresidente;
		this.mCodGiudice1 = aModel.mCodGiudice1;
		this.mDescrGiudice1 = aModel.mDescrGiudice1;
		this.mCodGiudice2 = aModel.mCodGiudice2;
		this.mDescrGiudice2 = aModel.mDescrGiudice2;
		this.mCodPg = aModel.mCodPg;
		this.mDescrPg = aModel.mDescrPg;
		this.mCodIdEsperto1 = aModel.mCodIdEsperto1;
		this.mDescrIdEsperto1 = aModel.mDescrIdEsperto1;
		this.mCodIdEsperto2 = aModel.mCodIdEsperto2;
		this.mDescrIdEsperto2 = aModel.mDescrIdEsperto2;
		this.mCodIdAssistente = aModel.mCodIdAssistente;
		this.mDescrIdAssistente = aModel.mDescrIdAssistente;
		// this.mFlagRinviata = aModel.mFlagRinviata;
		this.mNumeroMaxFascicoli = aModel.mNumeroMaxFascicoli;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mNomeTemplate = aModel.mNomeTemplate;
		this.mLuogoUdienza = aModel.mLuogoUdienza;
		this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mNumCollegio = aModel.mNumCollegio;
		this.mOraInizio = aModel.mOraInizio;
		this.mMinInizio = aModel.mMinInizio;
		this.mOraFine = aModel.mOraFine;
		this.mMinFine = aModel.mMinFine;
		this.mOraFineCC = aModel.mOraFineCC;
		this.mMinFineCC = aModel.mMinFineCC;
	}

	// COSTRUTTORE MODEL
	public UdienzaModel(BigDecimal aIdUdienza, Date aDataUdienza, String aCodPresidente,
			String aDescrPresidente, String aCodGiudice1, String aDescrGiudice1, String aCodGiudice2,
			String aDescrGiudice2, String aCodPg, String aDescrPg, BigDecimal aCodIdEsperto1,
			String aDescrIdEsperto1, BigDecimal aCodIdEsperto2, String aDescrIdEsperto2,
			BigDecimal aCodIdAssistente, String aDescrIdAssistente,
			// String aFlagRinviata,
			BigDecimal aNumeroMaxFascicoli, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			String aLuogoUdienza, String aCodUfficioAppartenenza, String aDescrUfficioAppartenenza,
			BigDecimal aNumCollegio, String aOraInizio, String aMinInizio, String aOraFine, String aMinFine,
			String aOraFineCC, String aMinFineCC) {
		this.mIdUdienza = aIdUdienza;
		this.mDataUdienza = aDataUdienza;
		this.mCodPresidente = aCodPresidente;
		this.mDescrPresidente = aDescrPresidente;
		this.mCodGiudice1 = aCodGiudice1;
		this.mDescrGiudice1 = aDescrGiudice1;
		this.mCodGiudice2 = aCodGiudice2;
		this.mDescrGiudice2 = aDescrGiudice2;
		this.mCodPg = aCodPg;
		this.mDescrPg = aDescrPg;
		this.mCodIdEsperto1 = aCodIdEsperto1;
		this.mDescrIdEsperto1 = aDescrIdEsperto1;
		this.mCodIdEsperto2 = aCodIdEsperto2;
		this.mDescrIdEsperto2 = aDescrIdEsperto2;
		this.mCodIdAssistente = aCodIdAssistente;
		this.mDescrIdAssistente = aDescrIdAssistente;
		// this.mFlagRinviata = aFlagRinviata;
		this.mNumeroMaxFascicoli = aNumeroMaxFascicoli;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mLuogoUdienza = aLuogoUdienza;
		this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mNumCollegio = aNumCollegio;
		this.mOraInizio = aOraInizio;
		this.mMinInizio = aMinInizio;
		this.mOraFine = aOraFine;
		this.mMinFine = aMinFine;
		this.mOraFineCC = aOraFineCC;
		this.mMinFineCC = aMinFineCC;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdUdienza() {
		return mIdUdienza;
	}

	public Date getDataUdienza() {
		return mDataUdienza;
	}

	public String getCodPresidente() {
		return mCodPresidente;
	}

	public String getDescrPresidente() {
		return mDescrPresidente;
	}

	public String getCodGiudice1() {
		return mCodGiudice1;
	}

	public String getDescrGiudice1() {
		return mDescrGiudice1;
	}

	public String getCodGiudice2() {
		return mCodGiudice2;
	}

	public String getDescrGiudice2() {
		return mDescrGiudice2;
	}

	public String getCodPg() {
		return mCodPg;
	}

	public String getDescrPg() {
		return mDescrPg;
	}

	public BigDecimal getCodIdEsperto1() {
		return mCodIdEsperto1;
	}

	public String getDescrIdEsperto1() {
		return mDescrIdEsperto1;
	}

	public BigDecimal getCodIdEsperto2() {
		return mCodIdEsperto2;
	}

	public String getDescrIdEsperto2() {
		return mDescrIdEsperto2;
	}

	public BigDecimal getCodIdAssistente() {
		return mCodIdAssistente;
	}

	public String getDescrIdAssistente() {
		return mDescrIdAssistente;
	}

	// public String getFlagRinviata() { return mFlagRinviata; }
	public BigDecimal getNumeroMaxFascicoli() {
		return mNumeroMaxFascicoli;
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

	public Date getDataUdienzaFine() {
		return mDataUdienzaFine;
	}

	public String getNomeTemplate() {
		return mNomeTemplate;
	}

	public String getLuogoUdienza() {
		return mLuogoUdienza;
	}

	public String getCodUfficioAppartenenza() {
		return mCodUfficioAppartenenza;
	}

	public String getDescrUfficioAppartenenza() {
		return mDescrUfficioAppartenenza;
	}

	public BigDecimal getNumCollegio() {
		return mNumCollegio;
	}

	public String getOraInizio() {
		return mOraInizio;
	}

	public String getMinInizio() {
		return mMinInizio;
	}

	public String getOraFine() {
		return mOraFine;
	}

	public String getMinFine() {
		return mMinFine;
	}

	public String getOraFineCC() {
		return mOraFineCC;
	}

	public String getMinFineCC() {
		return mMinFineCC;
	}

	//
	// METODI SET()
	//

	public void setIdUdienza(BigDecimal aValore) {
		mIdUdienza = aValore;
	}

	public void setDataUdienza(Date aValore) {
		mDataUdienza = aValore;
	}

	public void setCodPresidente(String aValore) {
		mCodPresidente = aValore;
	}

	public void setDescrPresidente(String aValore) {
		mDescrPresidente = aValore;
	}

	public void setCodGiudice1(String aValore) {
		mCodGiudice1 = aValore;
	}

	public void setDescrGiudice1(String aValore) {
		mDescrGiudice1 = aValore;
	}

	public void setCodGiudice2(String aValore) {
		mCodGiudice2 = aValore;
	}

	public void setDescrGiudice2(String aValore) {
		mDescrGiudice2 = aValore;
	}

	public void setCodPg(String aValore) {
		mCodPg = aValore;
	}

	public void setDescrPg(String aValore) {
		mDescrPg = aValore;
	}

	public void setCodIdEsperto1(BigDecimal aValore) {
		mCodIdEsperto1 = aValore;
	}

	public void setDescrIdEsperto1(String aValore) {
		mDescrIdEsperto1 = aValore;
	}

	public void setCodIdEsperto2(BigDecimal aValore) {
		mCodIdEsperto2 = aValore;
	}

	public void setDescrIdEsperto2(String aValore) {
		mDescrIdEsperto2 = aValore;
	}

	public void setCodIdAssistente(BigDecimal aValore) {
		mCodIdAssistente = aValore;
	}

	public void setDescrIdAssistente(String aValore) {
		mDescrIdAssistente = aValore;
	}

	// public void setFlagRinviata(String aValore ) { mFlagRinviata = aValore; }
	public void setNumeroMaxFascicoli(BigDecimal aValore) {
		mNumeroMaxFascicoli = aValore;
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

	public void setDataUdienzaFine(Date aValore) {
		mDataUdienzaFine = aValore;
	}

	public void setNomeTemplate(String aValore) {
		mNomeTemplate = aValore;
	}

	public void setLuogoUdienza(String aValore) {
		mLuogoUdienza = aValore;
	}

	public void setCodUfficioAppartenenza(String aValore) {
		mCodUfficioAppartenenza = aValore;
	}

	public void setDescrUfficioAppartenenza(String aValore) {
		mDescrUfficioAppartenenza = aValore;
	}

	public void setNumCollegio(BigDecimal aValore) {
		mNumCollegio = aValore;
	}

	public void setOraInizio(String aValore) {
		mOraInizio = aValore;
	}

	public void setMinInizio(String aValore) {
		mMinInizio = aValore;
	}

	public void setOraFine(String aValore) {
		mOraFine = aValore;
	}

	public void setMinFine(String aValore) {
		mMinFine = aValore;
	}

	public void setOraFineCC(String aValore) {
		mOraFineCC = aValore;
	}

	public void setMinFineCC(String aValore) {
		mMinFineCC = aValore;
	}

}