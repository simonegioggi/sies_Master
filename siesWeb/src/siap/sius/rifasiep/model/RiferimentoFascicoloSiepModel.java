package siap.sius.rifasiep.model;

/**
* <p>Title: RiferimentoFascicoloSiepModel</p>
* <p>Description: Classe Model che rappresenta il RiferimentoFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class RiferimentoFascicoloSiepModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 2021712031969515784L;
	private BigDecimal mIdRiferimentoFascicoloSiep;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mAnnoFascicoloSiep;
	private BigDecimal mProgrFascicoloSiep;
	private String mCodUffFascicoloSiep;
	private String mDescrUffFascicoloSiep;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mDataProvvedimento;
	private BigDecimal mAnnoProvvedimento;
	private String mNumeroProvvedimento;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private Date mDataIrrevocabilita;
	private Date mDataFineValidita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSiuIdFascicoloSius;
	private String mNote;
	private String mFlagMS;
	private String mFlagFasSiusUnif;
	// TODO carmela (verificare)
	// Identifica se si tratta di un Titolo Esecutivo Principale o Altro Titolo Esecutivo
	// associato al Procedimento Sius
	private String mFlagRifTitoloEsecutivo;

	// COSTRUTTORE DI DEFAULT
	public RiferimentoFascicoloSiepModel() {
		this.mIdRiferimentoFascicoloSiep = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mAnnoFascicoloSiep = null;
		this.mProgrFascicoloSiep = null;
		this.mCodUffFascicoloSiep = "";
		this.mDescrUffFascicoloSiep = "";
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mDataProvvedimento = null;
		this.mAnnoProvvedimento = null;
		this.mNumeroProvvedimento = "";
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mDataIrrevocabilita = null;
		this.mDataFineValidita = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSiuIdFascicoloSius = null;
		this.mNote = "";
		this.mFlagMS = "";
		this.mFlagRifTitoloEsecutivo = "";
		this.mFlagFasSiusUnif = "";
	}

	// COSTRUTTORE DI COPIA
	public RiferimentoFascicoloSiepModel(RiferimentoFascicoloSiepModel aModel) {
		this.mIdRiferimentoFascicoloSiep = aModel.mIdRiferimentoFascicoloSiep;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mAnnoFascicoloSiep = aModel.mAnnoFascicoloSiep;
		this.mProgrFascicoloSiep = aModel.mProgrFascicoloSiep;
		this.mCodUffFascicoloSiep = aModel.mCodUffFascicoloSiep;
		this.mDescrUffFascicoloSiep = aModel.mDescrUffFascicoloSiep;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mDataProvvedimento = aModel.mDataProvvedimento;
		this.mAnnoProvvedimento = aModel.mAnnoProvvedimento;
		this.mNumeroProvvedimento = aModel.mNumeroProvvedimento;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mDataIrrevocabilita = aModel.mDataIrrevocabilita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mNote = aModel.mNote;
		this.mFlagMS = aModel.mFlagMS;
		this.mFlagFasSiusUnif = aModel.mFlagFasSiusUnif;

	}

	// COSTRUTTORE MODEL
	public RiferimentoFascicoloSiepModel(BigDecimal aIdRiferimentoFascicoloSiep,
			BigDecimal aFasSieIdFascicoloSiep, BigDecimal aAnnoFascicoloSiep, BigDecimal aProgrFascicoloSiep,
			String aCodUffFascicoloSiep, String aDescrUffFascicoloSiep, String aCodTipoProvvedimento,
			String aDescrTipoProvvedimento, Date aDataProvvedimento, BigDecimal aAnnoProvvedimento,
			String aNumeroProvvedimento, String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente,
			String aCodLuogoEmittente, String aDescrLuogoEmittente, Date aDataIrrevocabilita,
			Date aDataFineValidita, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSiuIdFascicoloSius, String aNote, String aFlagMS, String aFlagFasSiusUnif) {
		this.mIdRiferimentoFascicoloSiep = aIdRiferimentoFascicoloSiep;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mAnnoFascicoloSiep = aAnnoFascicoloSiep;
		this.mProgrFascicoloSiep = aProgrFascicoloSiep;
		this.mCodUffFascicoloSiep = aCodUffFascicoloSiep;
		this.mDescrUffFascicoloSiep = aDescrUffFascicoloSiep;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mDataProvvedimento = aDataProvvedimento;
		this.mAnnoProvvedimento = aAnnoProvvedimento;
		this.mNumeroProvvedimento = aNumeroProvvedimento;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mDataIrrevocabilita = aDataIrrevocabilita;
		this.mDataFineValidita = aDataFineValidita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mNote = aNote;
		this.mFlagMS = aFlagMS;
		this.mFlagFasSiusUnif = aFlagFasSiusUnif;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdRiferimentoFascicoloSiep() {
		return mIdRiferimentoFascicoloSiep;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getAnnoFascicoloSiep() {
		return mAnnoFascicoloSiep;
	}

	public BigDecimal getProgrFascicoloSiep() {
		return mProgrFascicoloSiep;
	}

	public String getCodUffFascicoloSiep() {
		return mCodUffFascicoloSiep;
	}

	public String getDescrUffFascicoloSiep() {
		return mDescrUffFascicoloSiep;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public Date getDataProvvedimento() {
		return mDataProvvedimento;
	}

	public BigDecimal getAnnoProvvedimento() {
		return mAnnoProvvedimento;
	}

	public String getNumeroProvvedimento() {
		return mNumeroProvvedimento;
	}

	public String getCodTipoAutoritaEmittente() {
		return mCodTipoAutoritaEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilita;
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public String getNote() {
		return mNote;
	}

	public String getFlagMS() {
		return mFlagMS;
	}

	public String getFlagRifTitoloEsecutivo() {
		return mFlagRifTitoloEsecutivo;
	}

	public String getFlagFasSiusUnif() {
		return mFlagFasSiusUnif;
	}

	//
	// METODI SET()
	//
	public void setIdRiferimentoFascicoloSiep(BigDecimal aValore) {
		mIdRiferimentoFascicoloSiep = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setAnnoFascicoloSiep(BigDecimal aValore) {
		mAnnoFascicoloSiep = aValore;
	}

	public void setProgrFascicoloSiep(BigDecimal aValore) {
		mProgrFascicoloSiep = aValore;
	}

	public void setCodUffFascicoloSiep(String aValore) {
		mCodUffFascicoloSiep = aValore;
	}

	public void setDescrUffFascicoloSiep(String aValore) {
		mDescrUffFascicoloSiep = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setDataProvvedimento(Date aValore) {
		mDataProvvedimento = aValore;
	}

	public void setAnnoProvvedimento(BigDecimal aValore) {
		mAnnoProvvedimento = aValore;
	}

	public void setNumeroProvvedimento(String aValore) {
		mNumeroProvvedimento = aValore;
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		mCodTipoAutoritaEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilita = aValore;
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setFlagMS(String aValore) {
		mFlagMS = aValore;
	}

	public void setFlagRifTitoloEsecutivo(String aValore) {
		mFlagRifTitoloEsecutivo = aValore;
	}

	public void setFlagFasSiusUnif(String aValore) {
		mFlagFasSiusUnif = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdRiferimentoFascicoloSiep + " - " + mFasSieIdFascicoloSiep + " - " + mAnnoFascicoloSiep
				+ " - " + mProgrFascicoloSiep + " - " + mCodUffFascicoloSiep + " - " + mDescrUffFascicoloSiep
				+ " - " + mCodTipoProvvedimento + " - " + mDescrTipoProvvedimento + " - " + mDataProvvedimento
				+ " - " + mAnnoProvvedimento + " - " + mNumeroProvvedimento + " - "
				+ mCodTipoAutoritaEmittente + " - " + mDescrTipoAutoritaEmittente + " - " + mCodLuogoEmittente
				+ " - " + mDescrLuogoEmittente + " - " + mDataIrrevocabilita + " - " + mDataFineValidita
				+ " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento
				+ " - " + mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - "
				+ mDataAggiornamento + " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento
				+ " - " + mFasSiuIdFascicoloSius + " - " + mNote + " - " + mFlagMS + " - " + mFlagFasSiusUnif;

		return lStr;
	}
}